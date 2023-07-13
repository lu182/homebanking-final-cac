package com.cac.homebankingfinalcac.infrastructure.utils.responsegeneric;

import java.util.Date;
import java.util.List;

public class ResponseDTO {

    private boolean success;
    private Object data;
    private int httpCode;
    private String message;
    private Date date = new Date();
    private List<String> errors;

    //Constructor vacío:
    public ResponseDTO() {
    }

    //Constructor adaptado - sin fecha ni lista de errores:
    public ResponseDTO(boolean success, Object data, int httpCode, String message) {
        this.success = success;
        this.data = data;
        this.httpCode = httpCode;
        this.message = message;
    }


    //Constructor adaptado - sin fecha con lista de errores:
    public ResponseDTO(boolean success, Object data, int httpCode, String message, List<String> errors) {
        this.success = success;
        this.data = data;
        this.httpCode = httpCode;
        this.message = message;
        this.errors = errors;
    }


    //Getters & Setters:
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /*
    public Date getDate() {
        return date;
    }
    */

    public void setDate(Date date) {
        this.date = date;
    }

    /*
    public List<String> getErrors() {
        return errors;
    }
    */

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

}
