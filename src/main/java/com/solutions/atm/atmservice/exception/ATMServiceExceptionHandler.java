package com.solutions.atm.atmservice.exception;

import com.solutions.atm.atmservice.model.ErrorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ATMServiceExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ATMServiceExceptionHandler.class);


    /**
     * This is sample global exception handler, assume that application has similar one, and all unchecked exceptions are handled by it.
     * This method handles InvalidArgumentException exception and returns a response body with bad request status.
     */
    @ExceptionHandler(InvalidArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorInfo handleException(HttpServletRequest request, InvalidArgumentException exception){
        logger.error("InvalidArgumentException occurred, responding with Bad Request, exception : {}", exception);
        return new ErrorInfo(request.getRequestURI(), exception.getMessage());
    }
}
