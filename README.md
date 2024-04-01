# Backend para Página Web de Billetes Antiguos

Este repositorio contiene el código del backend para una página web dedicada a la gestión de billetes antiguos. El backend está desarrollado en [Java](https://www.java.com) utilizando el framework [Spring Boot](https://spring.io/projects/spring-boot).
Forma parte de la evaluación continua de la asignatura: Programación de Aplicaciones Telemáticas.

## Descripción de los Endpoints

A continuación se detalla la descripción de cada endpoint del backend:

| Método | Ruta| Descripción| Posibles Respuestas     
|--------|----------------------------------------|---------------------------------------------------|------------------------------------------------------|
| GET    | /api/billetes/id/{id}| Obtiene las características de un billete por su ID| 200: Retorna el billete solicitado<br>404: Billete no encontrado |
| POST   | /api/billetes| Crea el billete junto con todas sus características| 201: Billete creado exitosamente<br>404: No encontrado |
| PUT    | /api/billetes/id/{id}/actualizar| Actualiza cualquiera de los valores de un billete existente| 200: Billete actualizado exitosamente<br>409: Billete no encontrado |
| DELETE | /api/billetes/id/{id}/borrar| Elimina un billete por su ID| 204: Billete eliminado exitosamente<br>404: Billete no encontrado |
| GET    | /api/contacto/id/{idContacto}| Obtiene la lista de los billetes subidos por el usuario con su id| 200: Retorna el contacto solicitado<br>404: Contacto no encontrado |
| POST   | /api/contacto|Crea un nuevo registro de usuario| 201: Contacto creado exitosamente<br>400: Solicitud inválida |
| PUT    | /api/contacto/id/{idContacto}/actualizar/{idBillete}| Actualiza los billetes que tiene subidos un usuario registrado| 200: Contacto actualizado exitosamente<br>404: Contacto no encontrado |
| DELETE | /api/contacto/id/{idContacto}/borrar/{idBillete}| Elimina un billete de un usuario | 204: Contacto eliminado exitosamente<br>404: Contacto no encontrado |
