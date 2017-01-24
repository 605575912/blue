package com.android.json.stream;

import android.text.TextUtils;

import com.support.loader.proguard.IProguard;

/**
 * 定义错误响应，统一错误接口实现时，所有JSON响应数据定义都必须继承该类
 * @author lhy
 *
 */
public class UniformErrorResponse implements IProguard.ProtectMembers{
    public Integer	errorCode ; //错误码
    public String	errorMessage; //    错误描述
    /**
     * 判断是否有错误，仅当errorCode！=0和errorMessage不为空时才算有错误
     */
    public boolean hasError(){
	if (errorCode != null && errorCode.intValue()  != 0 && !TextUtils.isEmpty(errorMessage))
	    return true;
	else
	    return false;
    }
    
    public int	getErrorCode(){
	return errorCode == null ? 0 : errorCode.intValue();
    }
    /**
     * 检查TOKEN是否过期
     * @return
     */
    public boolean checkTokenIfExpired(){
	if (errorCode == null )
	    return false;
	if (errorCode >= 104 && errorCode <= 108 || errorCode == 2){ //2013.4.15 逄万春增加 “LOGIN_STATUS_TAG=2 需要吊起登陆标签”
	    return true;
	}else{
	    return false;
	}
    }
}
