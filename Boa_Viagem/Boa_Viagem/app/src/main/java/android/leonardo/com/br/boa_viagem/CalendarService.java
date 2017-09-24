package android.leonardo.com.br.boa_viagem;




import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.services.GoogleKeyInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import com.google.api.client.util.DateTime;


import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import static com.google.api.client.extensions.android2.AndroidHttp.newCompatibleTransport;


public class CalendarService {

    private Calendar calendar;
    private String nomeConta;
    //Construtor
    public CalendarService(String nomeConta, String tokenAcesso) {
        this.nomeConta = nomeConta;


        GoogleCredential credencial = new GoogleCredential();

        credencial.setAccessToken(tokenAcesso);
        HttpTransport transport = newCompatibleTransport();
        JsonFactory jsonFactory = new GsonFactory();



        calendar = Calendar.builder(transport, jsonFactory)
                .setApplicationName(Constantes.APP_NAME)
                .setHttpRequestInitializer(credencial)
                .setJsonHttpRequestInitializer(
                        new GoogleKeyInitializer(Constantes.API_KEY))
                .build();



    }


    //Criando evento no Calendario do google
    public String criarEvento(Viagem viagem) {
        Event evento = new Event();
        evento.setSummary(viagem.getDestino());
        List<EventAttendee> participantes =
                Arrays.asList((new EventAttendee().setEmail(nomeConta)));
        evento.setAttendees(participantes);



     DateTime inicio = new DateTime(viagem.getDateChegada(),
                TimeZone.getDefault());
        DateTime fim = new DateTime(viagem.getDateSaida(),TimeZone.
                getDefault());



        evento.setStart(new EventDateTime().setDateTime(inicio));
        evento.setEnd(new EventDateTime().setDateTime(fim));

        try {
            Event eventoCriado = calendar.events()
                    .insert(nomeConta, evento)
                    .execute();
            return eventoCriado.getId();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}