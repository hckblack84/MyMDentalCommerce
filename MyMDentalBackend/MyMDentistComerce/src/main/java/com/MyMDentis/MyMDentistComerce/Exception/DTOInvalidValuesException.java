package com.MyMDentis.MyMDentistComerce.Exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DTOInvalidValuesException {

    private String code;
    private String attribute;
    private String message;

    public DTOInvalidValuesException(String code, String attribute, String message) {
        this.message = message;
        this.code = code;
        this.attribute = attribute;
    }
}
