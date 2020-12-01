package com.mandin.antoine.attestations.model;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mandin.antoine.attestations.util.Utils;

import java.util.Date;
import java.util.Objects;

public class Place implements BundleProf {
    protected static final String BUNDLE_ID = "id",
            BUNDLE_ADDRESS = "address",
            BUNDLE_ZIP_CODE = "zipCode",
            BUNDLE_CITY = "city";
    private Long id;
    private String address;
    private int zipCode;
    private String city;

    public Place(Long id, String address, int zipCode, String city) {
        this.id = id;
        this.address = address;
        this.zipCode = zipCode;
        this.city = city;
    }

    public static Place fromBundle(Bundle bundle, String prefix) {
        Long id = bundle.getLong(Utils.buildBundleKey(BUNDLE_ID, prefix));
        String address = bundle.getString(Utils.buildBundleKey(BUNDLE_ADDRESS, prefix));
        String city = bundle.getString(Utils.buildBundleKey(BUNDLE_CITY, prefix));
        int zipCode = bundle.getInt(Utils.buildBundleKey(BUNDLE_ZIP_CODE, prefix));
        return new Place(id, address, zipCode, city);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
        return "Place{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", zipCode=" + zipCode +
                ", city='" + city + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass() || id == null) return false;
        Place that = (Place) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public void populateBundle(Bundle bundle, @Nullable String prefix) {
        bundle.putLong(Utils.buildBundleKey(BUNDLE_ID, prefix), id);
        bundle.putString(Utils.buildBundleKey(BUNDLE_ADDRESS, prefix), address);
        bundle.putString(Utils.buildBundleKey(BUNDLE_CITY, prefix), city);
        bundle.putInt(Utils.buildBundleKey(BUNDLE_ZIP_CODE, prefix), zipCode);
    }
}
