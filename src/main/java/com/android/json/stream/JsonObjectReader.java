/*
 * Copyright (C) 2012 The Aspire Mobile Market project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * Author:Michae Liu (liuhuayou@21cn.com)
 * Created Date: 2012.12.5 
 */
package com.android.json.stream;


import com.support.loader.proguard.PrimitiveName;
import com.support.loader.utils.ReflectHelper;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;



public class JsonObjectReader {
    private 	String TAG ;
    private JsonReader mReader ;
    
    public JsonObjectReader(JsonReader reader){
	TAG = "JsonObjectReader";
	mReader = reader;
    }
    
    public JsonObjectReader(InputStream is) throws Exception {
	mReader = new JsonReader(new InputStreamReader(is, "utf-8"));
	mReader.setLenient(true);
    }
    
    public JsonObjectReader(JSONObject jsonObj){
	String jsondata = jsonObj.toString();
	StringReader sr = new StringReader(jsondata);
	mReader = new JsonReader(sr);
	mReader.setLenient(true);
    }
    
    public void close(){
	if (mReader != null){
	    try {
		mReader.close();
	    } catch (Exception e) {
	    }
	    mReader = null ;
	}
    }
    
    private Field getAnnotatedFieldWith(Class<?> objclazz, String jsonname){
	if (jsonname == null){
	    return null ;
	}
	Field	[] fields ;
	PrimitiveName pn ;
	while(!objclazz.equals(Object.class)){
	    fields = objclazz.getDeclaredFields() ;
	    if (fields != null){
		for (Field f: fields){
		    pn = f.getAnnotation(PrimitiveName.class);
		    if (pn != null && jsonname.equals(pn.value())){
			return f ;
		    }
		}
	    }
	    objclazz = objclazz.getSuperclass();
	}
	return null ;
    }
    
    private Field getDeclaredField(Class<?> objclazz, String jsonname) throws NoSuchFieldException{
	if (objclazz.equals(Object.class))
	    throw new NoSuchFieldException(jsonname);
	Field field = null ;
	Class<?> saved_objclazz = objclazz ;
	while(field == null){
	    try{
		field = objclazz.getDeclaredField(jsonname);
		return field ;
	    }catch(NoSuchFieldException e){
		objclazz = objclazz.getSuperclass();
		if (objclazz.equals(Object.class)){
		    PrimitiveName pn = saved_objclazz.getAnnotation(PrimitiveName.class);
		    if (pn != null){
			field = getAnnotatedFieldWith(saved_objclazz, jsonname);
			if (field != null){
//			    AspLog.i(TAG, "getDeclaredField from annotation fieldname="+field.getName() +",jsonname="+jsonname);
			    return field ;
			}
		    }
		    throw new NoSuchFieldException(jsonname); 
		}
	    }
	}
	throw new NoSuchFieldException(jsonname);
    }
    
    private void skipReadObject() throws IOException{
	JsonToken phase = mReader.peek() ;
	if (phase == JsonToken.BEGIN_OBJECT){
	    mReader.beginObject();
	    try {
		while (mReader.hasNext()) {
		    mReader.nextName();
		    mReader.skipValue();
		}
	    } finally{
		mReader.endObject() ;
	    }
	} else if (phase == JsonToken.END_OBJECT){
	    mReader.endObject();
	}else {
	    mReader.skipValue() ;
	}
    }
    
    public void readObject(Object object) throws IOException,UniformErrorException {
	final Class<?> objclazz = object.getClass();
	Class<?> fieldclazz = null;
	String jsonname ;
	Field	field ;
	if (mReader.peek() != JsonToken.BEGIN_OBJECT) {
	    mReader.skipValue();
	} else {
	    mReader.beginObject();
	    try {
		while (mReader.hasNext()) {
		    jsonname = mReader.nextName();
		    try {
			field = getDeclaredField(objclazz, jsonname);
//			AspLog.i(TAG, "jsonname=" + jsonname + ",field=" + field.getType());
			field.setAccessible(true);
			fieldclazz = field.getType();
			readFieldObject(object, field, fieldclazz);
		    } catch (Exception e) {
			// e.printStackTrace();
//			AspLog.e(TAG,
//				"skip " + objclazz.getSimpleName() + ",field=" + jsonname + ",reason=" + e.getMessage());
			mReader.skipValue();
		    }
		}
	    } finally {
		mReader.endObject();
	    }
	}
    }
    
    private void readFieldObject (Object object,Field field,  Class<?> fieldclazz) throws IllegalArgumentException, IOException, IllegalAccessException,Exception{
	Integer classtype = JsonDataType.get(fieldclazz);
//	AspLog.i(TAG, "readFieldObject fieldclazz="+fieldclazz +",classtype="+classtype);
	if (classtype == null){ //没找到指定的读方法
	    if (fieldclazz.isArray()) {
		readObjectArray(object, field); // Object array
	    } else if (List.class.getName().equals(fieldclazz.getName())) {
		readObjectList(object, field); // Object List
	    } else {
		readObject(object, field); // Object
	    }
	    return ;
	}
	switch (classtype) {
	case JsonDataType.TYPE_INT:
	    if (fieldclazz.isArray())
		readIntArray(object, field); // int array
	    else
		readInt(object, field); // int
	    break;
	case JsonDataType.TYPE_SHORT:
	    if (fieldclazz.isArray())
		readShortArray(object, field); // short array
	    else
		readShort(object, field); // short
	    break;
	case JsonDataType.TYPE_LONG:
	    if (fieldclazz.isArray())
		readLongArray(object, field); // long array
	    else
		readLong(object, field); // long
	    break;
	case JsonDataType.TYPE_DOUBLE:
	    if (fieldclazz.isArray())
		readDoubleArray(object, field); // double array
	    else
		readDouble(object, field); // double
	    break;
	case JsonDataType.TYPE_FLOAT:
	    if (fieldclazz.isArray())
		readFloatArray(object, field); // float array
	    else
		readFloat(object, field); // float
	    break;
	case JsonDataType.TYPE_BOOLEAN:
	    if (fieldclazz.isArray())
		readBooleanArray(object, field); // boolean array
	    else
		readBoolean(object, field); // boolean
	    break;
	case JsonDataType.TYPE_BYTE:
	    if (fieldclazz.isArray())
		readByteArray(object, field); // byte array
	    else
		readByte(object, field); // byte
	    break;
	case JsonDataType.TYPE_STRING:
	    if (fieldclazz.isArray()){
		readStringArray(object, field); // String array
	    }else{
		readString(object, field); // String
	    }
	    break;
	default:
	    break;
	}
    }
    
    
    private void readInt(Object object,Field field) throws IOException, IllegalArgumentException, IllegalAccessException {
	JsonToken token = mReader.peek();
	if (token != JsonToken.STRING && token != JsonToken.NUMBER) { //Michael Liu:2012.10.9 added
	    mReader.skipValue();
	    if (Integer.class.equals(field.getType())) {
		field.set(object, null);
	    } 
	} else {
	    try {
		int val = mReader.nextInt();
		if (Integer.class.equals(field.getType())) {
		    field.set(object, Integer.valueOf(val));
		} else {
		    field.setInt(object, val);
		}
	    } catch (Exception e) { //防止因数据格式错误引起后继数据读取也失败的问题，遇数据格式错时，不对此字段进行赋值处理
		mReader.skipValue();
//		AspLog.e(TAG, "readInt fail, field="+field.getName() +",reason="+e);
	    }
	}
    }

    private void readIntArray(Object object,Field field) throws IOException, IllegalArgumentException, IllegalAccessException {
	JsonToken token = mReader.peek();
	if (token != JsonToken.BEGIN_ARRAY){
	    field.set(object, null);
	    mReader.skipValue();
	    return ;
	}
	List<Integer> ints = new ArrayList<Integer>();
	mReader.beginArray();
	while (mReader.hasNext()) {
	    token = mReader.peek();
	    if (token != JsonToken.STRING && token != JsonToken.NUMBER) {
		mReader.skipValue();
	    } else {
		try {
		    ints.add(mReader.nextInt());
		} catch (Exception e) { // 防止因数据格式错误引起后继数据读取也失败的问题，遇数据格式错时，不对此字段进行赋值处理
		    mReader.skipValue();
//		    AspLog.e(TAG, "readIntArray fail,field="+field.getName()+",reason=" + e);
		}
	    }
	}
	mReader.endArray();
	if (ints.size() == 0) {
	    field.set(object, null);
	    return;
	}
	Integer[] vals = new Integer[ints.size()];
	ints.toArray(vals);
	if (Integer[].class.equals(field.getType())) {
	    field.set(object, vals);
	} else {
	    int[] vals2 = new int[ints.size()];
	    for (int k = 0; k < vals2.length; k++){
		vals2[k] = vals[k];
	    }	    
	    field.set(object, vals2);
	}
    }
    
    private void readShort(Object object,Field field) throws IOException, IllegalArgumentException, IllegalAccessException {
	JsonToken token = mReader.peek();
	if (token != JsonToken.STRING && token != JsonToken.NUMBER) {
	    mReader.skipValue();
	    if (Short.class.equals(field.getType())) {
		field.set(object, null);
	    }
	} else {
	    try {
		short val = (short) mReader.nextInt();
		if (Short.class.equals(field.getType())) {
		    field.set(object, Short.valueOf(val));
		} else {
		    field.setShort(object, val);
		}
	    } catch (Exception e) { // 防止因数据格式错误引起后继数据读取也失败的问题，遇数据格式错时，不对此字段进行赋值处理
		mReader.skipValue();
//		AspLog.e(TAG, "readShort fail, field="+field.getName()+",reason=" + e);
	    }
	}
    }

    private void readShortArray(Object object,Field field) throws IOException, IllegalArgumentException, IllegalAccessException {
	JsonToken token = mReader.peek();
	if (token != JsonToken.BEGIN_ARRAY){
	    field.set(object, null);
	    mReader.skipValue();
	    return ;
	}
	List<Short> shorts = new ArrayList<Short>();
	mReader.beginArray();
	while(mReader.hasNext()){
	    token = mReader.peek();
	    if (token != JsonToken.STRING && token != JsonToken.NUMBER) {
		mReader.skipValue();
	    } else {
		try {
		    shorts.add((short) mReader.nextInt());
		} catch (Exception e) { // 防止因数据格式错误引起后继数据读取也失败的问题，遇数据格式错时，不对此字段进行赋值处理
		    mReader.skipValue();
//		    AspLog.e(TAG, "readShortArray fail,field="+field.getName()+",reason=" + e);
		}
	    }
	}
	mReader.endArray();
	if (shorts.size() == 0) {
	    field.set(object, null);
	    return;
	}
	Short[] vals = new Short[shorts.size()];
	shorts.toArray(vals);
	if (Short[].class.equals(field.getType())) {
	    field.set(object, vals);
	} else {
	    short[] vals2 = new short[shorts.size()];
	    for (int k = 0; k < vals2.length; k++){
		vals2[k] = vals[k];
	    }	    
	    field.set(object, vals2);
	}
    }
    
    private void readLong(Object object, Field field)throws IOException, IllegalArgumentException, IllegalAccessException {
	JsonToken token = mReader.peek();
	if (token != JsonToken.STRING && token != JsonToken.NUMBER) {
	    mReader.skipValue();
	    if (Long.class.equals(field.getType())) {
		field.set(object, null);
	    }
	} else {
	    try {
		long val = mReader.nextLong();
		if (Long.class.equals(field.getType())) {
		    field.set(object, Long.valueOf(val));
		} else {
		    field.setLong(object, val);
		}
	    } catch (Exception e) { // 防止因数据格式错误引起后继数据读取也失败的问题，遇数据格式错时，不对此字段进行赋值处理
		mReader.skipValue();
//		AspLog.e(TAG, "readLong fail, field=" + field.getName() + ",reason=" + e);
	    }
	}
    }
    
    private void readLongArray(Object object,Field field) throws IOException, IllegalArgumentException, IllegalAccessException {
	JsonToken token = mReader.peek();
	if (token != JsonToken.BEGIN_ARRAY){
	    field.set(object, null);
	    mReader.skipValue();
	    return ;
	}
	List<Long> longs = new ArrayList<Long>();
	mReader.beginArray();
	while(mReader.hasNext()){
	    token = mReader.peek();
	    if (token != JsonToken.STRING && token != JsonToken.NUMBER) {
		mReader.skipValue();
	    } else {
		try {
		    longs.add(mReader.nextLong());
		} catch (Exception e) { // 防止因数据格式错误引起后继数据读取也失败的问题，遇数据格式错时，不对此字段进行赋值处理
		    mReader.skipValue();
//		    AspLog.e(TAG, "readLongArray fail, field=" + field.getName() + ",reason=" + e);
		}
	    }
	}
	mReader.endArray();
	if (longs.size() == 0) {
	    field.set(object, null);
	    return;
	}
	Long[] vals = new Long[longs.size()];
	longs.toArray(vals);
	if (Long[].class.equals(field.getType())) {
	    field.set(object, vals);
	} else {
	    long[] vals2 = new long[longs.size()];
	    for (int k = 0; k < vals2.length; k++){
		vals2[k] = vals[k];
	    }	    
	    field.set(object, vals2);
	}
    }

    
    private void readDouble(Object object, Field field)throws IOException, IllegalArgumentException, IllegalAccessException {
	JsonToken token = mReader.peek();
	if (token != JsonToken.STRING && token != JsonToken.NUMBER) {
	    mReader.skipValue();
	    if (Double.class.equals(field.getType())) {
		field.set(object, null);
	    }
	} else {
	    try {
		double val = mReader.nextDouble();
		if (Double.class.equals(field.getType())) {
		    field.set(object, val);
		} else {
		    field.setDouble(object, val);
		}
	    } catch (Exception e) { // 防止因数据格式错误引起后继数据读取也失败的问题，遇数据格式错时，不对此字段进行赋值处理
		mReader.skipValue();
//		AspLog.e(TAG, "readDouble fail, field=" + field.getName() + ",reason=" + e);
	    }
	}
    }
    
    private void readDoubleArray(Object object,Field field) throws IOException, IllegalArgumentException, IllegalAccessException {
	JsonToken token = mReader.peek();
	if (token != JsonToken.BEGIN_ARRAY){
	    field.set(object, null);
	    mReader.skipValue();
	    return ;
	}
	List<Double> doubles = new ArrayList<Double>();
	mReader.beginArray();
	while(mReader.hasNext()){
	    token = mReader.peek();
	    if (token != JsonToken.STRING && token != JsonToken.NUMBER) {
		mReader.skipValue();
	    } else {
		try {
		    doubles.add(mReader.nextDouble());
		} catch (Exception e) { // 防止因数据格式错误引起后继数据读取也失败的问题，遇数据格式错时，不对此字段进行赋值处理
		    mReader.skipValue();
//		    AspLog.e(TAG, "readDoubleArray fail, field=" + field.getName() + ",reason=" + e);
		}
	    } 
	}
	mReader.endArray();
	if (doubles.size() == 0) {
	    field.set(object, null);
	    return;
	}
	Double[] vals = new Double[doubles.size()];
	doubles.toArray(vals);
	if (Double[].class.equals(field.getType())) {
	    field.set(object, vals);
	} else {
	    double[] vals2 = new double[doubles.size()];
	    for (int k = 0; k < vals2.length; k++){
		vals2[k] = vals[k];
	    }
	    field.set(object, vals2);
	}
    }
    
    private void readFloat(Object object, Field field)throws IOException, IllegalArgumentException, IllegalAccessException {
	JsonToken token = mReader.peek();
	if (token != JsonToken.STRING && token != JsonToken.NUMBER) {
	    mReader.skipValue();
	    if (Float.class.equals(field.getType())) {
		field.set(object, null);
	    }
	} else {
	    try {
		float val = (float) mReader.nextDouble();
		if (Float.class.equals(field.getType())) {
		    field.set(object, val);
		} else {
		    field.setFloat(object, val);
		}
	    } catch (Exception e) { // 防止因数据格式错误引起后继数据读取也失败的问题，遇数据格式错时，不对此字段进行赋值处理
		mReader.skipValue();
//		AspLog.e(TAG, "readFloat fail, field=" + field.getName() + ",reason=" + e);
	    }
	}
    }
    
    private void readFloatArray(Object object,Field field) throws IOException, IllegalArgumentException, IllegalAccessException {
	JsonToken token = mReader.peek();
	if (token != JsonToken.BEGIN_ARRAY){
	    field.set(object, null);
	    mReader.skipValue();
	    return ;
	}
	
	List<Float> floats = new ArrayList<Float>();
	mReader.beginArray();
	while(mReader.hasNext()){
	    token = mReader.peek();
	    if (token != JsonToken.STRING && token != JsonToken.NUMBER) {
		mReader.skipValue();
	    } else {
		try {
		    floats.add((float) mReader.nextDouble());
		} catch (Exception e) { // 防止因数据格式错误引起后继数据读取也失败的问题，遇数据格式错时，不对此字段进行赋值处理
		    mReader.skipValue();
//		    AspLog.e(TAG, "readFloatArray fail, field=" + field.getName() + ",reason=" + e);
		}
	    } 
	}
	mReader.endArray();
	if (floats.size() == 0) {
	    field.set(object, null);
	    return;
	}
	Float[] vals = new Float[floats.size()];
	floats.toArray(vals);
	if (Float[].class.equals(field.getType())) {
	    field.set(object, vals);
	} else {
	    float[] vals2 = new float[floats.size()];
	    for (int k = 0; k < vals2.length; k++){
		vals2[k] = vals[k];
	    }	    
	    field.set(object, vals2);
	}
    }
    
    private void readBoolean(Object object, Field field)throws IOException, IllegalArgumentException, IllegalAccessException {
	JsonToken token = mReader.peek();
	if (token != JsonToken.BOOLEAN) {
	    mReader.skipValue();
	    if (Boolean.class.equals(field.getType())) {
		field.set(object, null);
	    }
	} else {
	    try {
		boolean val = mReader.nextBoolean();
		if (Boolean.class.equals(field.getType())) {
		    field.set(object, val);
		} else {
		    field.setBoolean(object, val);
		}
	    } catch (Exception e) { // 防止因数据格式错误引起后继数据读取也失败的问题，遇数据格式错时，不对此字段进行赋值处理
		mReader.skipValue();
//		AspLog.e(TAG, "readBoolean fail, field=" + field.getName() + ",reason=" + e);
	    }
	}
    }

    private void readBooleanArray(Object object,Field field) throws IOException, IllegalArgumentException, IllegalAccessException {
	JsonToken token = mReader.peek();
	if (token != JsonToken.BEGIN_ARRAY){
	    field.set(object, null);
	    mReader.skipValue();
	    return ;
	}

	List<Boolean> booleans = new ArrayList<Boolean>();
	mReader.beginArray();
	while(mReader.hasNext()){
	    token = mReader.peek();
	    if (token != JsonToken.BOOLEAN) {
		mReader.skipValue();
	    } else {
		try {
		    booleans.add(mReader.nextBoolean());
		} catch (Exception e) { // 防止因数据格式错误引起后继数据读取也失败的问题，遇数据格式错时，不对此字段进行赋值处理
		    mReader.skipValue();
//		    AspLog.e(TAG, "readBooleanArray fail, field=" + field.getName() + ",reason=" + e);
		}
	    } 
	}
	mReader.endArray();
	if (booleans.size() == 0) {
	    field.set(object, null);
	    return;
	}
	Boolean[] vals = new Boolean[booleans.size()];
	booleans.toArray(vals);
	if (Boolean[].class.equals(field.getType())) {
	    field.set(object, vals);
	} else {
	    boolean[] vals2 = new boolean[booleans.size()];
	    for (int k = 0; k < vals2.length; k++){
		vals2[k] = vals[k];
	    }	    
	    field.set(object, vals2);
	}
    }

    
    private void readByte(Object object, Field field)throws IOException, IllegalArgumentException, IllegalAccessException {
	JsonToken token = mReader.peek();
	if (token != JsonToken.STRING && token != JsonToken.NUMBER) {
	    mReader.skipValue();
	    if (Byte.class.equals(field.getType())) {
		field.set(object, null);
	    }
	} else {
	    try {
		byte val = (byte) mReader.nextInt();
		if (Byte.class.equals(field.getType())) {
		    field.set(object, val);
		} else {
		    field.setByte(object, val);
		}
	    } catch (Exception e) { // 防止因数据格式错误引起后继数据读取也失败的问题，遇数据格式错时，不对此字段进行赋值处理
		mReader.skipValue();
//		AspLog.e(TAG, "readByte fail, field=" + field.getName() + ",reason=" + e);
	    }
	}
    }
    
    private void readByteArray(Object object,Field field) throws IOException, IllegalArgumentException, IllegalAccessException {
	JsonToken token = mReader.peek();
	if (token != JsonToken.BEGIN_ARRAY){
	    field.set(object, null);
	    mReader.skipValue();
	    return ;
	}

	List<Byte> bytes = new ArrayList<Byte>();
	mReader.beginArray();
	while(mReader.hasNext()){
	    token = mReader.peek();
	    if (token != JsonToken.STRING && token != JsonToken.NUMBER) {
		mReader.skipValue();
	    } else {
		try {
		    bytes.add((byte) mReader.nextInt());
		} catch (Exception e) { // 防止因数据格式错误引起后继数据读取也失败的问题，遇数据格式错时，不对此字段进行赋值处理
		    mReader.skipValue();
//		    AspLog.e(TAG, "readByteArray fail, field=" + field.getName() + ",reason=" + e);
		}
	    } 
	}
	mReader.endArray();
	if (bytes.size() == 0){
	    field.set(object, null);
	    return ;
	}
	Byte [] vals = new Byte[bytes.size()];
	bytes.toArray(vals);
	if (Byte[].class.equals(field.getType())){
	    field.set(object, vals);
	}else{
	    byte [] vals2 = new byte[bytes.size()];
	    for (int k = 0; k < vals2.length; k++){
		vals2[k] = vals[k];
	    }	    
	    field.set(object, vals2);
	}
    }
    
    private void readString(Object object, Field field)throws IOException, IllegalArgumentException, IllegalAccessException {
	JsonToken token = mReader.peek();
	if (token != JsonToken.STRING && token != JsonToken.NUMBER) {
	    mReader.skipValue();
	    field.set(object, null);
	} else {
	    try {
		String val = mReader.nextString();
		if (val != null && val.length() == 4 && "null".equalsIgnoreCase(val)){//鉴于MM后台经常把空数据写成"null"，特将此字符串处理成空指针。
		    val = "";
		}
		field.set(object, val);
	    } catch (Exception e) { // 防止因数据格式错误引起后继数据读取也失败的问题，遇数据格式错时，不对此字段进行赋值处理
		mReader.skipValue();
//		AspLog.e(TAG, "readString fail, field=" + field.getName() + ",reason=" + e);
	    }
	}
    }

    private void readStringArray(Object object,Field field) throws IOException, IllegalArgumentException, IllegalAccessException {
	JsonToken token = mReader.peek();
	if (token != JsonToken.BEGIN_ARRAY){
	    field.set(object, null);
	    mReader.skipValue();
	    return ;
	}

	List<String> strings = new ArrayList<String>();
	String val ;
	mReader.beginArray();
	while(mReader.hasNext()){
	    token = mReader.peek();
	    if (token != JsonToken.STRING && token != JsonToken.NUMBER) {
		mReader.skipValue();
	    } else {
		try {
		    val = mReader.nextString();
		    if (val != null) {
			strings.add(val);
		    }
		} catch (Exception e) { // 防止因数据格式错误引起后继数据读取也失败的问题，遇数据格式错时，不对此字段进行赋值处理
		    mReader.skipValue();
//		    AspLog.e(TAG, "readStringArray fail, field=" + field.getName() + ",reason=" + e);
		}
	    }
	}
	mReader.endArray();
	if (strings.size() == 0){
	    field.set(object, null);
	    return ;
	}
	String [] vals = new String[strings.size()];
	strings.toArray(vals);
	field.set(object, vals);
    }

    private void readObject(Object object, Field field){
	try {
	    if (mReader.peek() != JsonToken.BEGIN_OBJECT) {
		field.set(object, null);
		mReader.skipValue();
		return;
	    }
	    Object fieldobj = field.get(object);
	    if (fieldobj == null) {
		Class<?> classtype = field.getType();
//		String classname = getClassName(classtype);
//		AspLog.i(TAG, "readObj class=" + classname);
		fieldobj = ReflectHelper.newInstance(classtype);
		if (fieldobj != null){
		    readObject(fieldobj);
		    field.set(object, fieldobj);
		}else{
		    skipReadObject();
		}
	    } else {
		readObject(fieldobj);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void readObjectArray(Object object,Field field) throws IOException, IllegalArgumentException, IllegalAccessException {
	JsonToken token = mReader.peek();
	if (token != JsonToken.BEGIN_ARRAY) {
	    mReader.skipValue();
	    field.set(object, null);
	    return;
	}
	List<Object> objects = new ArrayList<Object>();
	Object fieldobj;
	Class<?> classtype = field.getType();
	Class<?> comptype = classtype.getComponentType();
//	AspLog.i(TAG, "readObjectArray classname=" + classtype.getName() + ",canname=" + classtype.getCanonicalName()
//		+ ",name=" + getClassName(classtype));
	mReader.beginArray();
	try {
//	    String classname = getClassName(classtype);
	    while (mReader.hasNext()) {
		token = mReader.peek();
		if (token != JsonToken.BEGIN_OBJECT) {
		    mReader.skipValue();
		} else {
		    fieldobj = ReflectHelper.newInstance(comptype);
		    if (fieldobj != null){
			readObject(fieldobj);
			objects.add(fieldobj);
		    }else{
			skipReadObject();
		    }
		}
	    }
	    if (objects.size() == 0) {
		field.set(object, null);
		return;
	    }
	    Object[] vals = (Object[]) ReflectHelper.newInstance(comptype, objects.size());
	    objects.toArray(vals);
	    field.set(object, vals);
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    mReader.endArray();
	}
    }
    
    private void readObjectList(Object object, Field field) throws IOException, IllegalArgumentException,Exception,
	    IllegalAccessException {
	if (mReader.peek() == JsonToken.NULL) {
	    mReader.skipValue();
	    field.set(object, null);
	    return;
	}
	List<Object> objects = new ArrayList<Object>();
	Object fieldobj;
	Type type = field.getGenericType();
	boolean result = true;
	if (type instanceof ParameterizedType) {
	    ParameterizedType parameterizedType = (ParameterizedType) type;
	    Type[] types = parameterizedType.getActualTypeArguments();
	    if (types != null && types.length == 1) {
		mReader.beginArray();
		try {
//		    String classname = ((Class<?>) types[0]).getName();
		    Class<?> comptype = (Class<?>)types[0];
		    while (mReader.hasNext()) {

			fieldobj = ReflectHelper.newInstance(comptype);
			if (fieldobj != null){
			    readObject(fieldobj);
			    objects.add(fieldobj);
			}else{
			    skipReadObject();
			}
		    }
		    if (objects.size() == 0) {
			field.set(object, null);
			return;
		    }
		    field.set(object, objects);
		} finally {
		    mReader.endArray();
		}
	    } else {
		result = false;
	    }
	} else {
	    result = false;
	}
	if (!result) {
	    mReader.skipValue();
	    field.set(object, null);
	    return;
	}

    }
    
//    private String getClassName(Class<?> clazz){
//	String name = clazz.getName();
//	int firstindex = name.indexOf("[L");
//	int endindex = name.indexOf(';');
//	if (endindex < 0)
//	    endindex = name.length();
//	if (firstindex < 0){
//	    firstindex = 0;
//	}else{
//	    firstindex += 2;
//	}
//	return name.substring(firstindex, endindex);
//    }
}
