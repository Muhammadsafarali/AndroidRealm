package com.example.safaralialisultanov.realmexample;

import android.widget.EditText;

/**
 * Created by safarali.alisultanov on 09.07.2016.
 */
public class Utility {

    public static boolean isBlankField(EditText etPersonData) {
        return etPersonData.getText().toString().trim().equals("");
    }
}
