package com.myblog.service.base.common;

import com.myblog.service.base.util.JsonUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;


@Data
@ApiModel(value = "全局统一返回结果")
public class Response {

    @ApiModelProperty(value = "是否成功")
    private Boolean success;

    @ApiModelProperty(value = "返回码")
    private Integer code;

    @ApiModelProperty(value = "返回消息")
    private String message;

    @ApiModelProperty(value = "返回数据")
    private Map<String, Object> data = new HashMap<String, Object>();

    public Response(){}

    public static Response ok(){
        Response r = new Response();
        r.setSuccess(ResultCodeEnum.SUCCESS.getSuccess());
        r.setCode(ResultCodeEnum.SUCCESS.getCode());
        r.setMessage(ResultCodeEnum.SUCCESS.getMessage());
        return r;
    }

    public static Response error(){
        Response r = new Response();
        r.setSuccess(ResultCodeEnum.UNKNOWN_REASON.getSuccess());
        r.setCode(ResultCodeEnum.UNKNOWN_REASON.getCode());
        r.setMessage(ResultCodeEnum.UNKNOWN_REASON.getMessage());
        return r;
    }

    public static Response setResult(ResultCodeEnum resultCodeEnum){
        Response r = new Response();
        r.setSuccess(resultCodeEnum.getSuccess());
        r.setCode(resultCodeEnum.getCode());
        r.setMessage(resultCodeEnum.getMessage());
        return r;
    }

    public Response success(Boolean success){
        this.setSuccess(success);
        return this;
    }

    public Response message(String message){
        this.setMessage(message);
        return this;
    }

    public Response code(Integer code){
        this.setCode(code);
        return this;
    }

    public Response data(String key, Object value){
        this.data.put(key, value);
        return this;
    }

    public Response data(Map<String, Object> map){
        this.setData(map);
        return this;
    }

    @Override
    public String toString() {
        return JsonUtils.objectToJson(this);
    }
}
