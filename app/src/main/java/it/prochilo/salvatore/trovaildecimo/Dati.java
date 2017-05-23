package it.prochilo.salvatore.trovaildecimo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import it.prochilo.salvatore.trovaildecimo.models.Data;
import it.prochilo.salvatore.trovaildecimo.models.Message;
import it.prochilo.salvatore.trovaildecimo.models.Time;
import it.prochilo.salvatore.trovaildecimo.models.Partita;
import it.prochilo.salvatore.trovaildecimo.models.User;

public final class Dati {

    private static Dati mIstance = new Dati();

    public static User user;
    public static Partita partita;
    public static List<Message> exampleMessage;
    public static List<User> exampleAmici;

    public Partita newPartita;

    private Dati() {
        user = new User("145872", "prochilo.salvatore@gmail.com", "Salvatore", "Prochilo")
                .addProprietas(24, "Taurianova", "Attaccante");
        for (int i = 0; i < 25; i++) {
            User amico = new User("584829" + i, "prova", "Nome #" + i, "Cognome #" + i)
                    .addProprietas(i, "City #" + i, "Attaccante")
                    .addFeedBack(5);
            user.addFriend(amico);
        }

        partita = new Partita("1dsf6a", user)
                .setGiorno(new Data(15, 4, 2017))
                .setTime(new Time(15, 47))
                .setTipologia("Normale")
                .setMinutaggio(60)
                .setNomeCampo("Americano")
                .setNumeroPartecipanti(12);

        //

        exampleMessage = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            exampleMessage.add(new Message(user, "Questo è un messaggio di prova, ed è il numero #" + i));
        }

        exampleAmici = new ArrayList<>();
        exampleAmici.add(new User("5215151", "email1@gmail.com", "Antonio", "Condello")
                .addProprietas(18, "Taurianova", "Portiere"));
        exampleAmici.add(new User("5215151", "email2@gmail.com", "Emanuele", "Macrì")
                .addProprietas(18, "Taurianova", "Difensore"));
        exampleAmici.add(new User("5215151", "email3@gmail.com", "Francesco", "Spinelli")
                .addProprietas(18, "Taurianova", "Attaccante"));
        exampleAmici.add(new User("5215151", "email4@gmail.com", "Simone", "Marafioti")
                .addProprietas(18, "Taurianova", "Centrocampista"));
    }

    public static Partita createNuovaPartita() {
        return new Partita(String.valueOf(new Random().nextInt(Integer.MAX_VALUE)),
                Dati.user);
    }

    public static Dati getInstance(){
        return mIstance;
    }

    public void setPartita(Partita partita) {
        newPartita = partita;
    }

}
