package com.mandin.antoine.attestations.model;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mandin.antoine.attestations.util.Utils;

import java.util.Date;

public class AdaptiveAttestation extends Attestation {
    protected static final String BUNDLE_DELTA_IN_MINUTE = "deltaInMinute",
            BUNDLE_PLACE = "place";
    private int deltaInMinute;
    private Place place;

    public AdaptiveAttestation(Long id, Date realCreationDate, Profile profile, Place place, Reason reason, int deltaInMinute) {
        super(id, realCreationDate, profile, reason);
        this.place = place;
        this.deltaInMinute = deltaInMinute;
    }

    public static AdaptiveAttestation fromBundle(Bundle bundle, String prefix, Long id, Date realCreationDate, Profile profile, Reason reason){
        int deltaInMinute = bundle.getInt(Utils.buildBundleKey(BUNDLE_DELTA_IN_MINUTE, prefix));

        Place place = Place.fromBundle(bundle, Utils.buildBundleKey(BUNDLE_PLACE, prefix));

        return new AdaptiveAttestation(id,realCreationDate, profile, place, reason, deltaInMinute);
    }

    @Override
    public Date getCreationDate() {
        long time = System.currentTimeMillis() - (deltaInMinute + 1) * 60L * 1000L;
        return new Date(time);
    }

    public Date getUsingDate() {
        long time = System.currentTimeMillis() - (deltaInMinute ) * 60L * 1000L;
        return new Date(time);
    }

    public Place getPlace() {
        return place;
    }

    @Override
    public void populateBundle(Bundle bundle, @Nullable String prefix) {
        super.populateBundle(bundle, prefix);
        bundle.putInt(Utils.buildBundleKey(BUNDLE_DELTA_IN_MINUTE, prefix), deltaInMinute);
        bundle.putString(Utils.buildBundleKey(BUNDLE_TYPE, prefix), "AdaptiveAttestation");
        place.populateBundle(bundle, Utils.buildBundleKey(BUNDLE_PLACE, prefix));
    }

    @Override
    @NonNull
    public String toString() {
        return "AdaptiveAttestation{" +
                "id=" + getId() +
                ", deltaInMinute=" + deltaInMinute +
                ", realCreationDate=" + realCreationDate +
                ", profile=" + profile +
                ", place=" + place +
                ", reason=" + reason +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass() || getId() == null) return false;
        AdaptiveAttestation that = (AdaptiveAttestation) o;
        return getId().equals(that.getId());
    }
}
