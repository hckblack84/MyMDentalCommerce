package com.MyMDentis.MyMDentistComerce.Exception;

import javax.print.DocFlavor;

public interface ExceptionValues {

    String UNKNOWN_EXCEPTION_CODE = "?-001";
    String UNKNOW_EXCEPTION_MESSAGE = "Un error interno no se pudo concretar. Por favor intentarlo mas tarde";

    String NULL_VALUES_EXCEPTION_CODE = "P-001";
    String NULL_VALUES_EXCEPTION_MESSAGE = "Existen valores nulos en la petición";

    String INVALID_VALUES_EXCEPTION_CODE = "P-002";
    String INVALID_VALUES_EXCEPTION_MESSAGE = "Valores entregados no corresponden al formato especificado";

    String INVALID_PRODUCT_CODE_EXCEPTION_CODE = "P-003";
    String INVALID_PRODUCT_CODE_EXCEPTION_MESSAGE = "Formato de codigo de producto invalido";

    String INVALID_PRICE_PRODUCT_EXCEPTION_CODE = "P-004";
    String INVALID_PRICE_PRODUCT_EXCEPTION_MESSAGE = "Precio de producto venta no puede ser menor al costo";

    String INVALID_STOCK_PRODUCT_EXCEPTION_CODE = "P-005";
    String INVALID_STOCK_PRODUCT_EXCEPTION_MESSAGE = "Stock critico no puede ser menor al stock actual";

    String DEPARTMENT_NOT_FOUND_EXCEPTION_CODE = "P-006";
    String DEPARTMENT_NOT_FOUND_EXCEPTION_MESSAGE = "Departamento no encontrado";

    String NAME_PRODUCT_ALREADY_EXIST_CODE = "P-007";
    String NAME_PRODUCT_ALREADY_EXIST_MESSAGE = "Nombre del producto ya existe en el sistema";

    String CODE_PRODUCT_ALREADY_EXIST_CODE = "P-008";
    String CODE_PRODUCT_ALREADY_EXIST_MESSAGE = "Codigo del producto ya existe en el sistema";

    String PRODUCT_NOT_FOUND_CODE = "E-001";
    String PRODUCT_NOT_FOUND_MESSAGE = "Producto no encontrado";

    String DEPARTMENT_NOT_FOUND_CODE = "E-002";
    String DEPARTMENT_NOT_FOUND_MESSAGE = "Departamento no encontrado";

    String USER_NOT_FOUND_CODE = "U-001";
    String USER_NOT_FOUND_MESSAGE = "Usuario no encontrado";

    String USER_REGISTER_INVALID_CODE = "U-002";
    String USER_REGISTER_INVALID_MESSAGE = "El registro de usuario es invalido";

    String USER_ALREADY_EXIST_CODE = "U-003";
    String USER_ALREADY_EXIST_MESSAGE = "Ya existe un usuario con este nombre";

    String EMAIL_USER_ALREADY_EXIST_CODE = "U-004";
    String EMAIL_USER_ALREADY_EXIST_MESSAGE = "Ya existe un usuario con este email";

    String NOT_USER_CREDENTIALS_CODE = "U-005";
    String NOT_USER_CREDENTIALS_MESSAGE = "Usuario sin credenciales";

    String VALUES_NOT_COMPATIBLE_REQUEST_CODE = "R-001";
    String VALUES_NOT_COMPATIBLE_REQUEST_MESSAGE = "Valores de la peticion no compatibles con la entrada";

    String INVALID_STOCK_REQUEST_CODE = "R-001";
    String INVALID_STOCK_REQUEST_MESSAGE = "No se puede pedir mas stock de la que existe";

}
