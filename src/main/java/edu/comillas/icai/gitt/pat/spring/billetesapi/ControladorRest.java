package edu.comillas.icai.gitt.pat.spring.billetesapi;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
public class ControladorRest {

    public record ContactoCreado (
            @NotBlank(message = "Debe introducir un nombre de usuario")
            String idContacto,

            @NotBlank(message = "Debe introducir el id del billete que quiere vender")
            String idBillete
    ) {}

    public record BilleteCreado (
        @NotBlank(message="Debe rellenar el id del billete")  //esto se añade para validar.
        String id,

        @NotBlank(message="Debe rellenar su pais de procedencia")
        String pais,

        @NotNull(message="Rellene el año de expedición")
        Long año,

        @NotNull(message="El valor no puede ser nulo")
        String valor,

        @NotNull(message="El precio no puede ser nulo")
        Long precio


    ) {}

    private final Map<String, BilleteCreado> billetes= new HashMap<>();
    private final Map<String, List<String>> contactos= new HashMap<>();

    private Logger logger = LoggerFactory.getLogger(getClass());
    @PostMapping("/api/billetes")
    //@PreAuthorize("hasRole('ADMIN')") //AQUI PERMITO QUE ADMIN HAGA LAS PETICIONES, EL RESTO DE ROLES NO
    @ResponseStatus(HttpStatus.CREATED)
    public BilleteCreado crearBillete(@Valid @RequestBody BilleteCreado billeteNuevo, BindingResult bindingResult) {
        logger.error("Se ha producido un error inesperado al crear el billete {}", billeteNuevo);
        logger.debug("Datos recibidos para el nuevo billete: {}", billeteNuevo);

        if(bindingResult.hasErrors()) {
            throw new ExcepcionActualizacionErronea(bindingResult);
        }

        if(billetes.get(billeteNuevo.id)!=null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }



        billetes.put(billeteNuevo.id(), billeteNuevo); //
        return billeteNuevo;
    }

    @PostMapping("/api/contacto")
    @ResponseStatus(HttpStatus.CREATED)
    public ContactoCreado crearContacto(@Valid @RequestBody ContactoCreado contactoNuevo, BindingResult bindingResult) {
        logger.error("Se ha producido un error inesperado al crear el billete {}", contactoNuevo);
        logger.debug("Datos recibidos para el nuevo billete: {}", contactoNuevo);

        if(bindingResult.hasErrors()) {
            throw new ExcepcionActualizacionErronea(bindingResult);
        }
        if (contactos.get(contactoNuevo.idBillete)!=null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }


        List<String> listaBilletes = new ArrayList<String>();
        listaBilletes.add(contactoNuevo.idBillete());
        contactos.put(contactoNuevo.idContacto(), listaBilletes);
        return contactoNuevo;
    }

    @PutMapping("api/contacto/id/{idContacto}/actualizar/{idBillete}")
    public List<String> actualizarBillete(@PathVariable String idContacto,
                                           @PathVariable String idBillete) {
        List<String> contacto = contactos.get(idContacto);
        if(contacto==null){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        contacto.add(idBillete);

        contactos.put(idContacto, contacto);
        return contacto;
    }

    @GetMapping("api/contacto/id/{idContacto}")
    public List<String> buscarContacto(@PathVariable String idContacto) {
        if(contactos.get(idContacto)==null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return contactos.get(idContacto);
    }

    @GetMapping("api/billetes/id/{id}")
    public BilleteCreado buscar(@PathVariable String id) {
        if(billetes.get(id)==null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return billetes.get(id);
    }

    @PutMapping("api/billetes/id/{id}/actualizar")
    public BilleteCreado actualizarBillete(@PathVariable String id,
                                           @RequestParam(required=false) String pais,
                                           @RequestParam(required=false) Long año,
                                           @RequestParam(required=false) String valor,
                                           @RequestParam(required=false) Long precio) {
        BilleteCreado billeteExistente = billetes.get(id);

        if(billeteExistente==null){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        Long precio_c = (precio != null) ? precio : billeteExistente.precio();
        String valor_c = (valor != null) ? valor : billeteExistente.valor();
        Long año_c = (año != null) ? año : billeteExistente.año();
        String pais_c = (pais != null) ? pais : billeteExistente.pais();
        BilleteCreado billeteActualizado = new BilleteCreado(id, pais_c, año_c, valor_c, precio_c);
        billetes.put(id, billeteActualizado);
        return billeteActualizado;
    }

    @DeleteMapping("/api/billetes/id/{id}/borrar")
    public void borrar(@PathVariable String id) {
        if (billetes.get(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        billetes.remove(id); //porque es un hashmap
    }

    @DeleteMapping("/api/contacto/id/{idContacto}/borrar/{idBillete}")
    public void borrar(@PathVariable String idContacto, @PathVariable String idBillete) {
        if (contactos.get(idContacto) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        List<String> listaBilletes = contactos.get(idContacto);
        listaBilletes.remove(idBillete);
        }

}
