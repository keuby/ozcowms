package com.keuby.ozcowms.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JsonResp<T> {
    private String code;
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private String msg;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private T data;

    public boolean isOk() {
        return RespCode.COMMON_SUCCESS_CODE.equals(code);
    }

    public static <T> JsonResp ok(String msg, T data) {
        return new JsonResp(RespCode.COMMON_SUCCESS_CODE, msg, data);
    }

    public static <T> JsonResp ok(T data) {
        return ok("ok", data);
    }

    public static <T> JsonResp ok() {
        return ok("ok", null);
    }

    public static <T> JsonResp error(String code, String msg, T data) {
        return new JsonResp(code, msg, data);
    }

    public static <T> JsonResp error(String msg, T data) {
        return error(RespCode.SERVER_ERROR_CODE, msg, data);
    }

    public static <T> JsonResp error(T data) {
        return error("server error", data);
    }

    public static <T> JsonResp error(String msg) {
        return error(msg, null);
    }

    public static <T> JsonResp error() {
        return error(null);
    }

    public static <T> JsonResp paramsMissing(String msg, T data) {
        return error(RespCode.PARAM_MISSING_CODE, msg, data);
    }

    public static <T> JsonResp paramsMissing(String msg) {
        return error(RespCode.PARAM_MISSING_CODE, msg, null);
    }

    public static <T> JsonResp paramsMissing(T data) {
        return paramsMissing("params is missing", data);
    }

    public static JsonResp paramsMissing() {
        return paramsMissing(null);
    }

    public static <T> JsonResp paramsError(String msg, T data) {
        return error(RespCode.PARAM_INCORRECT_CODE, msg, data);
    }

    public static <T> JsonResp paramsError(T data) {
        return paramsError("params is missing", data);
    }

    public static JsonResp paramsError() {
        return paramsError(null);
    }

    public static <T> JsonResp notFound(String msg, T data) {
        return error(RespCode.PAGE_NOT_FOUNT_ERROR_CODE, msg, data);
    }

    public static <T> JsonResp notFound(String msg) {
        return error(RespCode.PAGE_NOT_FOUNT_ERROR_CODE, msg, null);
    }

    public static <T> JsonResp notFound(T data) {
        return notFound("page not found", data);
    }

    public static <T> JsonResp notFound() {
        return notFound(null);
    }
}
