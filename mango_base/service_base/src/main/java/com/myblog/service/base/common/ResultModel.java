package com.myblog.service.base.common;

import java.util.Map;

import lombok.Data;

@Data
public class ResultModel<T> {

    private Integer code;

    private String message;

    private Boolean success;

    private T data;


    public static <T> ResultModel<T> ok(){
        ResultModel<T>  resultModel = new ResultModel();
        resultModel.setSuccess(ResultCodeEnum.SUCCESS.getSuccess());
        resultModel.setCode(ResultCodeEnum.SUCCESS.getCode());
        resultModel.setMessage(ResultCodeEnum.SUCCESS.getMessage());
        return resultModel;
    }

    public static <T> ResultModel<T> ok(T data){
        ResultModel<T>  resultModel = new ResultModel();
        resultModel.setSuccess(ResultCodeEnum.SUCCESS.getSuccess());
        resultModel.setCode(ResultCodeEnum.SUCCESS.getCode());
        resultModel.setMessage(ResultCodeEnum.SUCCESS.getMessage());
        resultModel.setData(data);
        return resultModel;
    }

    public static <T> ResultModel<T> error(){
        ResultModel<T>  resultModel = new ResultModel();
        resultModel.setSuccess(ResultCodeEnum.UNKNOWN_REASON.getSuccess());
        resultModel.setCode(ResultCodeEnum.UNKNOWN_REASON.getCode());
        resultModel.setMessage(ResultCodeEnum.UNKNOWN_REASON.getMessage());
        return resultModel;
    }

    public static <T> ResultModel<T> setResult(ResultCodeEnum resultCodeEnum){
        ResultModel<T>  resultModel = new ResultModel();
        resultModel.setSuccess(resultCodeEnum.getSuccess());
        resultModel.setCode(resultCodeEnum.getCode());
        resultModel.setMessage(resultCodeEnum.getMessage());
        return resultModel;
    }

    public ResultModel<T> success(Boolean success){
        this.setSuccess(success);
        return this;
    }

    public ResultModel<T> message(String message){
        this.setMessage(message);
        return this;
    }

    public ResultModel<T> code(Integer code){
        this.setCode(code);
        return this;
    }

    public ResultModel<T> data(T data){
        this.setData(data);
        return this;
    }

}
