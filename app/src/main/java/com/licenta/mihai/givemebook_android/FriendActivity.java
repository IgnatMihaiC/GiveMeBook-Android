package com.licenta.mihai.givemebook_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;

import com.licenta.mihai.givemebook_android.CustomViews.CustomText.TextViewOpenSansBold;
import com.licenta.mihai.givemebook_android.Models.BaseModels.SharedUser;
import com.licenta.mihai.givemebook_android.Singletons.User;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class FriendActivity extends AppCompatActivity {

    @BindView(R.id.friend_activity_tab_host)
    TabHost tabHost;

    @BindView(R.id.friend_activity_userPhoto)
    CircleImageView userPhoto;

    @BindView(R.id.friend_activity_userName)
    TextViewOpenSansBold userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        int fid = intent.getIntExtra("position", 0);

        SharedUser friend = User.getInstance().getFriendList().get(fid);
        Picasso.with(this).load("http://" + friend.getPhotoUrl()).into(userPhoto);
        userName.setText(friend.getUsername().toUpperCase());


        tabHost.setup();

        TabHost.TabSpec spec = tabHost.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Liked");
        tabHost.addTab(spec);

        //Tab 2
        spec = tabHost.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Favorites");
        tabHost.addTab(spec);

    }
}
