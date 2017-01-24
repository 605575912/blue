package com.android.json.stream;

import java.util.HashMap;
import java.util.Map;

public class JsonDataType {
    private static	Map<Class<?>, Integer>	gClassTypeMap;
    public final static int	
    				TYPE_BYTE = 1,
    				TYPE_CHAR = 2,
    				TYPE_INT  = 3,
    				TYPE_SHORT = 4,
    				TYPE_LONG = 5,
    				TYPE_FLOAT = 6,
    				TYPE_DOUBLE = 7,
    				TYPE_BOOLEAN = 8,
    				TYPE_STRING = 9;
    
//                                TYPE_BYTES = 101,
//                            	TYPE_CHARS = 102,
//                            	TYPE_INTS  = 103,
//                            	TYPE_SHORTS = 104,
//                            	TYPE_LONGS = 105,
//                            	TYPE_FLOATS = 106,
//                            	TYPE_DOUBLES = 107,
//                            	TYPE_BOOLEANS = 108,
//                            	TYPE_STRINGS = 109,
//                            	
//                                TYPE_BYTE_C = 201,
//                            	TYPE_CHAR_C = 202,
//                            	TYPE_INT_C  = 203,
//                            	TYPE_SHORT_C = 204,
//                            	TYPE_LONG_C = 205,
//                            	TYPE_FLOAT_C = 206,
//                            	TYPE_DOUBLE_C = 207,
//                            	TYPE_BOOLEAN_C = 208,
//                            	
//                                TYPE_BYTE_CS = 301,
//                            	TYPE_CHAR_CS = 302,
//                            	TYPE_INT_CS  = 303,
//                            	TYPE_SHORT_CS = 304,
//                            	TYPE_LONG_CS = 305,
//                            	TYPE_FLOAT_CS = 306,
//                            	TYPE_DOUBLE_CS = 307,
//                            	TYPE_BOOLEAN_CS = 308;
    
    public static Integer get(Class<?> classtype){
	synchronized (JsonDataType.class){
	    if (gClassTypeMap == null){
		gClassTypeMap = new HashMap<Class<?>,Integer>();
		gClassTypeMap.put(byte.class, TYPE_BYTE);
		gClassTypeMap.put(short.class, TYPE_SHORT);
		gClassTypeMap.put(char.class, TYPE_CHAR);
		gClassTypeMap.put(int.class, TYPE_INT);
		gClassTypeMap.put(long.class, TYPE_LONG);
		gClassTypeMap.put(float.class, TYPE_FLOAT);
		gClassTypeMap.put(double.class, TYPE_DOUBLE);
		gClassTypeMap.put(boolean.class, TYPE_BOOLEAN);
		gClassTypeMap.put(String.class, TYPE_STRING);
		
		gClassTypeMap.put(byte[].class, TYPE_BYTE);
		gClassTypeMap.put(char[].class, TYPE_CHAR);
		gClassTypeMap.put(int[].class, TYPE_INT);
		gClassTypeMap.put(short[].class, TYPE_SHORT);
		gClassTypeMap.put(long[].class, TYPE_LONG);
		gClassTypeMap.put(float[].class, TYPE_FLOAT);
		gClassTypeMap.put(double[].class, TYPE_DOUBLE);
		gClassTypeMap.put(boolean[].class, TYPE_BOOLEAN);
		gClassTypeMap.put(String[].class, TYPE_STRING);
		
		gClassTypeMap.put(Byte.class, TYPE_BYTE);
		gClassTypeMap.put(Integer.class, TYPE_INT);
		gClassTypeMap.put(Character.class, TYPE_CHAR);
		gClassTypeMap.put(Short.class, TYPE_SHORT);
		gClassTypeMap.put(Long.class, TYPE_LONG);		
		gClassTypeMap.put(Float.class, TYPE_FLOAT);		
		gClassTypeMap.put(Double.class, TYPE_DOUBLE);		
		gClassTypeMap.put(Boolean.class, TYPE_BOOLEAN);
				
		gClassTypeMap.put(Byte[].class, TYPE_BYTE);
		gClassTypeMap.put(Integer[].class, TYPE_INT);
		gClassTypeMap.put(Character[].class, TYPE_CHAR);
		gClassTypeMap.put(Short[].class, TYPE_SHORT);		
		gClassTypeMap.put(Long[].class, TYPE_LONG);		
		gClassTypeMap.put(Float[].class, TYPE_FLOAT);		
		gClassTypeMap.put(Double[].class, TYPE_DOUBLE);		
		gClassTypeMap.put(Boolean[].class, TYPE_BOOLEAN);
		
//		gClassTypeMap.put(byte[].class, TYPE_BYTES);
//		gClassTypeMap.put(char[].class, TYPE_CHARS);
//		gClassTypeMap.put(int[].class, TYPE_INTS);
//		gClassTypeMap.put(short[].class, TYPE_SHORTS);
//		gClassTypeMap.put(long[].class, TYPE_LONGS);
//		gClassTypeMap.put(float[].class, TYPE_FLOATS);
//		gClassTypeMap.put(double[].class, TYPE_DOUBLES);
//		gClassTypeMap.put(boolean[].class, TYPE_BOOLEANS);
//		gClassTypeMap.put(String[].class, TYPE_STRINGS);
//		
//		gClassTypeMap.put(Byte.class, TYPE_BYTE_C);
//		gClassTypeMap.put(Integer.class, TYPE_INT_C);
//		gClassTypeMap.put(Character.class, TYPE_CHAR_C);
//		gClassTypeMap.put(Short.class, TYPE_SHORT_C);
//		gClassTypeMap.put(Long.class, TYPE_LONG_C);		
//		gClassTypeMap.put(Float.class, TYPE_FLOAT_C);		
//		gClassTypeMap.put(Double.class, TYPE_DOUBLE_C);		
//		gClassTypeMap.put(Boolean.class, TYPE_BOOLEAN_C);
//				
//		gClassTypeMap.put(Byte[].class, TYPE_BYTE_CS);
//		gClassTypeMap.put(Integer[].class, TYPE_INT_CS);
//		gClassTypeMap.put(Character[].class, TYPE_CHAR_CS);
//		gClassTypeMap.put(Short[].class, TYPE_SHORT_CS);		
//		gClassTypeMap.put(Long[].class, TYPE_LONG_CS);		
//		gClassTypeMap.put(Float[].class, TYPE_FLOAT_CS);		
//		gClassTypeMap.put(Double[].class, TYPE_DOUBLE_CS);		
//		gClassTypeMap.put(Boolean[].class, TYPE_BOOLEAN_CS);
		
	    }
	    return gClassTypeMap.get(classtype);
	}
    }
}
