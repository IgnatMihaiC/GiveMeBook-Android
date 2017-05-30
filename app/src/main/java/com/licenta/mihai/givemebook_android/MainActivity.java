package com.licenta.mihai.givemebook_android;

import android.animation.ObjectAnimator;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    //region initView
    @BindView(R.id.main_view_profile_picture)
    ImageView profilePicture;

    @BindView(R.id.main_view_profile_friends)
    ImageView profileFriends;

    @BindView(R.id.main_view_profile_settings)
    ImageView profileSettings;

    @BindView(R.id.main_view_search)
    ImageView profileSearch;

    @BindView(R.id.main_view_gridMainData)
    GridView gridView;

    //endregion

    static final String[] numbers = new String[]{
            "A", "B", "C", "D", "E",
            "A", "B", "C", "D", "E",
            "A", "B", "C", "D", "E",
            "A", "B", "C", "D", "E",
            "A", "B", "C", "D", "E",
            "A", "B", "C", "D", "E",
            "A", "B", "C", "D", "E",
            "A", "B", "C", "D", "E",
            "A", "B", "C", "D", "E",
            "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"};


    private Boolean toogleMenu = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        initComponents();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, numbers);

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getApplicationContext(),
                        ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initComponents() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    private void enterAnimation() {

        ObjectAnimator searchAnim;
        searchAnim = ObjectAnimator.ofFloat(profileSearch, "translationX", -250);
        searchAnim.setDuration(600);
        searchAnim.start();

        ObjectAnimator settingAnimX;
        settingAnimX = ObjectAnimator.ofFloat(profileFriends, "translationX", -150);
        settingAnimX.setDuration(600);
        settingAnimX.start();

        ObjectAnimator settingAnimY;
        settingAnimY = ObjectAnimator.ofFloat(profileFriends, "translationY", 100);
        settingAnimY.setDuration(600);
        settingAnimY.start();


        ObjectAnimator friendsAnim;
        friendsAnim = ObjectAnimator.ofFloat(profileSettings, "translationY", 250);
        friendsAnim.setDuration(600);
        friendsAnim.start();
    }

    private void exitAnimation() {
        ObjectAnimator searchAnim;
        searchAnim = ObjectAnimator.ofFloat(profileSearch, "translationX", 250);
        searchAnim.setDuration(600);
        searchAnim.start();

        ObjectAnimator settingAnimX;
        settingAnimX = ObjectAnimator.ofFloat(profileFriends, "translationX", 150);
        settingAnimX.setDuration(600);
        settingAnimX.start();

        ObjectAnimator settingAnimY;
        settingAnimY = ObjectAnimator.ofFloat(profileFriends, "translationY", -100);
        settingAnimY.setDuration(600);
        settingAnimY.start();


        ObjectAnimator friendsAnim;
        friendsAnim = ObjectAnimator.ofFloat(profileSettings, "translationY", -250);
        friendsAnim.setDuration(600);
        friendsAnim.start();
    }

    @OnClick({R.id.main_view_profile_picture, R.id.main_view_profile_friends, R.id.main_view_profile_settings, R.id.main_view_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_view_profile_picture:
                if (toogleMenu) {
                    enterAnimation();
                    toogleMenu = false;
                } else {
                    exitAnimation();
                    toogleMenu = true;
                }
                break;
            case R.id.main_view_profile_settings:
                Toast.makeText(MainActivity.this, "settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.main_view_search:
                Toast.makeText(getApplicationContext(),
                        "search", Toast.LENGTH_SHORT).show();
                break;
            case R.id.main_view_profile_friends:
                Toast.makeText(getApplicationContext(),
                       "fiends", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
