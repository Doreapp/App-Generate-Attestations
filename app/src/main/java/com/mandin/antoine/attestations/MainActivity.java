package com.mandin.antoine.attestations;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mandin.antoine.attestations.data_access.Service;
import com.mandin.antoine.attestations.model.AdaptiveAttestation;
import com.mandin.antoine.attestations.model.Attestation;
import com.mandin.antoine.attestations.model.Place;
import com.mandin.antoine.attestations.model.Profile;
import com.mandin.antoine.attestations.model.Reason;
import com.mandin.antoine.attestations.model.StaticAttestation;
import com.mandin.antoine.attestations.user_interface.DialogPlace;
import com.mandin.antoine.attestations.user_interface.DialogProfile;
import com.mandin.antoine.attestations.user_interface.HourPicker;
import com.mandin.antoine.attestations.user_interface.PlacesRadioGroup;
import com.mandin.antoine.attestations.user_interface.ReasonRadioGroup;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DialogPlace.OnValidateListener {
    private HourPicker usingDatePicker, creationDatePicker;
    private PlacesRadioGroup placesRadioGroup;
    private ReasonRadioGroup reasonRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_generate);

        findViewById(R.id.btnDefineProfile).setOnClickListener(this);
        findViewById(R.id.btnSeeAttestations).setOnClickListener(this);
        findViewById(R.id.btn_new_place).setOnClickListener(this);
        View btnGenerate = findViewById(R.id.btn_generate);
        btnGenerate.setOnClickListener(this);
        btnGenerate.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                createAdaptiveAttestation();
                return true;
            }
        });

        usingDatePicker = new HourPicker(findViewById(R.id.usingDateSelector));
        creationDatePicker = new HourPicker(findViewById(R.id.createDateSelector));

        placesRadioGroup = new PlacesRadioGroup((RadioGroup)
                findViewById(R.id.place_selector));
        reasonRadioGroup = new ReasonRadioGroup((RadioGroup)
                findViewById(R.id.reason_selector));

        if (new Service(this).getMainProfile() == null) {
            new DialogProfile(this, null).show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDefineProfile:
                new DialogProfile(this, new Service(this).getMainProfile())
                        .show();
                break;
            case R.id.btnSeeAttestations:
                navigateToAttestations();
                break;
            case R.id.btn_generate:
                generateAttestation();
                break;
            case R.id.btn_new_place:
                new DialogPlace(this, null)
                        .setOnValidateListener(this)
                        .show();
                break;
        }
    }

    private void createAdaptiveAttestation() {
        Profile profile = new Service(this).getMainProfile();
        if (profile == null) {
            Toast.makeText(this, "Veuillez spécifier votre profil.", Toast.LENGTH_SHORT).show();
            return;
        }

        Place place = placesRadioGroup.getSelectedPlace();
        if (place == null) {
            Toast.makeText(this, "Veuillez sélectionner un lieu de confinement.", Toast.LENGTH_SHORT).show();
            return;
        }
        Reason reason = Reason.sport_animaux;
        int delta = 21;//minutes

        final Attestation attestation = new AdaptiveAttestation(
                null,
                new Date(System.currentTimeMillis()),
                profile,
                place,
                reason,
                delta
        );

        Intent toAttestation = new Intent(MainActivity.this, WaitingActivity.class);
        Bundle bundle = new Bundle();
        attestation.populateBundle(bundle, null);
        toAttestation.putExtra(WaitingActivity.EXTRA_ATTESTATION, bundle);
        startActivity(toAttestation);
    }

    public void generateAttestation() {
        Profile profile = new Service(this).getMainProfile();
        if (profile == null) {
            Toast.makeText(this, "Veuillez spécifier votre profil.", Toast.LENGTH_SHORT).show();
            return;
        }
        Date usingDate = usingDatePicker.getDate();
        Date creationDate = creationDatePicker.getDate();
        Date realCreationDate = new Date(System.currentTimeMillis());

        Place place = placesRadioGroup.getSelectedPlace();
        if (place == null) {
            Toast.makeText(this, "Veuillez sélectionner un lieu de confinement.", Toast.LENGTH_SHORT).show();
            return;
        }
        Reason reason = reasonRadioGroup.getSelectedReason();
        if (reason == null) {
            Toast.makeText(this, "Veuillez sélectionner un motif.", Toast.LENGTH_SHORT).show();
            return;
        }

        Attestation attestation = new StaticAttestation(
                null,
                creationDate,
                usingDate,
                realCreationDate,
                profile,
                place,
                reason
        );

        new Service(this)
                .addAttestation(attestation);

        navigateToAttestations();
    }

    public void navigateToAttestations() {
        Intent attestationsIntent = new Intent(this, AttestationsActivity.class);
        startActivity(attestationsIntent);
    }

    @Override
    public void onValidate(Place place) {
        placesRadioGroup.reloadContent();
    }

}