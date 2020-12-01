package com.mandin.antoine.attestations.model;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mandin.antoine.attestations.util.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

//TODO abstract
public abstract class Attestation implements BundleProf {
    protected static final String BUNDLE_ID = "id", BUNDLE_REAL_CREATION_DATE = "realCreationDate",
            BUNDLE_PROFILE = "profile", BUNDLE_REASON = "reason",
            BUNDLE_TYPE = "type";
    protected Date realCreationDate;
    protected Profile profile;
    protected Reason reason;
    private Long id;

    protected Attestation(Long id, Date realCreationDate, Profile profile, Reason reason) {
        this.id = id;
        this.realCreationDate = realCreationDate;
        this.profile = profile;
        this.reason = reason;
    }

    public static Attestation fromBundle(Bundle bundle, String prefix) {
        Long id = bundle.getLong(Utils.buildBundleKey(BUNDLE_ID, prefix), -1);
        if(id == -1)
            id = null;
        long realCreationTime = bundle.getLong(Utils.buildBundleKey(BUNDLE_REAL_CREATION_DATE, prefix));

        int reasonOrdinal = bundle.getInt(Utils.buildBundleKey(BUNDLE_REASON, prefix));

        Profile profile = Profile.fromBundle(bundle, BUNDLE_PROFILE);

        String type = bundle.getString(Utils.buildBundleKey(BUNDLE_TYPE, prefix));
        switch (type) {
            case "AdaptiveAttestation":
                return AdaptiveAttestation.fromBundle(bundle, prefix, id,
                        new Date(realCreationTime), profile, Reason.values()[reasonOrdinal]);
            case "StaticAttestation":
                return StaticAttestation.fromBundle(bundle, prefix, id,
                        new Date(realCreationTime), profile, Reason.values()[reasonOrdinal]);
        }
        return null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public abstract Date getCreationDate();

    public abstract Date getUsingDate();

    public Date getRealCreationDate() {
        return realCreationDate;
    }

    public Profile getProfile() {
        return profile;
    }

    public abstract Place getPlace();

    public Reason getReason() {
        return reason;
    }

    public String getBirthdayString() {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH).format(profile.getBirthday());
    }

    public String getUsingDay() {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH).format(getUsingDate());
    }

    public String getUsingHour() {
        return new SimpleDateFormat("HH:mm", Locale.FRENCH).format(getUsingDate());
    }

    public String buildQRData() {
        final SimpleDateFormat dayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH);
        final SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm", Locale.FRENCH);
        return "Cree le: " + dayFormat.format(getCreationDate()) +
                " a " + hourFormat.format(getCreationDate()).replace(":", "h") +
                ";\n " +
                "Nom: " + profile.getLastName() +
                ";\n " +
                "Prenom: " + profile.getFirstName() +
                ";\n " +
                "Naissance: " + dayFormat.format(profile.getBirthday()) + " a " + profile.getPlaceOfBirth() +
                ";\n " +
                "Adresse: " + getPlace().getAddress() + " " + getPlace().getZipCode() + " " + getPlace().getCity() +
                ";\n " +
                "Sortie: " + dayFormat.format(getUsingDate()) + " a " + hourFormat.format(getUsingDate()) +
                ";\n " +
                "Motifs: " + reason.toString();
    }


    @Override
    public void populateBundle(Bundle bundle, @Nullable String prefix) {
        if (id != null)
            bundle.putLong(Utils.buildBundleKey(BUNDLE_ID, prefix), id);
        bundle.putLong(Utils.buildBundleKey(BUNDLE_REAL_CREATION_DATE, prefix), realCreationDate.getTime());
        bundle.putInt(Utils.buildBundleKey(BUNDLE_REASON, prefix), reason.ordinal());
        profile.populateBundle(bundle, Utils.buildBundleKey(BUNDLE_PROFILE, prefix));
    }

    @Override
    @NonNull
    public String toString() {
        return "Attestation{" +
                "id=" + id +
                ", realCreationDate=" + realCreationDate +
                ", profile=" + profile +
                ", reason=" + reason +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass() || id == null) return false;
        Attestation that = (Attestation) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
