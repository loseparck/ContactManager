package com.esisa.contactmanager.models;

import java.util.Vector;

/**
 * Created by Yassine on 30/12/2014.
 */
public class Contact {
    private String id;
    private String nom;
    private Vector<String> email;
    private Vector<String> numbers;
    private String picture;

    public Contact(){

    }

    public Contact(String id, String nom, Vector<String> email, Vector<String> numbers,String picture) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.numbers = numbers;
        this.picture = picture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Vector<String> getEmail() {
        return email;
    }

    public void setEmail(Vector<String> email) {
        this.email = email;
    }

    public Vector<String> getNumbers() {
        return numbers;
    }

    public void setNumbers(Vector<String> numbers) {
        this.numbers = numbers;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id='" + id + '\'' +
                ", nom='" + nom + '\'' +
                ", email=" + email +
                ", numbers=" + numbers +
                ", picture='" + picture + '\'' +
                '}';
    }
}
