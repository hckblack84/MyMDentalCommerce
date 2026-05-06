package com.MyMDentis.MyMDentistComerce.Exception;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NotFoundEntityException extends RuntimeException {

    String code;
    String entity;

    public NotFoundEntityException(String code, String entity, String message) {
        super(message);
        this.code = code;
        this.entity = entity;
    }
}
