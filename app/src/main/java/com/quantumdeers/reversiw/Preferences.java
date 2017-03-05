package com.quantumdeers.reversiw;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by juanfdg on 5/03/17.
 */

public class Preferences extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.app_preferences);
    }
}
