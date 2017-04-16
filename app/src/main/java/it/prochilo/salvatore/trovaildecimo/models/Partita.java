package it.prochilo.salvatore.trovaildecimo.models;

import java.util.ArrayList;
import java.util.List;

public class Partita {

    public String id;
    public List<User> listaPartecipanti;
    public String luogo;
    public String orario;
    public int numPartecipanti;

    public Partita(String id, String luogo, String orario, int numPartecipanti) {
        this.id = id;
        listaPartecipanti = new ArrayList<>(numPartecipanti);
        this.luogo = luogo;
        this.orario = orario;
    }

    public Partita addPartecipante(User utente) {
        listaPartecipanti.add(utente);
        return this;
    }
}
