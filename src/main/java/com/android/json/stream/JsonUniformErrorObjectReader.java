package com.android.json.stream;

import java.io.IOException;

public class JsonUniformErrorObjectReader extends JsonObjectReader {

    public JsonUniformErrorObjectReader(JsonReader reader) {
	super(reader);
    }

    public void readObject(Object object) throws UniformErrorException,IOException  {
	super.readObject(object);
	if (object instanceof UniformErrorResponse){ //检查
	    UniformErrorResponse error = (UniformErrorResponse)object;
	    if (error.hasError()){ //检测到统一错误接口定义的错误，抛出异常，框架类及业务类需捕获该异常进行处理，给出相应的错误提示
		throw new UniformErrorException(error.errorCode, error.errorMessage);
	    }
	}
    }

}
