package it.prochilo.salvatore.trovaildecimo.models;

import java.util.ArrayList;
import java.util.List;

public class Partita {

    public String id;
    public List<User> listaPartecipanti;
    public String luogo;
    public int ora, minuti;
    public int numPartecipanti;

    public Partita(String id, int numPartecipanti) {
        this.id = id;
        listaPartecipanti = new ArrayList<>(numPartecipanti);
    }

    public Partita setLuogo(String luogo) {
        this.luogo = luogo;
        return this;
    }

    public Partita setTime(final int ora, final int minuti) {
        this.ora = ora;
        this.minuti = minuti;
        return this;
    }

    public Partita addPartecipante(User utente) {
        listaPartecipanti.add(utente);
        return this;
    }
}
