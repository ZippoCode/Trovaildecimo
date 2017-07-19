package it.prochilo.salvatore.trovaildecimo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.prochilo.salvatore.datamodels.Match;
import it.prochilo.salvatore.datamodels.User;

public final class Dati {

    private static Dati mIstance = new Dati();

    public static User user = User.Builder.create("14587","prochilo.salvatore@gmail.com","Salvatore","Prochilo")
            .build();
    public static List<User> exampleAmici;

    public Match newPartita;

    private Dati() {
        exampleAmici = new ArrayList<>();
        /**exampleAmici.add(new User("5215151", "email1@gmail.com", "Antonio", "Condello")
                .addProprietas(18, "Taurianova", "Portiere"));
        exampleAmici.add(new User("5215151", "email2@gmail.com", "Emanuele", "Macrì")
                .addProprietas(18, "Taurianova", "Difensore"));
        exampleAmici.add(new User("5215151", "email3@gmail.com", "Francesco", "Spinelli")
                .addProprietas(18, "Taurianova", "Attaccante"));
        exampleAmici.add(new User("5215151", "email4@gmail.com", "Simone", "Marafioti")
                .addProprietas(18, "Taurianova", "Centrocampista"));*/
    }

    public void setPartita(Match partita) {
        newPartita = partita;
    }
}
