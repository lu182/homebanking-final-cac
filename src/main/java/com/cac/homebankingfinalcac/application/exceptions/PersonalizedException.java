package com.cac.homebankingfinalcac.application.exceptions;

import com.cac.homebankingfinalcac.infrastructure.utils.responsegeneric.ResponseDTO;
import org.springframework.http.HttpStatus;

public class PersonalizedException extends RuntimeException{

    private HttpStatus status;
    private String message;
    private ResponseDTO dataResponse;

    //Constructor vacío:
    public PersonalizedException() {
    }

    //Constructor con parámetros:
    public PersonalizedException(HttpStatus status, String message, ResponseDTO dataResponse) {
        super();
        this.status = status;
        this.message = message;
        this.dataResponse = dataResponse;
    }

    //Constructor adaptado:
    public PersonalizedException(HttpStatus status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

    //Constructor adaptado:
    public PersonalizedException(HttpStatus status, ResponseDTO dataResponse) {
        super();
        this.status = status;
        this.dataResponse = dataResponse;
    }

    //Constructor adaptado:
    public PersonalizedException(String message) {
        super(message);
        this.message = message;
    }

    //Getters & Setters:
    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResponseDTO getDataResponse() {
        return dataResponse;
    }

    public void setDataResponse(ResponseDTO dataResponse) {
        this.dataResponse = dataResponse;
    }
}
