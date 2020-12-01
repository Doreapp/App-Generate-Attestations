package com.mandin.antoine.attestations;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.mandin.antoine.attestations.data_access.Service;
import com.mandin.antoine.attestations.model.Attestation;
import com.mandin.antoine.attestations.user_interface.ViewHolderAttestation;

import java.util.ArrayList;
import java.util.List;

public class AttestationsActivity extends AppCompatActivity implements ViewHolderAttestation.OnClickListener {
    private LinearLayout mainLayout;
    private List<Attestation> attestations;
    private List<ViewHolderAttestation> viewHolders = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_attestations);

        mainLayout = findViewById(R.id.layout_attestations_list);

        buildList();
    }

    private void buildList(){
        mainLayout.removeAllViews();
        viewHolders.clear();

        attestations = new Service(this).getAttestations();
        if(attestations != null){
            LayoutInflater inflater = LayoutInflater.from(this);
            for(Attestation attestation : attestations){
                View itemView = inflater.inflate(R.layout.view_attestation, mainLayout, false);
                ViewHolderAttestation vh = new ViewHolderAttestation(itemView, attestation);
                vh.setOnClickListener(this);
                mainLayout.addView(itemView);
                viewHolders.add(vh);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        for(ViewHolderAttestation vh : viewHolders)
            vh.loadTimeDifference();
    }

    @Override
    public void onClick(Attestation attestation, ViewHolderAttestation viewHolder) {
        Log.i("AttestationsActivity","on click : "+attestation);

        Intent attestationIntent = new Intent(this, ViewActivity.class);
        Bundle bundle = new Bundle();
        attestation.populateBundle(bundle, null);
        attestationIntent.putExtra(ViewActivity.EXTRA_ATTESTATION, bundle);

        startActivity(attestationIntent);
    }

    @Override
    public void onClickRemove(Attestation attestation, ViewHolderAttestation viewHolder) {
        new Service(this).removeAttestation(attestation);
        mainLayout.removeView(viewHolder.getItemView());
        attestations.remove(attestation);
        viewHolders.remove(viewHolder);
    }
}