package com.mandin.antoine.attestations;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mandin.antoine.attestations.model.Attestation;

public class WaitingActivity extends AppCompatActivity {
    public static final String EXTRA_ATTESTATION = "attestation";
    private Attestation attestation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        Bundle bundle = getIntent().getBundleExtra(EXTRA_ATTESTATION);
        attestation = Attestation.fromBundle(bundle, null);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toAttestation = new Intent(WaitingActivity.this, ViewActivity.class);
                Bundle bundle = new Bundle();
                attestation.populateBundle(bundle, null);
                toAttestation.putExtra(ViewActivity.EXTRA_ATTESTATION, bundle);
                startActivity(toAttestation);
            }
        });
    }
}