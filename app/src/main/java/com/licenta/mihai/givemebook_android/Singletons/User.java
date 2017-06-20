package com.licenta.mihai.givemebook_android.Singletons;

import com.licenta.mihai.givemebook_android.Models.BaseModels.SharedUser;
import com.licenta.mihai.givemebook_android.Models.BaseModels.UserModel;

import java.util.ArrayList;
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

    List<SharedUser> friendList = new ArrayList<>();

    public UserModel getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserModel currentUser) {
        this.currentUser = currentUser;
    }

    public List<SharedUser> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<SharedUser> friendList) {
        this.friendList = friendList;
    }
}
