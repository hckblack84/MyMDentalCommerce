package com.MyMDentis.MyMDentistComerce.Exception;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NullValuesEntityException extends RuntimeException {

    private String code;
    private String nullAttribute;

    public NullValuesEntityException(String codeException, String nullAttribute, String message) {
        super(message);
        this.nullAttribute = nullAttribute;
        this.code = codeException;
    }


}
