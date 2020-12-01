package com.mandin.antoine.attestations.user_interface;

import android.content.Context;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.mandin.antoine.attestations.data_access.Schema;
import com.mandin.antoine.attestations.data_access.Service;
import com.mandin.antoine.attestations.model.Place;

import java.util.ArrayList;
import java.util.List;

public class PlacesRadioGroup {
    private RadioGroup radioGroup;
    private List<Place> places;

    public PlacesRadioGroup(RadioGroup radioGroup) {
        this.radioGroup = radioGroup;

        initContent();
    }

    public void reloadContent(){
        radioGroup.clearCheck();
        radioGroup.removeAllViews();
        initContent();
    }

    private void initContent(){
        Context context = radioGroup.getContext();

        places = new Service(context).getPlaces();
        if(places == null){
            places = new ArrayList<>();
        }

        for(Place place : places){
            radioGroup.addView(generate(context, place));
        }
    }

    private RadioButton generate(Context context, final Place place){
        RadioButton result = new RadioButton(context);
        result.setText(place.getCity()+", "+place.getAddress());
        result.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new DialogPlace(v.getContext(), place)
                        .setOnValidateListener(new DialogPlace.OnValidateListener() {
                            @Override
                            public void onValidate(Place place) {
                                reloadContent();
                            }
                        })
                        .show();
                return true;
            }
        });
        return result;
    }

    public Place getSelectedPlace(){
        int radioButtonID = radioGroup.getCheckedRadioButtonId();
        if(radioButtonID == -1)
            return null;
        View radioButton = radioGroup.findViewById(radioButtonID);
        int idx = radioGroup.indexOfChild(radioButton);
        return places.get(idx);
    }
}
