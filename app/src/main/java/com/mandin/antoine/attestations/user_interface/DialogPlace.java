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
import com.mandin.antoine.attestations.model.Place;

public class DialogPlace extends Dialog {
    private EditText etAddress, etZipCode, etCity;
    private Place place;
    private OnValidateListener onValidateListener;

    public DialogPlace(@NonNull Context context, @Nullable Place place) {
        super(context);
        setContentView(R.layout.dialog_new_place);
        this.place = place;

        initButtons();
        initFields();
    }

    public DialogPlace setOnValidateListener(OnValidateListener onValidateListener) {
        this.onValidateListener = onValidateListener;
        return this;
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
        etAddress = findViewById(R.id.etAddress);
        etZipCode = findViewById(R.id.etZipCode);
        etCity = findViewById(R.id.etCity);

        if (place != null) {
            etAddress.setText(place.getAddress());
            etZipCode.setText("" + place.getZipCode());
            etCity.setText(place.getCity());
        }
    }

    private void handleValidate() {
        String address = etAddress.getText().toString();
        if (address.trim().isEmpty()) {
            alertError("Veuillez entrer une adresse");
            return;
        }

        String zipCodeStr = etZipCode.getText().toString();
        if (zipCodeStr.trim().isEmpty()) {
            alertError("Veuillez entrer un code postal");
            return;
        }
        int zipCode = -1;
        try {
            zipCode = Integer.parseInt(zipCodeStr);
        } catch (NumberFormatException e) {
            alertError("Veuillez entrer un code postal correct");
            return;
        }
        if (zipCode < 1000 || zipCode > 99999) {
            alertError("Veuillez entrer un code postal correct");
            return;
        }

        String city = etCity.getText().toString();
        if (city.trim().isEmpty()) {
            alertError("Veuillez entrer une ville");
            return;
        }

        if (place != null) {
            place.setAddress(address.trim());
            place.setZipCode(zipCode);
            place.setCity(city.trim());
            new Service(getContext())
                    .updatePlace(place);
        } else {
            place = new Place(
                    null,
                    address.trim(),
                    zipCode,
                    city.trim()
            );
            new Service(getContext())
                    .addPlace(place);
        }

        if (onValidateListener != null)
            onValidateListener.onValidate(place);
        dismiss();
    }

    private void alertError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public interface OnValidateListener {
        void onValidate(Place place);
    }

}
