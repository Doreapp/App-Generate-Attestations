package com.mandin.antoine.attestations.model;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mandin.antoine.attestations.util.Utils;

import java.util.Date;
import java.util.Objects;

public class Profile implements BundleProf {
    protected static final String BUNDLE_ID = "id",
            BUNDLE_FIRST_NAME = "firstName",
            BUNDLE_LAST_NAME = "lastName",
            BUNDLE_BIRTHDAY = "birthday",
            BUNDLE_PLACE_OF_BIRTH = "placeOfBirth";
    private Long id;
    private String firstName;
    private String lastName;
    private Date birthday;
    private String placeOfBirth;

    public Profile(Long id, String firstName, String lastName, Date birthday, String placeOfBirth) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.placeOfBirth = placeOfBirth;
    }

    public static Profile fromBundle(Bundle bundle, String prefix) {
        Long id = bundle.getLong(Utils.buildBundleKey(BUNDLE_ID, prefix));
        String firstName = bundle.getString(Utils.buildBundleKey(BUNDLE_FIRST_NAME, prefix));
        String lastName = bundle.getString(Utils.buildBundleKey(BUNDLE_LAST_NAME, prefix));
        long birthdayTime = bundle.getLong(Utils.buildBundleKey(BUNDLE_BIRTHDAY, prefix));
        String placeOfBirth = bundle.getString(Utils.buildBundleKey(BUNDLE_PLACE_OF_BIRTH, prefix));
        return new Profile(id, firstName, lastName, new Date(birthdayTime), placeOfBirth);
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    @NonNull
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthday=" + birthday +
                ", placeOfBirth='" + placeOfBirth + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass() || id == null) return false;
        Profile that = (Profile) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public void populateBundle(Bundle bundle, @Nullable String prefix) {
        bundle.putLong(Utils.buildBundleKey(BUNDLE_ID, prefix), id);
        bundle.putString(Utils.buildBundleKey(BUNDLE_FIRST_NAME, prefix), firstName);
        bundle.putString(Utils.buildBundleKey(BUNDLE_LAST_NAME, prefix), lastName);
        bundle.putLong(Utils.buildBundleKey(BUNDLE_BIRTHDAY, prefix), birthday.getTime());
        bundle.putString(Utils.buildBundleKey(BUNDLE_PLACE_OF_BIRTH, prefix), placeOfBirth);
    }
}
