package com.keuby.ozcowms.common.exception;

import com.keuby.ozcowms.common.response.RespCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceException extends RuntimeException {
    private String code;
    private Object data;

    public ServiceException(String code, String message, Object data, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.data = data;
    }

    public ServiceException(String code, String message) {
        this(code, message, null, null);
    }

    public ServiceException(String message, Object data) {
        this(RespCode.SERVER_ERROR_CODE, message, data, null);
    }

    public ServiceException(String message) {
        this(RespCode.SERVER_ERROR_CODE, message);
    }
}
