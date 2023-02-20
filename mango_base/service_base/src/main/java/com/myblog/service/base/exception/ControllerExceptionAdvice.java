package com.myblog.service.base.exception;

import com.myblog.service.base.common.ResultModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ResultModel<?> businessExceptionHandler(BusinessException exception) {
        return ResultModel.error().message(exception.getMessage());
    }

    @ExceptionHandler(LoginException.class)
    @ResponseBody
    public ResultModel<?> loginExceptionHandler(LoginException exception) {
        return ResultModel.error().message(exception.getMessage());
    }

    @ExceptionHandler(MyAccessDeniedException.class)
    @ResponseBody
    public ResultModel<?> myAccessDeniedExceptionHandler(MyAccessDeniedException exception) {
        return ResultModel.error().message(exception.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ResultModel<?> throwableHandler(Throwable exception) {
        return ResultModel.error();
    }
}
