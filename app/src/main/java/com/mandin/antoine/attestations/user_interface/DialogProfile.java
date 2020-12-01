package com.mandin.antoine.attestations.user_interface;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mandin.antoine.attestations.R;
import com.mandin.antoine.attestations.data_access.Service;
import com.mandin.antoine.attestations.model.Profile;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DialogProfile extends Dialog {
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etBirthday;
    private EditText etPlaceOfBirth;
    private Profile profile;

    public DialogProfile(@NonNull Context context, @Nullable Profile profile) {
        super(context);
        setContentView(R.layout.dialog_profile);
        this.profile = profile;

        initButtons();
        initFields();
    }

    private void initButtons() {
        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        findViewById(R.id.btnValidate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleValidate();
            }
        });
    }

    private void initFields() {
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etBirthday = findViewById(R.id.etBirthday);
        etPlaceOfBirth = findViewById(R.id.etPlaceOfBirth);

        if (profile != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH);
            etFirstName.setText(profile.getFirstName());
            etLastName.setText(profile.getLastName());
            etPlaceOfBirth.setText(profile.getPlaceOfBirth());
            etBirthday.setText(dateFormat.format(profile.getBirthday()));
        }
    }

    private void handleValidate() {
        String firstName = etFirstName.getText().toString();
        if (firstName.trim().isEmpty()) {
            alertError("Veuillez entrer un prénom");
            return;
        }

        String lastName = etLastName.getText().toString();
        if (lastName.trim().isEmpty()) {
            alertError("Veuillez entrer un nom");
            return;
        }

        String birthdayStr = etBirthday.getText().toString();
        if (!birthdayStr.matches("^[0-3][0-9]\\/[0-1][0-9]\\/[12][09][0-9][0-9]$")) {
            alertError("Veuillez entrer une date de naissance correcte");
            return;
        }
        int day = Integer.parseInt(birthdayStr.substring(0, 2));
        int month = Integer.parseInt(birthdayStr.substring(3, 5));
        int year = Integer.parseInt(birthdayStr.substring(6));
        if (day < 0 || day > 31 || month <= 0 || month > 12) {
            alertError("Veuillez entrer une date de naissance correcte");
            return;
        }

        String placeOfBirth = etPlaceOfBirth.getText().toString();
        if (placeOfBirth.trim().isEmpty()) {
            alertError("Veuillez entrer un lieu de naissance");
            return;
        }

        Calendar birthday = Calendar.getInstance();
        birthday.set(Calendar.YEAR, year);
        birthday.set(Calendar.MONTH, month-1);
        birthday.set(Calendar.DAY_OF_MONTH, day);
        birthday.set(Calendar.HOUR_OF_DAY, 0);
        birthday.set(Calendar.MINUTE, 0);
        birthday.set(Calendar.SECOND, 0);

        if (profile != null) {
            profile.setFirstName(firstName.trim());
            profile.setLastName(lastName.trim());
            profile.setBirthday(birthday.getTime());
            profile.setPlaceOfBirth(placeOfBirth.trim());
            new Service(getContext())
                    .updateProfile(profile);
        } else {
            profile = new Profile(
                    null,
                    firstName.trim(),
                    lastName.trim(),
                    birthday.getTime(),
                    placeOfBirth.trim()
            );
            new Service(getContext())
                    .addProfile(profile);
        }

        dismiss();
    }

    private void alertError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
