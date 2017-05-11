package it.prochilo.salvatore.trovaildecimo.models;

import java.util.LinkedList;
import java.util.List;

public class User {

    public String email;
    public String name;
    public String surname;
    public int age;
    public String city;
    public String role;
    public int numGamesPlayed = 0;
    public float feedback;

    public List<User> amiciList = null;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String name, String surname) {
        this.email = email;
        this.name = name;
        this.surname = surname;
    }

    public User addProprietas(int age, String city, String role) {
        this.age = age;
        this.city = city;
        this.role = role;
        return this;
    }

    public User addFeedBack(float feedback) {
        this.feedback = feedback;
        return this;
    }

    public void aggiungiAmico(User user) {
        if (amiciList == null) {
            amiciList = new LinkedList<>();
        }
        amiciList.add(user);
    }

    public void rimuoviAmico(User user) {
        amiciList.remove(user);
    }

}
