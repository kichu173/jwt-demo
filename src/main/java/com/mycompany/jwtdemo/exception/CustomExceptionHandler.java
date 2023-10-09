package com.mycompany.jwtdemo.exception;

import com.mycompany.jwtdemo.dto.ErrorDTO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<List<ErrorDTO>> handleBusinessException(BusinessException be) {
        for(ErrorDTO errorDTO: be.getErrors()) {
            log.error("Business Exception Error code: {}, message: {}", errorDTO.getCode(), errorDTO.getMessage());
        }
        return new ResponseEntity<>(be.getErrors(), HttpStatus.BAD_REQUEST);
    }
}
