package com.mandin.antoine.attestations.user_interface;

import android.content.Context;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.mandin.antoine.attestations.model.Reason;

public class ReasonRadioGroup {
    public static final String[] NAMES = {
            "Travail",
            "Achats",
            "Santé",
            "Famille",
            "Handicap",
            "Balade, sport, animaux",
            "Convocation",
            "Missions",
            "Enfants"
    };
    private RadioGroup radioGroup;

    public ReasonRadioGroup(RadioGroup radioGroup) {
        this.radioGroup = radioGroup;
        Context context = radioGroup.getContext();

        for (Reason reason : Reason.values()) {
            radioGroup.addView(generate(context, reason));
        }
    }

    private static RadioButton generate(Context context, Reason reason) {
        RadioButton result = new RadioButton(context);
        result.setText(NAMES[reason.ordinal()]);
        return result;
    }

    public Reason getSelectedReason() {
        int radioButtonID = radioGroup.getCheckedRadioButtonId();
        if (radioButtonID == -1)
            return null;
        View radioButton = radioGroup.findViewById(radioButtonID);
        int idx = radioGroup.indexOfChild(radioButton);
        return Reason.values()[idx];
    }

}
