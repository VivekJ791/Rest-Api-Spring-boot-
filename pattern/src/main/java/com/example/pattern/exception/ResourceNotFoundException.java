package com.example.pattern.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends Exception{
    String resourceName;
    String fieldValue;
    Object fieldName;

    public ResourceNotFoundException(String resourceName, String fieldValue, Object fieldName) {
        super(String.format("%s is not found in %s :'%s'",resourceName,fieldValue,fieldName));
        this.resourceName = resourceName;
        this.fieldValue = fieldValue;
        this.fieldName = fieldName;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public Object getFieldName() {
        return fieldName;
    }
}
