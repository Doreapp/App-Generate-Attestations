package com.mandin.antoine.attestations.generator;

import com.mandin.antoine.attestations.model.Reason;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AttestationData {
    private Date creationDate;
    private Date usingDate;
    private String lastName;
    private String firstName;
    private Date birthday;
    private String placeOfBirth;
    private String address;
    private String zipCode;
    private String city;
    private Reason reason;

    public AttestationData() {
    }

    public static AttestationData sample() {
        AttestationData data = new AttestationData();
        data.setCreationDate(new Date(System.currentTimeMillis()));
        data.setUsingDate(new Date(System.currentTimeMillis()));
        data.setFirstName("Antoine");
        data.setLastName("Mandin");
        data.setBirthday(new Date(System.currentTimeMillis()));
        data.setPlaceOfBirth("St. Herblain");
        data.setAddress("5 rue du moulin");
        data.setZipCode("44710");
        data.setCity("St. léger les vignes");
        data.setReason(Reason.achats_culturel_cultuel);
        return data;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUsingDate() {
        return usingDate;
    }

    public void setUsingDate(Date usingDate) {
        this.usingDate = usingDate;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Reason getReason() {
        return reason;
    }

    public void setReason(Reason reason) {
        this.reason = reason;
    }

    public String getBirthdayString() {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH).format(birthday);
    }

    public String getOutHour() {
        return new SimpleDateFormat("HH:mm", Locale.FRENCH).format(usingDate);
    }

    public String getOutDay() {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH).format(usingDate);
    }

    public String buildQRData() {
        final SimpleDateFormat dayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH);
        final SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm", Locale.FRENCH);
        return "Cree le: " + dayFormat.format(creationDate) + " a " + hourFormat.format(creationDate).replace(":", "h") +
                ";\n " +
                "Nom: " + lastName +
                ";\n " +
                "Prenom: " + firstName +
                ";\n " +
                "Naissance: " + dayFormat.format(birthday) + " a " + placeOfBirth +
                ";\n " +
                "Adresse: " + address + " " + zipCode + " " + city +
                ";\n " +
                "Sortie: " + dayFormat.format(usingDate) + " a " + hourFormat.format(usingDate) +
                ";\n " +
                "Motifs: " + reason.toString()
                + ";\n"; //They added that
    }
}
