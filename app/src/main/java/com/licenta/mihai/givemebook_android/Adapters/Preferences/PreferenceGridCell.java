package com.licenta.mihai.givemebook_android.Adapters.Preferences;

import com.licenta.mihai.givemebook_android.Models.BaseModels.Preferences;

/**
 * Created by mihai on 09.06.2017.
 */

public class PreferenceGridCell {

    Preferences preferences;

    public PreferenceGridCell(Preferences preferences) {
        this.preferences = preferences;
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }
}
