package com.MyMDentis.MyMDentistComerce.Exception;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NullValuesEntityException extends RuntimeException {

    private String code;

    public NullValuesEntityException(String codeException, String message) {
        super(message);
        this.code = codeException;
    }


}
