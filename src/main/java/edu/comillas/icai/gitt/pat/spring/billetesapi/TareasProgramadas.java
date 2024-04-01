package edu.comillas.icai.gitt.pat.spring.billetesapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

public class TareasProgramadas {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private String respuesta_ant;

    @Value("${url}") private String url;

    public TareasProgramadas() {respuesta_ant=new String();}
    @Scheduled(fixedRate = 300000)
    public void ritmoFijo() {
        logger.info("Me ejecuto cada 5 minutos");
    }

    @Scheduled(cron = "0 * * * * *")
    public void expresionCron() {
        logger.info("Me ejecuto cuando empieza un nuevo minuto");
    }

    @Scheduled(fixedRate = 60000)  //esto es una tarea programada
    public void consultarAPI() {
        //String.url="https://xkcd.com/info.0.json";
        logger.info("la url es: "+url);

        try { //lanzamos misma peticion api. hay que compararlo con lo atilano.
            HttpHeaders headers = new HttpHeaders();
            //headers.set("Cabecera", "Valor");
            ResponseEntity<String> response = new RestTemplate().exchange(
                    url, HttpMethod.GET,
                    new HttpEntity<>( headers),
                    String.class
            );

            if(response.getStatusCode()== HttpStatus.OK) {
                String respuesta = response.getBody();
                logger.info("he recibido respuesta");
                if (respuesta != this.respuesta_ant) {
                    logger.info(respuesta);
                }
                respuesta_ant = respuesta;
            }
        } catch (HttpStatusCodeException e) {  //recogemos el resultado
            logger.error("Error {} en la respuesta", e.getStatusCode());
        } catch (Exception e) {
            logger.error("Error inesperado en la llamada del API", e);
        }


    }
}
