package com.synclab.triphippie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class UniqueFieldException extends RuntimeException{
    public UniqueFieldException(String message){
        super(message);
    }
}

