package org.linlinjava.litemall.admin.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Log logger = LogFactory.getLog(GlobalExceptionHandler.class);


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public Object argumentHandler(MethodArgumentTypeMismatchException e){
        e.printStackTrace();
        return ResponseUtil.badArgumentValue();
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object exceptionHandler(Exception e){
        e.printStackTrace();

        logger.error(e);

        return ResponseUtil.serious();
    }

}
