package edu.comillas.icai.gitt.pat.spring.billetesapi;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@Controller public class ControladorSSR {

    private final RestTemplate restTemplate;

    @Autowired
    public ControladorSSR(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/usuario")
    public String registro(
            @Valid @ModelAttribute("contactoNuevo") //record que me hace la validacion
            FormularioRegistro contactoNuevo,
            BindingResult result,
            Model model //creo un modelo generico al que meter atributos.
    ) {
        if (!result.hasErrors()) { //en caso de no haber errores: meto el atributo de exito.
            model.addAttribute("Ha sido registrado con exito", contactoNuevo.id());
        }
        return "contactoNuevo";
    }

    @GetMapping("/usuario/id/{id}")
    public List<String> billetesUsuario (@PathVariable String idC, Model model) {
        String url = "http://localhost:8080/api/contacto/id/" + idC;
        List <String> listaBilletes= restTemplate.getForObject(url, ArrayList.class);

        if(listaBilletes!=null) {
            model.addAttribute("billetes", listaBilletes);
        }

        return listaBilletes;
    }

    @GetMapping("/usuario/venta")
    public String mostrarBilleteVenta(Model model) {
        model.addAttribute("billeteVenta", new FormularioBillete("", "",0L ,"" , 0L));
        return "billeteVenta";
    }


    @PostMapping("/usuario/billete")
    public String venta(
            @Valid @ModelAttribute("billeteVenta") //record que me hace la validacion
            FormularioBillete billeteVenta,
            BindingResult result,
            Model model //creo un modelo generico al que meter atributos.
    ) {
        if (!result.hasErrors()) { //en caso de no haber errores: meto el atributo de exito.
            model.addAttribute("exito",
                    "Su billete " + billeteVenta.id() + ", ha sido registrado con exito.");
        }
        return "billeteNuevo"; //este campo me retorna contacto sin el campo de exito.
    }

    public record FormularioRegistro (
            @Email(message = "El formato del email es incorrecto")
            @NotBlank(message = "El email es obligatorio ")
            String email,

            @NotBlank(message = "El id del billete no puede estar vacío")
            //no puede venir en blanco_ obligatorio
            @Size(max = 10, message = "El tamaño no puede superar 10 caracteres")
            String id,
            @NotBlank(message = "Introduzca una contraseña")
            String contraseña

    ) {}

    public record FormularioBillete(
    @NotBlank(message = "El id del billete no puede estar vacío")
     //no puede venir en blanco_ obligatorio
     @Size(max = 10, message = "El tamaño no puede superar 10 caracteres")
     String id,
     @NotBlank(message = "El billete debe tener un pais de procedencia")
     String pais,

     @NotNull(message = "El año de expedicion no puede estar vacío")
     Long año,
     @NotNull(message = "El billete debe tener un valor")
     String valor,
     @NotNull(message = "El precio de venta no puede estar vacío")
     Long precio
    ) {}

}
