package com.flexcub.resourceplanning.exceptionhandler;

import com.flexcub.resourceplanning.exceptions.ControllerException;
import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.utils.FlexcubErrorCodes;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid
            (MethodArgumentNotValidException ex,
             HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> errorMap = new HashMap<>();
        List<String> errorList = ex.getBindingResult().
                getFieldErrors().stream().
                map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        errorMap.put("errors", errorList);
        errorMap.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(errorMap, status);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ControllerException> handleServiceException(ServiceException e) {
        ControllerException controllerException = new ControllerException(e.getErrorCode(), e.getErrorDesc());
        if (controllerException.getErrorCode().equalsIgnoreCase(FlexcubErrorCodes.DATA_NOT_SAVED.getErrorCode())) {
            return new ResponseEntity<>(controllerException, HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (controllerException.getErrorCode().equalsIgnoreCase(FlexcubErrorCodes.DATA_NOT_FOUND.getErrorCode()) ||
                controllerException.getErrorCode().equalsIgnoreCase(FlexcubErrorCodes.INVALID_REGISTRATION_LINK.getErrorCode())) {
            return new ResponseEntity<>(controllerException, HttpStatus.NOT_FOUND);
        } else if (controllerException.getErrorCode().equalsIgnoreCase(FlexcubErrorCodes.ATTACHMENT_UPDATE_FAILED.getErrorCode())) {
            return new ResponseEntity<>(controllerException, HttpStatus.SERVICE_UNAVAILABLE);
        } else {
            return new ResponseEntity<>(controllerException, HttpStatus.BAD_REQUEST);
        }
    }

}
