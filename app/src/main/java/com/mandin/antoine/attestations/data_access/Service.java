package com.mandin.antoine.attestations.data_access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mandin.antoine.attestations.model.Attestation;
import com.mandin.antoine.attestations.model.Place;
import com.mandin.antoine.attestations.model.Profile;
import com.mandin.antoine.attestations.model.Reason;
import com.mandin.antoine.attestations.model.StaticAttestation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Service {
    private static final String TAG = "Service";
    private Context context;
    private Helper helper;
    private boolean oneTimeUse;

    public Service(Context context) {
        this(context, false);
    }

    public Service(Context context, boolean oneTimeUse) {

        if (context == null) {
            throw new NullPointerException("Context must not be null.");
        }
        this.oneTimeUse = oneTimeUse;
        this.context = context;
        helper = new Helper(context);
    }

    private void closeIfOneUse() {
        if (oneTimeUse)
            close();
    }

    public void close() {
        helper.close();
    }

    public void addPlace(Place place) {
        Log.i(TAG, "add Place : " + place);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Schema.Place.ZIP_CODE, place.getZipCode());
        values.put(Schema.Place.ADDRESS, place.getAddress());
        values.put(Schema.Place.CITY, place.getCity());

        long id = db.insert(
                Schema.Place.TABLE_NAME,
                null,
                values
        );

        place.setId(id);
        closeIfOneUse();
    }

    public void addAttestation(Attestation attestation) {
        Log.i(TAG, "add attestation : " + attestation);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Schema.Attestation.CREATION_DATE, attestation.getCreationDate().getTime());
        values.put(Schema.Attestation.REAL_CREATION_DATE, attestation.getRealCreationDate().getTime());
        values.put(Schema.Attestation.USING_DATE, attestation.getUsingDate().getTime());
        values.put(Schema.Attestation.PLACE, attestation.getPlace().getId());
        values.put(Schema.Attestation.PROFILE, attestation.getProfile().getId());
        values.put(Schema.Attestation.REASON, attestation.getReason().ordinal());

        long id = db.insert(
                Schema.Attestation.TABLE_NAME,
                null,
                values
        );

        attestation.setId(id);
        closeIfOneUse();
    }

    public void addProfile(Profile profile) {
        Log.i(TAG, "add profile : " + profile);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Schema.Profile.FIRST_NAME, profile.getFirstName());
        values.put(Schema.Profile.LAST_NAME, profile.getLastName());
        values.put(Schema.Profile.BIRTHDAY, profile.getBirthday().getTime());
        values.put(Schema.Profile.PLACE_OF_BIRTH, profile.getPlaceOfBirth());

        long id = db.insert(
                Schema.Profile.TABLE_NAME,
                null,
                values
        );

        profile.setId(id);
        closeIfOneUse();
    }

    public void updateProfile(Profile profile) {
        Log.i(TAG, "updateProfile : " + profile);
        if (profile.getId() == null) {
            addProfile(profile);
            return;
        }

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Schema.Profile.FIRST_NAME, profile.getFirstName());
        values.put(Schema.Profile.LAST_NAME, profile.getLastName());
        values.put(Schema.Profile.BIRTHDAY, profile.getBirthday().getTime());
        values.put(Schema.Profile.PLACE_OF_BIRTH, profile.getPlaceOfBirth());

        db.update(
                Schema.Profile.TABLE_NAME,
                values,
                Schema.Profile._ID + " = ?",
                new String[]{"" + profile.getId()}
        );
        closeIfOneUse();
    }

    public void updatePlace(Place place) {
        Log.i(TAG, "updatePlace : " + place);
        if (place.getId() == null) {
            addPlace(place);
            return;
        }

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Schema.Place.CITY, place.getCity());
        values.put(Schema.Place.ADDRESS, place.getAddress());
        values.put(Schema.Place.ZIP_CODE, place.getZipCode());

        db.update(
                Schema.Place.TABLE_NAME,
                values,
                Schema.Place._ID + " = ?",
                new String[]{"" + place.getId()}
        );
        closeIfOneUse();
    }

    public Profile getMainProfile() {
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Schema.Profile.TABLE_NAME,
                null);

        Profile result = null;
        if (cursor.moveToFirst()) {
            long id = cursor.getLong(
                    cursor.getColumnIndexOrThrow(Schema.Profile._ID));
            String lastName = cursor.getString(
                    cursor.getColumnIndexOrThrow(Schema.Profile.LAST_NAME));
            String firstName = cursor.getString(
                    cursor.getColumnIndexOrThrow(Schema.Profile.FIRST_NAME));
            String placeOfBirth = cursor.getString(
                    cursor.getColumnIndexOrThrow(Schema.Profile.PLACE_OF_BIRTH));
            long birthdayTime = cursor.getLong(
                    cursor.getColumnIndexOrThrow(Schema.Profile.BIRTHDAY));
            result = new Profile(id, firstName, lastName, new Date(birthdayTime), placeOfBirth);
        }

        Log.i(TAG, "getMainProfile : " + result);

        cursor.close();
        closeIfOneUse();
        return result;
    }

    public Profile getProfileById(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Schema.Profile.TABLE_NAME +
                        " WHERE " + Schema.Profile._ID + " = ?",
                new String[]{"" + id});

        Profile result = null;
        if (cursor.moveToFirst()) {
            String lastName = cursor.getString(
                    cursor.getColumnIndexOrThrow(Schema.Profile.LAST_NAME));
            String firstName = cursor.getString(
                    cursor.getColumnIndexOrThrow(Schema.Profile.FIRST_NAME));
            String placeOfBirth = cursor.getString(
                    cursor.getColumnIndexOrThrow(Schema.Profile.PLACE_OF_BIRTH));
            long birthdayTime = cursor.getLong(
                    cursor.getColumnIndexOrThrow(Schema.Profile.BIRTHDAY));
            result = new Profile(id, firstName, lastName, new Date(birthdayTime), placeOfBirth);
        }

        Log.i(TAG, "getProfileById : " + result);

        cursor.close();
        closeIfOneUse();
        return result;
    }

    public Place getPlaceById(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Schema.Place.TABLE_NAME +
                        " WHERE " + Schema.Place._ID + " = ?",
                new String[]{"" + id});

        Place result = null;
        if (cursor.moveToFirst()) {
            String address = cursor.getString(
                    cursor.getColumnIndexOrThrow(Schema.Place.ADDRESS));
            String city = cursor.getString(
                    cursor.getColumnIndexOrThrow(Schema.Place.CITY));
            int zipCode = cursor.getInt(
                    cursor.getColumnIndexOrThrow(Schema.Place.ZIP_CODE));
            result = new Place(id, address, zipCode, city);
        }

        Log.i(TAG, "getPlaceById : " + result);

        cursor.close();
        closeIfOneUse();
        return result;
    }

    public List<Attestation> getAttestations() {
        SQLiteDatabase db = helper.getReadableDatabase();

        final boolean actualOneUse = oneTimeUse;
        oneTimeUse = false;

        Cursor cursor = db.rawQuery("SELECT * FROM " + Schema.Attestation.TABLE_NAME, null);

        List<Attestation> result = null;
        if (cursor.moveToFirst()) {
            result = new ArrayList<>();
            do {
                long createTime = cursor.getLong(
                        cursor.getColumnIndexOrThrow(Schema.Attestation.CREATION_DATE));
                long usingTime = cursor.getLong(
                        cursor.getColumnIndexOrThrow(Schema.Attestation.USING_DATE));
                long realCreationTime = cursor.getLong(
                        cursor.getColumnIndexOrThrow(Schema.Attestation.REAL_CREATION_DATE));
                long profileId = cursor.getLong(
                        cursor.getColumnIndexOrThrow(Schema.Attestation.PROFILE));
                long placeId = cursor.getLong(
                        cursor.getColumnIndexOrThrow(Schema.Attestation.PLACE));
                long id = cursor.getLong(
                        cursor.getColumnIndexOrThrow(Schema.Attestation._ID));
                int reasonId = cursor.getInt(
                        cursor.getColumnIndexOrThrow(Schema.Attestation.REASON));
                Profile profile = getProfileById(profileId);
                Place place = getPlaceById(placeId);
                Reason reason = Reason.values()[reasonId];
                result.add(new StaticAttestation(
                        id, new Date(createTime),
                        new Date(usingTime),
                        new Date(realCreationTime),
                        profile,
                        place,
                        reason
                ));
            } while (cursor.moveToNext());
            Log.i(TAG, "getAttestations : " + Arrays.toString(result.toArray(new Attestation[]{})));
        }
        oneTimeUse = actualOneUse;

        Log.i(TAG, "getAttestations (anyway) : " + result);

        cursor.close();
        closeIfOneUse();
        return result;
    }

    public Attestation getAttestationById(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        final boolean actualOneUse = oneTimeUse;
        oneTimeUse = false;

        Cursor cursor = db.rawQuery("SELECT * FROM " + Schema.Attestation.TABLE_NAME +
                        " WHERE " + Schema.Attestation._ID + " = ?",
                new String[]{"" + id});

        Attestation result = null;
        if (cursor.moveToFirst()) {
            long createTime = cursor.getLong(
                    cursor.getColumnIndexOrThrow(Schema.Attestation.CREATION_DATE));
            long usingTime = cursor.getLong(
                    cursor.getColumnIndexOrThrow(Schema.Attestation.USING_DATE));
            long realCreationTime = cursor.getLong(
                    cursor.getColumnIndexOrThrow(Schema.Attestation.REAL_CREATION_DATE));
            long profileId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(Schema.Attestation.PROFILE));
            long placeId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(Schema.Attestation.PLACE));
            int reasonId = cursor.getInt(
                    cursor.getColumnIndexOrThrow(Schema.Attestation.REASON));
            Profile profile = getProfileById(profileId);
            Place place = getPlaceById(placeId);
            Reason reason = Reason.values()[reasonId];
            result = new StaticAttestation(
                    id, new Date(createTime),
                    new Date(usingTime),
                    new Date(realCreationTime),
                    profile,
                    place,
                    reason
            );
        }
        oneTimeUse = actualOneUse;

        Log.i(TAG, "getAttestationById (anyway) : " + result);

        cursor.close();
        closeIfOneUse();
        return result;
    }

    public List<Place> getPlaces() {
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Schema.Place.TABLE_NAME, null);

        List<Place> result = null;
        if (cursor.moveToFirst()) {
            result = new ArrayList<>();
            do {
                String address = cursor.getString(
                        cursor.getColumnIndexOrThrow(Schema.Place.ADDRESS));
                String city = cursor.getString(
                        cursor.getColumnIndexOrThrow(Schema.Place.CITY));
                int zipCode = cursor.getInt(
                        cursor.getColumnIndexOrThrow(Schema.Place.ZIP_CODE));
                long id = cursor.getLong(
                        cursor.getColumnIndexOrThrow(Schema.Place._ID));
                result.add(new Place(id, address, zipCode, city));
            } while (cursor.moveToNext());
            Log.i(TAG, "getPlaces : " + Arrays.toString(result.toArray(new Place[]{})));
        }
        Log.i(TAG, "getPlaces (anyway) : " + result);

        cursor.close();
        closeIfOneUse();
        return result;
    }

    public void removeAttestation(long id) {
        SQLiteDatabase db = helper.getWritableDatabase();

        db.delete(
                Schema.Attestation.TABLE_NAME,
                Schema.Attestation._ID + " = ?",
                new String[]{"" + id}
        );

        closeIfOneUse();
    }

    public void removeAttestation(Attestation attestation) {
        removeAttestation(attestation.getId());
    }
}
