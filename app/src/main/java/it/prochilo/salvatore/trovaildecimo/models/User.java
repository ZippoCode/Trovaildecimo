package it.prochilo.salvatore.trovaildecimo.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class User {

    private String email;
    public String name;
    public String surname;
    public int age;
    public String city;
    public String role;
    public int numGamesPlayed = 0;
    public float feedback;
    public List<User> amiciList = null;

    public User() {
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

    public static User fromJson(final JSONObject jsonObject) throws JSONException {
        final String email = jsonObject.getString("email");
        final String nome = jsonObject.getString("nome");
        final String cognome = jsonObject.getString("cognome");
        final int age = jsonObject.getInt("age");
        final String city = jsonObject.getString("city");
        final String role = jsonObject.getString("role");
        return new User(email, nome, cognome)
                .addProprietas(age, city, role);
    }

    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", email);
        jsonObject.put("nome", name);
        jsonObject.put("cognome", surname);
        jsonObject.put("age", age);
        jsonObject.put("city", city);
        jsonObject.put("role", role);
        return jsonObject;
    }

}
