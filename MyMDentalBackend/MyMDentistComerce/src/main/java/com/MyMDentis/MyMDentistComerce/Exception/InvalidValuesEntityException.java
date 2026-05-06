package com.MyMDentis.MyMDentistComerce.Exception;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class InvalidValuesEntityException extends RuntimeException {

    private String code;
    private String attribute;

    public InvalidValuesEntityException(String code, String attribute, String message) {
        super(message);
        this.code = code;
        this.attribute = attribute;
    }
}
