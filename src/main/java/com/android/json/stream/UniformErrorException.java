package com.android.json.stream;



public class UniformErrorException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = 3619457667407529282L;
    int	mErrorCode;
    String	mErrorMessage;
    
    public UniformErrorException(int errorCode, String errMsg){
	mErrorCode = errorCode;
	mErrorMessage = errMsg ;
    }
    
    public int	getErrorCode(){
	return mErrorCode ;
    }
    
    public String	getErrorMessage(){
	return mErrorMessage;
    }
    
    public UniformErrorResponse getError(){
	UniformErrorResponse err = new UniformErrorResponse();
	err.errorCode = mErrorCode;
	err.errorMessage = mErrorMessage;
	return err ;
    }
    
    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder();
	sb.append(mErrorMessage)
		.append('(')
		.append(mErrorCode)
		.append(')');
	return super.toString();
    }    
}
