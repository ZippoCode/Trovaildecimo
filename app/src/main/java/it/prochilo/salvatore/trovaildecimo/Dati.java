package it.prochilo.salvatore.trovaildecimo;

import it.prochilo.salvatore.trovaildecimo.models.Partita;
import it.prochilo.salvatore.trovaildecimo.models.User;

public final class Dati {

    private static Dati mIstance = new Dati();

    public static User user;
    public static Partita partita;

    private Dati() {
        user = new User("prochilo.salvatore@gmail.com", "Salvatore", "Prochilo")
                .addProprietas(24, "Taurianova", "Attaccante");
        for (int i = 0; i < 25; i++) {
            User amico = new User("prova", "Nome #" + i, "Cognome #" + i)
                    .addProprietas(i, "City #" + i, "Attaccante")
                    .addFeedBack(5);
            user.aggiungiAmico(amico);
        }

        partita = new Partita("1dsf6a", user)
                .setGiorno(15, 4, 2017)
                .setTime(15, 47)
                .setTipologia(Partita.TipoIncontro.NORMALE)
                .setMinutaggio(60)
                .setNomeCampo("Americano")
                .setNumeroPartecipanti(12);
    }

}
