package com.android.json.stream;


import com.support.loader.utils.ReflectHelper;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class JsonObjectWriter {
    private String		TAG = "";
    private JsonWriter mJsonWriter;
    public JsonObjectWriter(OutputStream os){
	TAG = getClass().getSimpleName();
	try {
	    mJsonWriter = new JsonWriter(new OutputStreamWriter(os,"UTF-8"));
	} catch (UnsupportedEncodingException e) {
	    mJsonWriter = new JsonWriter(new OutputStreamWriter(os));
	}
	mJsonWriter.setIndent("  ");
    }
    
    public void close(){
	if (mJsonWriter != null){
	    try {
		mJsonWriter.close();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }
    
    public static String writeObjectAsString (Object object) throws IllegalArgumentException, IOException, IllegalAccessException{
	ByteArrayOutputStream bos = new ByteArrayOutputStream(64);
	JsonObjectWriter jow = new JsonObjectWriter(bos);
	jow.writeObject(object);
	jow.close();
	String jsdata = bos.toString("UTF-8");
	bos.close();
	return jsdata;
    }
    
    public void writeObject(Object object) throws IOException, IllegalArgumentException, IllegalAccessException {
	final Class<?> objclazz = object.getClass();
	Class<?> fieldclazz = null;
	mJsonWriter.beginObject();
	try {
	    Field[] fields = null ;
	    if (ReflectHelper.methodSupported(object, "getOrderedFields", null)){
		fields = (Field[])ReflectHelper.callDeclaredMethod(object, "getOrderedFields",
			null, null);
	    }else{
		fields = objclazz.getDeclaredFields();
	    }
	    if (fields != null) {
		for (Field field : fields) {
		    if(field == null) {
			continue;
		    }
		    if (!Modifier.isStatic(field.getModifiers())) {
			field.setAccessible(true);
			fieldclazz = field.getType();
			Object fieldobj = field.get(object);
			if (fieldobj == null){ //Michael Liu:空字段不输出，可以节省流量！调试需要时，可以恢复这两句
			    mJsonWriter.name(field.getName());
			    mJsonWriter.nullValue();
			}else{
			    writeFieldObject(object, field, fieldclazz);
			}
		    }else{ //不写静态变量
//			AspLog.i(TAG, "writeObject field="+field.getName() +",is static?");
		    }
		}
	    }
	} finally {
	    mJsonWriter.endObject();
	}
    }
    private void writeFieldObject (Object object,Field field,  Class<?> fieldclazz) throws IllegalArgumentException, IOException, IllegalAccessException{
	Integer classtype = JsonDataType.get(fieldclazz);
//	if (AspLog.isPrintLog){
//	    AspLog.i(TAG, "writeFieldObject fieldclazz="+fieldclazz +",classtype="+classtype +",name="+field.getName());
//	}
	if (classtype == null){ //没找到指定的读方法
//	    Object fieldval = field.get(object);
//	    if (fieldval != null && fieldval instanceof List){ //暂不支持List类型
//		Object [] vals = ((List)fieldval).toArray();
//		mJsonWriter.name(field.getName());
//		if (vals == null || vals.length == 0){
//		    mJsonWriter.nullValue();
//		}else{
//		    mJsonWriter.beginArray();
//		    try{
//			
//		    }finally{
//			mJsonWriter.endArray();
//		    }
//		}
//	    }else 
	    if (fieldclazz.isArray()){
		writeObjectArray(object, field); //Object array
	    }else{
		writeObject(object, field); //Object
	    }
	    return ;
	}
	switch (classtype) {
	case JsonDataType.TYPE_SHORT:
	    if (fieldclazz.isArray())
		writeShortArray(object, field); // int array
	    else
		writeShort(object, field); // int
	    break;
	case JsonDataType.TYPE_CHAR:
	    if (fieldclazz.isArray())
		writeCharArray(object, field); // char array
	    else
		writeChar(object, field); // char
	    break;
	case JsonDataType.TYPE_INT:
	    if (fieldclazz.isArray())
		writeIntArray(object, field); // int array
	    else
		writeInt(object, field); // int
	    break;
	case JsonDataType.TYPE_LONG:
	    if (fieldclazz.isArray())
		writeLongArray(object, field); // int array
	    else
		writeLong(object, field); // int
	    break;
	case JsonDataType.TYPE_DOUBLE:
	    if (fieldclazz.isArray())
		writeDoubleArray(object, field); // double array
	    else
		writeDouble(object, field); // double
	    break;
	case JsonDataType.TYPE_FLOAT:
	    if (fieldclazz.isArray())
		writeFloatArray(object, field); // double array
	    else
		writeFloat(object, field); // double
	    break;
	    
	case JsonDataType.TYPE_BOOLEAN:
//	    if (AspLog.isPrintLog)
//		AspLog.i(TAG, "writeBoolean type="+classtype);
	    if (fieldclazz.isArray())
		writeBooleanArray(object, field); // boolean array
	    else
		writeBoolean(object, field); // boolean
	    break;
	case JsonDataType.TYPE_BYTE:
	    if (fieldclazz.isArray())
		writeByteArray(object, field); // byte array
	    else
		writeByte(object, field); // byte
	    break;
	case JsonDataType.TYPE_STRING:
	    if (fieldclazz.isArray()){
		writeStringArray(object, field); // String array
	    }else{
		writeString(object, field); // String
	    }
	    break;
	default:
	    break;
	}
    }
    
    
    private void writeShort(Object object,Field field) throws IOException, IllegalArgumentException, IllegalAccessException {
	Short val = (Short)field.get(object);
	mJsonWriter.name(field.getName());
	if (val != null)
	    mJsonWriter.value(val.intValue());
	else
	    mJsonWriter.nullValue();
    }
    

    private void writeShortArray(Object object,Field field) throws IOException, IllegalArgumentException, IllegalAccessException {
	mJsonWriter.name(field.getName());
	mJsonWriter.beginArray();
	try {
	    if (Short[].class.equals(field.getType())) {
		Short[] vals = (Short[]) field.get(object);
		if (vals == null || vals.length == 0) {
		    mJsonWriter.nullValue();
		} else {
		    for (Short v : vals) {
			if (v != null)
			    mJsonWriter.value(v.longValue());
			else
			    mJsonWriter.nullValue();
		    }
		}
	    } else {
		short[] vals = (short[]) field.get(object);
		if (vals == null || vals.length == 0) {
		    mJsonWriter.nullValue();
		} else {
		    for (short v : vals) {
			mJsonWriter.value(v);
		    }
		}
	    }
	} finally {
	    mJsonWriter.endArray();
	}
    }
    
    private void writeChar(Object object,Field field) throws IOException, 
    IllegalArgumentException, IllegalAccessException {
	Character val = (Character)field.get(object);
	mJsonWriter.name(field.getName());
	if (val != null){
	    mJsonWriter.value(val.toString());
	}else{
	    mJsonWriter.nullValue() ;
	}
    }
    

    private void writeCharArray(Object object,Field field) throws IOException, 
    IllegalArgumentException, IllegalAccessException {
	mJsonWriter.name(field.getName());
	mJsonWriter.beginArray();
	try {
	    if (Character[].class.equals(field.getType())) {
		Character[] vals = (Character[]) field.get(object);
		if (vals == null || vals.length == 0) {
		    mJsonWriter.nullValue();
		} else {
		    for (Character v : vals) {
			if (v != null)
			    mJsonWriter.value(v.toString());
			else
			    mJsonWriter.nullValue();
		    }
		}
	    } else {
		char[] vals = (char[]) field.get(object);
		if (vals == null || vals.length == 0) {
		    mJsonWriter.nullValue();
		} else {
		    for (char v : vals) {
			mJsonWriter.value(String.valueOf(v));
		    }
		}
	    }
	} finally {
	    mJsonWriter.endArray();
	}
    }
    
    private void writeInt(Object object,Field field) throws IOException, IllegalArgumentException, IllegalAccessException {
	Integer val = (Integer)field.get(object);
	mJsonWriter.name(field.getName());
	if (val != null){
	    mJsonWriter.value(val.intValue());
	}else{
	    mJsonWriter.nullValue();
	}
    }
    

    private void writeIntArray(Object object,Field field) throws IOException, IllegalArgumentException, IllegalAccessException {
	mJsonWriter.name(field.getName());
	mJsonWriter.beginArray();
	try{
	    if (Integer[].class.equals(field.getType())) {
		Integer[] vals = (Integer[]) field.get(object);
		if (vals == null || vals.length == 0) {
		    mJsonWriter.nullValue();
		} else {
		    for (Integer v : vals) {
			if (v != null)
			    mJsonWriter.value(v.longValue());
			else
			    mJsonWriter.nullValue();
		    }
		}
	    } else {
		int[] vals = (int[]) field.get(object);
		if (vals == null || vals.length == 0) {
		    mJsonWriter.nullValue();
		} else {
		    for (int v : vals) {
			mJsonWriter.value(v);
		    }
		}
	    }
	}finally{
	    mJsonWriter.endArray();
	}
    }
    
    private void writeLong(Object object,Field field) throws IOException, IllegalArgumentException, IllegalAccessException {
	Long val = (Long)field.get(object);
	mJsonWriter.name(field.getName());
	if (val != null){
	    mJsonWriter.value(val.longValue());
	}else{
	    mJsonWriter.nullValue() ;
	}
    }
    

    private void writeLongArray(Object object,Field field) throws IOException, IllegalArgumentException, IllegalAccessException {
	mJsonWriter.name(field.getName());
	mJsonWriter.beginArray();
	try{
	    if (Long[].class.equals(field.getType())) {
		Long[] vals = (Long[]) field.get(object);
		if (vals == null || vals.length == 0) {
		    mJsonWriter.nullValue();
		} else {
		    for (Long v : vals) {
			if (v != null)
			    mJsonWriter.value(v.longValue());
			else
			    mJsonWriter.nullValue();
		    }
		}
	    } else {
		long[] vals = (long[]) field.get(object);
		if (vals == null || vals.length == 0) {
		    mJsonWriter.nullValue();
		} else {
		    for (long v : vals) {
			mJsonWriter.value(v);
		    }
		}
	    }
	}finally{
	    mJsonWriter.endArray();
	}
    }

    private void writeDouble(Object object, Field field)throws IOException, IllegalArgumentException, IllegalAccessException {
	Double val = (Double)field.get(object);
	mJsonWriter.name(field.getName());
	if (val != null){
	    mJsonWriter.value(val.doubleValue());
	}else{
	    mJsonWriter.nullValue();
	}
    }
    
    private void writeDoubleArray(Object object,Field field) throws IOException, IllegalArgumentException, IllegalAccessException {
	mJsonWriter.name(field.getName());
	mJsonWriter.beginArray();
	try{
	    if (Double[].class.equals(field.getType())) {
		Double[] vals = (Double[]) field.get(object);
		if (vals == null || vals.length == 0) {
		    mJsonWriter.nullValue();
		} else {
		    for (Double v : vals) {
			if (v != null)
			    mJsonWriter.value(v.doubleValue());
			else
			    mJsonWriter.nullValue();
		    }
		}
	    } else {
		double[] vals = (double[]) field.get(object);
		if (vals == null || vals.length == 0) {
		    mJsonWriter.nullValue();
		} else {
		    for (double v : vals) {
			mJsonWriter.value(v);
		    }
		}
	    }
	}finally{
	    mJsonWriter.endArray();
	}
    }

    private void writeFloat(Object object, Field field)throws IOException, IllegalArgumentException, IllegalAccessException {
	Float val = (Float)field.get(object);
	mJsonWriter.name(field.getName());
	if (val != null)
	    mJsonWriter.value(val.floatValue());
	else
	    mJsonWriter.nullValue() ;
    }
    
    private void writeFloatArray(Object object,Field field) throws IOException, IllegalArgumentException, IllegalAccessException {
	mJsonWriter.name(field.getName());
	mJsonWriter.beginArray();
	try {
	    if (Float[].class.equals(field.getType())) {
		Float[] vals = (Float[]) field.get(object);
		if (vals == null || vals.length == 0) {
		    mJsonWriter.nullValue();
		} else {
		    for (Float v : vals) {
			if (v != null)
			    mJsonWriter.value(v.doubleValue());
			else
			    mJsonWriter.nullValue();
		    }
		}
	    } else {
		float[] vals = (float[]) field.get(object);
		if (vals == null || vals.length == 0) {
		    mJsonWriter.nullValue();
		} else {
		    for (float v : vals) {
			mJsonWriter.value(v);
		    }
		}
	    }
	} finally {
	    mJsonWriter.endArray();
	}
    }
    
    
    private void writeBoolean(Object object, Field field)throws IOException, IllegalArgumentException, IllegalAccessException {
	Boolean val = (Boolean)field.get(object);
	mJsonWriter.name(field.getName());
	if(val != null)
	    mJsonWriter.value(val.booleanValue());
	else
	    mJsonWriter.nullValue();
    }

    private void writeBooleanArray(Object object,Field field) throws IOException, IllegalArgumentException, IllegalAccessException {
	mJsonWriter.name(field.getName());
	Object vals = field.get(object);
	if (vals == null ){
	    mJsonWriter.nullValue();
	    return ;
	}
	mJsonWriter.beginArray();
	try{
	    if(Boolean[].class.equals(field.getType())){
		Boolean [] bvals = (Boolean [])vals;
		if (bvals == null || bvals.length == 0) {
		    mJsonWriter.nullValue();
		} else {
		    for (Boolean v : bvals) {
			if (v != null)
			    mJsonWriter.value(v.booleanValue());
			else
			    mJsonWriter.nullValue();
		    }
		}
	    }else{
		boolean [] bvals = (boolean [])vals;
		if (bvals == null || bvals.length == 0) {
		    mJsonWriter.nullValue();
		} else {
		    for (boolean v : bvals) {
			mJsonWriter.value(v);
		    }
		}
	    }
	}finally{
	    mJsonWriter.endArray();
	}
    }

    
    private void writeByte(Object object, Field field)throws IOException, IllegalArgumentException, IllegalAccessException {
	Byte val = (Byte)field.get(object);
	mJsonWriter.name(field.getName());
	if (val != null)
	    mJsonWriter.value(val.intValue());
	else
	    mJsonWriter.nullValue();
    }
    
    private void writeByteArray(Object object,Field field) throws IOException, IllegalArgumentException, IllegalAccessException {
	mJsonWriter.name(field.getName());
//	if (AspLog.isPrintLog){
//	    AspLog.i(TAG, "writeByteArray, object = " + object + ", field = " + field.getName());
//	}
	mJsonWriter.beginArray();
	try{
	    if (Byte[].class.equals(field.getType())) {
		Byte[] vals = (Byte[]) field.get(object);
		if (vals == null ||  vals.length == 0) {
		    mJsonWriter.nullValue();
		} else {
		    for (Byte v : vals) {
			if (v != null)
			    mJsonWriter.value(v.longValue());
			else
			    mJsonWriter.nullValue();
		    }
		}
	    } else {
		byte[] vals = (byte[]) field.get(object);
		if (vals == null ||vals.length == 0) {
		    mJsonWriter.nullValue();
		} else {
		    for (byte v : vals) {
			mJsonWriter.value(v);
		    }
		}
	    }	}finally{
	    mJsonWriter.endArray();
	}
    }
    
    private void writeString(Object object, Field field)throws IOException, IllegalArgumentException, IllegalAccessException {
	String val = (String)field.get(object);
	mJsonWriter.name(field.getName());
	if (val != null)
	    mJsonWriter.value(val);
	else
	    mJsonWriter.nullValue() ;
    }

    private void writeStringArray(Object object,Field field) throws IOException, IllegalArgumentException, IllegalAccessException {
	mJsonWriter.name(field.getName());
	String[] vals = (String[])field.get(object);
	if (vals == null || vals.length == 0){
	    mJsonWriter.nullValue();
	    return ;
	}
	mJsonWriter.beginArray();
	try{
	    for (String v:vals){
		if (v != null)
		    mJsonWriter.value(v);
		else
		    mJsonWriter.nullValue();
	    }
	}finally{
	    mJsonWriter.endArray();
	}
    }

    private void writeObject(Object object, Field field) throws IOException{

	Object val = null;
	String name = null;
	try {
	    name = field.getName(); 
	    val = field.get(object);
//	    if (AspLog.isPrintLog){
//		AspLog.i(TAG, "writeObject object = " + object + ", field.getName() = " + name
//			+ ", field.get(object) = " + val);
//	    }

	} catch (Exception e) {
	    e.printStackTrace();
	    return ;
	}
	mJsonWriter.name(name);
	try {
	    if (val != null){
		writeObject(val);
	    }else{
		mJsonWriter.nullValue() ;
	    }
	} catch (IllegalArgumentException e) {
	    e.printStackTrace();
	} catch (IllegalAccessException e) {
	    e.printStackTrace();
	}
    }

    private void writeObjectArray(Object object,Field field) throws IOException, IllegalArgumentException, IllegalAccessException {
	mJsonWriter.name(field.getName());
	Object[] vals = (Object[])field.get(object);
	if (vals == null || vals.length == 0){
	    mJsonWriter.nullValue();
	    return ;
	}
	mJsonWriter.beginArray();
	try{
	    for (Object v:vals){
		if (v != null)
		    writeObject(v);
		else
		    mJsonWriter.nullValue();
	    }
	}finally{
	    mJsonWriter.endArray();
	}    
    }
    
}
