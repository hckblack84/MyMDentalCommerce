package com.MyMDentis.MyMDentistComerce.Exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
public class DTONotFoundEntityException {

    public String code;
    public String entity;
    public String message;

}
