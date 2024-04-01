package edu.comillas.icai.gitt.pat.spring.billetesapi;


import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

public class ExcepcionActualizacionErronea extends RuntimeException {
    private List<FieldError> errores;
    public ExcepcionActualizacionErronea(BindingResult result) {
        this.errores = result.getFieldErrors();
    }
    public List<FieldError> getErrores() {
        return errores;
    }
}
