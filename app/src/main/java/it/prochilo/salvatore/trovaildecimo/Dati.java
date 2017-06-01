package it.prochilo.salvatore.trovaildecimo;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;

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
    public static List<User> exampleAmici;

    public Partita newPartita;

    private Dati() {
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

    public void setPartita(Partita partita) {
        newPartita = partita;
    }
}
