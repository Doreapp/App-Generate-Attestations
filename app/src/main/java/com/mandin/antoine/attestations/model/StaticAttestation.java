package com.mandin.antoine.attestations.model;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mandin.antoine.attestations.util.Utils;

import java.util.Date;

public class StaticAttestation extends Attestation {
    protected static final String BUNDLE_CREATION_DATE = "creationDate",
            BUNDLE_USING_DATE = "usingDate",
            BUNDLE_PLACE = "place";
    private Date creationDate;
    private Date usingDate;
    private Place place;

    public StaticAttestation(Long id, Date creationDate, Date usingDate, Date realCreationDate, Profile profile, Place place, Reason reason) {
        super(id, realCreationDate, profile, reason);
        this.creationDate = creationDate;
        this.usingDate = usingDate;
        this.place = place;
    }

    public static StaticAttestation fromBundle(Bundle bundle, String prefix, Long id, Date realCreationDate, Profile profile, Reason reason) {
        long creationTime = bundle.getLong(Utils.buildBundleKey(BUNDLE_CREATION_DATE, prefix));
        long usingTime = bundle.getLong(Utils.buildBundleKey(BUNDLE_USING_DATE, prefix));

        Place place = Place.fromBundle(bundle, Utils.buildBundleKey(BUNDLE_PLACE, prefix));

        return new StaticAttestation(id, new Date(creationTime), new Date(usingTime), realCreationDate, profile, place, reason);
    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public Date getUsingDate() {
        return usingDate;
    }

    @Override
    public Place getPlace() {
        return place;
    }

    @Override
    public void populateBundle(Bundle bundle, @Nullable String prefix) {
        super.populateBundle(bundle, prefix);

        bundle.putLong(Utils.buildBundleKey(BUNDLE_CREATION_DATE, prefix), creationDate.getTime());
        bundle.putLong(Utils.buildBundleKey(BUNDLE_USING_DATE, prefix), usingDate.getTime());
        bundle.putString(Utils.buildBundleKey(BUNDLE_TYPE, prefix), "StaticAttestation");
        place.populateBundle(bundle, Utils.buildBundleKey(BUNDLE_PLACE, prefix));
    }

    @Override
    @NonNull
    public String toString() {
        return "StaticAttestation{" +
                "id=" + getId() +
                ", creationDate=" + creationDate +
                ", usingDate=" + usingDate +
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
        StaticAttestation that = (StaticAttestation) o;
        return getId().equals(that.getId());
    }
}
