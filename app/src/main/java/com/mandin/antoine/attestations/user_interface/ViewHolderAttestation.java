package com.mandin.antoine.attestations.user_interface;

import android.view.View;
import android.widget.TextView;

import com.mandin.antoine.attestations.R;
import com.mandin.antoine.attestations.model.Attestation;
import com.mandin.antoine.attestations.util.DateComparator;

import java.util.Date;

public class ViewHolderAttestation {
    private TextView tvPrimary, tvSecondary, tvLastConsult;
    private Attestation attestation;
    private View contentView;

    public ViewHolderAttestation(View contentView, Attestation attestation) {
        this.contentView = contentView;
        this.attestation = attestation;

        tvPrimary = contentView.findViewById(R.id.tv_primary);
        tvSecondary = contentView.findViewById(R.id.tv_secondary);
        tvLastConsult = contentView.findViewById(R.id.tv_last_consult);

        loadTimeDifference();
    }

    public void setOnClickListener(final OnClickListener onClickListener) {
        this.contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(attestation, ViewHolderAttestation.this);
            }
        });
        contentView.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClickRemove(attestation, ViewHolderAttestation.this);
            }
        });
    }

    public View getItemView() {
        return contentView;
    }

    public void loadTimeDifference(){
        tvPrimary.setText(buildPrimaryString());
        tvSecondary.setText(buildSecondaryString());
        tvLastConsult.setText(buildLastConsultString());
    }

    private String buildLastConsultString() {
        Date now = new Date(System.currentTimeMillis());
        Date realCreationDate = attestation.getRealCreationDate();
        DateComparator dateComparator = new DateComparator();
        dateComparator.setDates(now, realCreationDate);

        return "Dernière consultation : " + dateComparator.shortDifferenceFR();
    }

    private String buildSecondaryString() {
        Date now = new Date(System.currentTimeMillis());
        Date creationDate = attestation.getCreationDate();
        DateComparator dateComparator = new DateComparator();
        dateComparator.setDates(now, creationDate);

        return attestation.getPlace().getCity() + ", " +
                attestation.getPlace().getAddress() + "\n" +
                "Crée " +
                dateComparator.shortDifferenceFR();

    }

    private String buildPrimaryString() {
        Date now = new Date(System.currentTimeMillis());
        Date usingDate = attestation.getUsingDate();

        DateComparator dateComparator = new DateComparator();
        dateComparator.setDates(now, usingDate);

        return ReasonRadioGroup.NAMES[attestation.getReason().ordinal()] +
                " - " +
                dateComparator.shortDifferenceFR();
    }

    public interface OnClickListener {
        void onClick(Attestation attestation, ViewHolderAttestation viewHolder);

        void onClickRemove(Attestation attestation, ViewHolderAttestation viewHolder);
    }
}
