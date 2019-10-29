package com.keuby.ozcowms.product.controller;

import com.keuby.ozcowms.common.exception.ServiceException;
import com.keuby.ozcowms.common.response.JsonResp;
import com.keuby.ozcowms.common.response.RespCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@ControllerAdvice
@ResponseBody
public class ErrorHandlerController {

    @ExceptionHandler(ConstraintViolationException.class)
    public JsonResp violationException(ConstraintViolationException cause) {
        Set<ConstraintViolation<?>> violations =  cause.getConstraintViolations();
        List<String> messages = new ArrayList<>();
        for (ConstraintViolation<?> violation : violations) {
            String message = violation.getMessage();
            List<Path.Node> paths = StreamSupport.stream(violation.getPropertyPath().spliterator(), false).collect(Collectors.toList());
            Path.Node node = paths.get(paths.size() - 1);
            messages.add(node.getName() + ' ' + message);
        }
        return JsonResp.error(RespCode.PARAM_INCORRECT_CODE, String.join(",", messages), null);
    }

    @ExceptionHandler(ServiceException.class)
    public JsonResp serviceException(ServiceException cause) {
        return JsonResp.error(cause.getCode(), cause.getMessage(), cause.getData());
    }
}
