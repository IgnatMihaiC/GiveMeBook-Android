package com.licenta.mihai.givemebook_android.Singletons;

import com.licenta.mihai.givemebook_android.Models.BaseModels.Preferences;
import com.licenta.mihai.givemebook_android.Models.BaseModels.UserModel;

import java.util.List;

/**
 * Created by mihai on 24.05.2017.
 */

public class User {
    private static final User ourInstance = new User();

    public static User getInstance() {
        return ourInstance;
    }

    UserModel currentUser = new UserModel();


    public UserModel getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserModel currentUser) {
        this.currentUser = currentUser;
    }
}
