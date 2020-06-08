package com.serice.sericefeign.exception;

public class NoMappingParamString extends Exception {

    /*无参构造函数*/
    public NoMappingParamString(){
        super();
    }

    //用详细信息指定一个异常
    public NoMappingParamString(String message){
        super(message);
    }

    //用指定的详细信息和原因构造一个新的异常
    public NoMappingParamString(String message, Throwable cause){
        super(message,cause);
    }

    //用指定原因构造一个新的异常
    public NoMappingParamString(Throwable cause) {
        super(cause);
    }
}
