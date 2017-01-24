package com.android.json.stream;


import com.support.loader.utils.ReflectHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * 这个类用来检测JsonObjectReader与JsonObjectWriter是否正常读也写
 * 
 * @author lhy
 * 
 */
public class JsonObjectVerifier {
    private static final String TAG = "JsonObjectVerifier";

    /**
     * 深度比较两个对象,类名,成员变量完全相等的情况下返回true,否则返回false
     * 
     * @param obj1
     * @param obj2
     * @return
     */
    public static boolean verifyObject(Object obj1, Object obj2) {

	if (obj1 == null && obj2 == null) { // 同时为null
	    // TODO 同时为null,不再继续比较
	    return true;
	}
	if (obj1 == null || obj2 == null) { // 一个为null,一个不为null,直接返回false
	    return false;
	}

	final Class<?> objclazz1 = obj1.getClass();
	final Class<?> objclazz2 = obj2.getClass();
	if (!objclazz1.equals(objclazz2)) {// 类名不相同,直接返回false
//	    AspLog.w(TAG, "Obj1's type=" + objclazz1.getName() + " doestn't equal to Obj2's type="
//		    + objclazz2.getName());
	    return false;
	}

	Field[] fields1 = null;
	Field[] fields2 = null;
	// if (!objclazz1.equals(objclazz2)){
	// AspLog.w(TAG, "Obj1's type="+ objclazz1.getName()
	// +" doestn't equal to Obj2's type="+objclazz2.getName());
	// return true;
	// }
	if (ReflectHelper.methodSupported(obj1, "getOrderedFields", null)) {
	    fields1 = (Field[]) ReflectHelper.callDeclaredMethod(obj1, "getOrderedFields", null, null);
	} else {
	    fields1 = objclazz1.getDeclaredFields();
	}
	if (ReflectHelper.methodSupported(obj2, "getOrderedFields", null)) {
	    fields2 = (Field[]) ReflectHelper.callDeclaredMethod(obj2, "getOrderedFields", null, null);
	} else {
	    fields2 = objclazz2.getDeclaredFields();
	}

	int k = 0;
	boolean flag = false;
	for (k = 0; k < fields1.length; k++) {
	    if(fields1[k] == null){
		continue;
	    }
	    if (!Modifier.isStatic(fields1[k].getModifiers())) {// 只比较非static函数
		try {
		    flag = verifyObjectValue(obj1, fields1[k], obj2, fields2[k]);
		} catch (Exception e) {
		    e.printStackTrace();
		    return false;
		}
		if (!flag) {
		    return false;
		}
	    }
	}
	return true;
    }

    private static boolean verifyObjectValue(Object declaredobj1, Field field1, Object declaredobj2, Field field2)
	    throws IllegalArgumentException, IllegalAccessException {
//	AspLog.i(TAG, "declaredobj1 = " + declaredobj1 + ", Field1 = " + field1 + ", declaredobj2 = " + declaredobj2
//		+ ", Field2 = " + field2);
	final Class<?> obj1clazz = declaredobj1.getClass();
	final Class<?> obj2clazz = declaredobj2.getClass();
	final Object fieldobj1 = field1.get(declaredobj1);
	final Object fieldobj2 = field2.get(declaredobj2);

//	AspLog.w(TAG, obj1clazz.getName() + "'s field=" + field1.getName() + ", fieldobj1 = " + fieldobj1 + ", "
//		+ obj2clazz.getName() + "'s field=" + field2.getName() + ", fieldobj2 = " + fieldobj2);

	if (fieldobj1 == null && fieldobj2 == null) { // 同时为null
	    // TODO 同时为null,不再继续比较
	    return true;
	}
	if (fieldobj1 == null || fieldobj2 == null) { // 一个为null,一个不为null,直接返回false
	    return false;
	}

	final Class<?> fld1clazz = field1.getType();
	final Class<?> fld2clazz = field2.getType();

//	AspLog.w(TAG, "fld1clazz = " + fld1clazz.getName() + ", fld2clazz = " + fld2clazz.getName());

	if ((!fld1clazz.equals(fld2clazz)) || (fld1clazz.isArray() && !fld2clazz.isArray())
		|| (fld2clazz.isArray() && !fld1clazz.isArray())) {// 类名不相同,直接返回false
//	    AspLog.w(TAG, "fld1clazz's type=" + fld1clazz.getName() + " doestn't equal to fld2clazz's type="
//		    + fld2clazz.getName());
	    return false;
	}

	Integer classtype1 = JsonDataType.get(fld1clazz);
	Integer classtype2 = JsonDataType.get(fld2clazz);

	if (classtype1 == null && classtype2 == null) {// TODO 为自定义的Json类型
	    return verifyObject(fieldobj1, fieldobj2);
	}

	if ((classtype1 == null && classtype2 != null) || (classtype1 != null && classtype2 == null)) {// fld1clazz类型和fld2clazz类型不匹配
	    return false;
	}

	if ((fld1clazz.isArray() && !fld2clazz.isArray()) || (!fld1clazz.isArray() && fld2clazz.isArray())) {// fld1clazz类型和fld2clazz类型不匹配
	    return false;
	}

	if (fld1clazz.isArray() && fld2clazz.isArray()) { // 数组类型
	    return compareArray(field1, fieldobj1, fieldobj2, fld1clazz, classtype1);
	} else if (!fld1clazz.isArray() && !fld2clazz.isArray()) {// 非数组类型
	    if (fieldobj1.equals(fieldobj2)) {
		return true;
	    } else {
		return false;
	    }
	} else { // 其他
	    return false;
	}
    }

    public static boolean compareArray(Field field1, Object fieldobj1, Object fieldobj2, Class<?> fld1clazz,
	    Integer classtype1) {

	switch (classtype1) {
	case JsonDataType.TYPE_INT:
	    return compareIntArray(field1, fieldobj1, fieldobj2, fld1clazz);

	case JsonDataType.TYPE_SHORT:
	    return compareShortArray(field1, fieldobj1, fieldobj2, fld1clazz);

	case JsonDataType.TYPE_LONG:
	    return compareLongArray(field1, fieldobj1, fieldobj2, fld1clazz);
	    
	case JsonDataType.TYPE_CHAR:
	    return compareCharArray(field1, fieldobj1, fieldobj2, fld1clazz);

	case JsonDataType.TYPE_DOUBLE:
	    return compareDoubleArray(field1, fieldobj1, fieldobj2, fld1clazz);

	case JsonDataType.TYPE_FLOAT:
	    return compareFloatArray(field1, fieldobj1, fieldobj2, fld1clazz);

	case JsonDataType.TYPE_BOOLEAN:
	    return compareBooleanArray(field1, fieldobj1, fieldobj2, fld1clazz);

	case JsonDataType.TYPE_BYTE:
	    return compareByteArray(field1, fieldobj1, fieldobj2, fld1clazz);

	case JsonDataType.TYPE_STRING:
	    return compareStringArray(field1, fieldobj1, fieldobj2, fld1clazz);

	default:
	    return false;
	}
    }

    private static boolean compareIntArray(Field field, Object fieldobj1, Object fieldobj2, Class<?> fldclazz) {
	if (fldclazz.isArray()) {
	    if (Integer[].class.equals(field.getType())) {
		Integer[] fld1s = (Integer[]) fieldobj1;
		Integer[] fld2s = (Integer[]) fieldobj2;
		boolean flag = true;
		if (fld1s.length == fld2s.length) {
		    for (int i = 0; i < fld1s.length; i++) {
			if (fld1s[i] == fld2s[i] || (fld1s[i] == null && fld2s[i] == null)) {
			    flag = true;
			} else if (fld1s[i].equals(fld2s[i])) {
			    flag = true;
			} else {
			    return false;
			}
		    }
		    return flag;
		} else {
		    return false;
		}
	    } else {
		int[] fld1s = (int[]) fieldobj1;
		int[] fld2s = (int[]) fieldobj2;
		boolean flag = true;
		if (fld1s.length == fld2s.length) {
		    for (int i = 0; i < fld1s.length; i++) {
			if (fld1s[i] == fld2s[i]) {
			    flag = true;
			} else {
			    return false;
			}
		    }
		    return flag;
		} else {
		    return false;
		}
	    }
	} else {
	    return false;
	}
    }

    private static boolean compareByteArray(Field field, Object fieldobj1, Object fieldobj2, Class<?> fldclazz) {
	if (fldclazz.isArray()) {
	    if (Byte[].class.equals(field.getType())) {
		Byte[] fld1s = (Byte[]) fieldobj1;
		Byte[] fld2s = (Byte[]) fieldobj2;
		boolean flag = true;
		if (fld1s.length == fld2s.length) {
		    for (int i = 0; i < fld1s.length; i++) {
			if (fld1s[i] == fld2s[i] || (fld1s[i] == null && fld2s[i] == null)) {
			    flag = true;
			} else if (fld1s[i].equals(fld2s[i])) {
			    flag = true;
			} else {
			    return false;
			}
		    }
		    return flag;
		} else {
		    return false;
		}
	    } else {
		byte[] fld1s = (byte[]) fieldobj1;
		byte[] fld2s = (byte[]) fieldobj2;
		boolean flag = true;
		if (fld1s.length == fld2s.length) {
		    for (int i = 0; i < fld1s.length; i++) {
			if (fld1s[i] == fld2s[i]) {
			    flag = true;
			} else {
			    return false;
			}
		    }
		    return flag;
		} else {
		    return false;
		}
	    }
	} else {
	    return false;
	}
    }

    private static boolean compareShortArray(Field field, Object fieldobj1, Object fieldobj2, Class<?> fldclazz) {
	if (fldclazz.isArray()) {
	    if (Short[].class.equals(field.getType())) {
		Short[] fld1s = (Short[]) fieldobj1;
		Short[] fld2s = (Short[]) fieldobj2;
		boolean flag = true;
		if (fld1s.length == fld2s.length) {
		    for (int i = 0; i < fld1s.length; i++) {
			if (fld1s[i] == fld2s[i] || (fld1s[i] == null && fld2s[i] == null)) {
			    flag = true;
			} else if (fld1s[i].equals(fld2s[i])) {
			    flag = true;
			} else {
			    return false;
			}
		    }
		    return flag;
		} else {
		    return false;
		}
	    } else {
		short[] fld1s = (short[]) fieldobj1;
		short[] fld2s = (short[]) fieldobj2;
		boolean flag = true;
		if (fld1s.length == fld2s.length) {
		    for (int i = 0; i < fld1s.length; i++) {
			if (fld1s[i] == fld2s[i]) {
			    flag = true;
			} else {
			    return false;
			}
		    }
		    return flag;
		} else {
		    return false;
		}
	    }
	} else {
	    return false;
	}
    }

    private static boolean compareCharArray(Field field, Object fieldobj1, Object fieldobj2, Class<?> fldclazz) {
	if (fldclazz.isArray()) {
	    if (Character[].class.equals(field.getType())) {
		Character[] fld1s = (Character[]) fieldobj1;
		Character[] fld2s = (Character[]) fieldobj2;
		boolean flag = true;
		if (fld1s.length == fld2s.length) {
		    for (int i = 0; i < fld1s.length; i++) {
			if (fld1s[i] == fld2s[i] || (fld1s[i] == null && fld2s[i] == null)) {
			    flag = true;
			} else if (fld1s[i].equals(fld2s[i])) {
			    flag = true;
			} else {
			    return false;
			}
		    }
		    return flag;
		} else {
		    return false;
		}
	    } else {
		char[] fld1s = (char[]) fieldobj1;
		char[] fld2s = (char[]) fieldobj2;
		boolean flag = true;
		if (fld1s.length == fld2s.length) {
		    for (int i = 0; i < fld1s.length; i++) {
			if (fld1s[i] == fld2s[i]) {
			    flag = true;
			} else {
			    return false;
			}
		    }
		    return flag;
		} else {
		    return false;
		}
	    }
	} else {
	    return false;
	}
    }

    private static boolean compareLongArray(Field field, Object fieldobj1, Object fieldobj2, Class<?> fldclazz) {
	if (fldclazz.isArray()) {
	    if (Long[].class.equals(field.getType())) {
		Long[] fld1s = (Long[]) fieldobj1;
		Long[] fld2s = (Long[]) fieldobj2;
		boolean flag = true;
		if (fld1s.length == fld2s.length) {
		    for (int i = 0; i < fld1s.length; i++) {
			if (fld1s[i] == fld2s[i] || (fld1s[i] == null && fld2s[i] == null)) {
			    flag = true;
			} else if (fld1s[i].equals(fld2s[i])) {
			    flag = true;
			} else {
			    return false;
			}
		    }
		    return flag;
		} else {
		    return false;
		}
	    } else {
		long[] fld1s = (long[]) fieldobj1;
		long[] fld2s = (long[]) fieldobj2;
		boolean flag = true;
		if (fld1s.length == fld2s.length) {
		    for (int i = 0; i < fld1s.length; i++) {
			if (fld1s[i] == fld2s[i]) {
			    flag = true;
			} else {
			    return false;
			}
		    }
		    return flag;
		} else {
		    return false;
		}
	    }
	} else {
	    return false;
	}
    }

    private static boolean compareFloatArray(Field field, Object fieldobj1, Object fieldobj2, Class<?> fldclazz) {
	if (fldclazz.isArray()) {
	    if (Float[].class.equals(field.getType())) {
		Float[] fld1s = (Float[]) fieldobj1;
		Float[] fld2s = (Float[]) fieldobj2;
		boolean flag = true;
		if (fld1s.length == fld2s.length) {
		    for (int i = 0; i < fld1s.length; i++) {
			if (fld1s[i] == fld2s[i] || (fld1s[i] == null && fld2s[i] == null)) {
			    flag = true;
			} else if (fld1s[i].equals(fld2s[i])) {
			    flag = true;
			} else {
			    return false;
			}
		    }
		    return flag;
		} else {
		    return false;
		}
	    } else {
		float[] fld1s = (float[]) fieldobj1;
		float[] fld2s = (float[]) fieldobj2;
		boolean flag = true;
		if (fld1s.length == fld2s.length) {
		    for (int i = 0; i < fld1s.length; i++) {
			if (fld1s[i] == fld2s[i]) {
			    flag = true;
			} else {
			    return false;
			}
		    }
		    return flag;
		} else {
		    return false;
		}
	    }
	} else {
	    return false;
	}
    }

    private static boolean compareDoubleArray(Field field, Object fieldobj1, Object fieldobj2, Class<?> fldclazz) {
	if (fldclazz.isArray()) {
	    if (Double[].class.equals(field.getType())) {
		Double[] fld1s = (Double[]) fieldobj1;
		Double[] fld2s = (Double[]) fieldobj2;
		boolean flag = true;
		if (fld1s.length == fld2s.length) {
		    for (int i = 0; i < fld1s.length; i++) {
			if (fld1s[i] == fld2s[i] || (fld1s[i] == null && fld2s[i] == null)) {
			    flag = true;
			} else if (fld1s[i].equals(fld2s[i])) {
			    flag = true;
			} else {
			    return false;
			}
		    }
		    return flag;
		} else {
		    return false;
		}
	    } else {
		double[] fld1s = (double[]) fieldobj1;
		double[] fld2s = (double[]) fieldobj2;
		boolean flag = true;
		if (fld1s.length == fld2s.length) {
		    for (int i = 0; i < fld1s.length; i++) {
			if (fld1s[i] == fld2s[i]) {
			    flag = true;
			} else {
			    return false;
			}
		    }
		    return flag;
		} else {
		    return false;
		}
	    }
	} else {
	    return false;
	}
    }

    private static boolean compareBooleanArray(Field field, Object fieldobj1, Object fieldobj2, Class<?> fldclazz) {
	if (fldclazz.isArray()) {
	    if (Boolean[].class.equals(field.getType())) {
		Boolean[] fld1s = (Boolean[]) fieldobj1;
		Boolean[] fld2s = (Boolean[]) fieldobj2;
		boolean flag = true;
		if (fld1s.length == fld2s.length) {
		    for (int i = 0; i < fld1s.length; i++) {
			if (fld1s[i] == fld2s[i] || (fld1s[i] == null && fld2s[i] == null)) {
			    flag = true;
			} else if (fld1s[i].equals(fld2s[i])) {
			    flag = true;
			} else {
			    return false;
			}
		    }
		    return flag;
		} else {
		    return false;
		}
	    } else {
		boolean[] fld1s = (boolean[]) fieldobj1;
		boolean[] fld2s = (boolean[]) fieldobj2;
		boolean flag = true;
		if (fld1s.length == fld2s.length) {
		    for (int i = 0; i < fld1s.length; i++) {
			if (fld1s[i] == fld2s[i]) {
			    flag = true;
			} else {
			    return false;
			}
		    }
		    return flag;
		} else {
		    return false;
		}
	    }
	} else {
	    return false;
	}
    }

    private static boolean compareStringArray(Field field, Object fieldobj1, Object fieldobj2, Class<?> fldclazz) {
	if (fldclazz.isArray()) {
	    if (String[].class.equals(field.getType())) {
		String[] fld1s = (String[]) fieldobj1;
		String[] fld2s = (String[]) fieldobj2;
		boolean flag = true;
		if (fld1s.length == fld2s.length) {
		    for (int i = 0; i < fld1s.length; i++) {
			if (fld1s[i] == fld2s[i] || (fld1s[i] == null && fld2s[i] == null)) {
			    flag = true;
			} else if (fld1s[i].equals(fld2s[i])) {
			    flag = true;
			} else {
			    return false;
			}
		    }
		    return flag;
		} else {
		    return false;
		}
	    } else {
		return false;
	    }
	} else {
	    return false;
	}
    }
}
