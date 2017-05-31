package com.licenta.mihai.givemebook_android;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

    @BindView(R.id.main_view_profile_settings_layout)
    RelativeLayout profileSettings;

    @BindView(R.id.main_view_search)
    ImageView profileSearch;

    @BindView(R.id.main_view_gridMainData)
    GridView gridView;


    @BindView(R.id.main_view_profile_settings1)
    ImageView settings1;

    @BindView(R.id.main_view_profile_settings2)
    ImageView settings2;

    @BindView(R.id.main_view_search_input)
    EditText searchInput;


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


    private Boolean toggleMenu = true;
    private Boolean toggleSettings = true;
    private Boolean toggleSearch = true;

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


    @OnClick({R.id.main_view_profile_picture, R.id.main_view_profile_friends, R.id.main_view_profile_settings, R.id.main_view_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_view_profile_picture:
                doMenuAnimations();
                break;
            case R.id.main_view_profile_settings:
                doSettingsAnimations();
                break;
            case R.id.main_view_search:
                doSearchAnimations();
                break;
            case R.id.main_view_profile_friends:
                break;
        }
    }

    //region menu animations
    private void enterAnimation() {

        profileSearch.animate().translationX(-250).setDuration(600);
        ObjectAnimator friendsAnimX;
        friendsAnimX = ObjectAnimator.ofFloat(profileFriends, "translationX", -150);
        friendsAnimX.setDuration(600);
        friendsAnimX.start();
        ObjectAnimator friendsAnimY;
        friendsAnimY = ObjectAnimator.ofFloat(profileFriends, "translationY", 100);
        friendsAnimY.setDuration(600);
        friendsAnimY.start();
        profileSettings.animate().translationY(250).setDuration(600);
    }

    private void exitAnimation() {
        profileSearch.animate().translationX(0).setDuration(600);
        ObjectAnimator friendsAnimX;
        friendsAnimX = ObjectAnimator.ofFloat(profileFriends, "translationX", 150);
        friendsAnimX.setDuration(600);
        friendsAnimX.start();

        ObjectAnimator friendsAnimY;
        friendsAnimY = ObjectAnimator.ofFloat(profileFriends, "translationY", -100);
        friendsAnimY.setDuration(600);
        friendsAnimY.start();
        profileSettings.animate().translationY(0).setDuration(600);

    }


    private void enterSettingsAnimation() {
        settings1.setVisibility(View.VISIBLE);
        settings2.setVisibility(View.VISIBLE);
        settings1.animate().translationX(-150).setDuration(600);
        settings1.animate().setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                settings1.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        settings2.animate().translationY(150).setDuration(600);
        settings2.animate().setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                settings2.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void exitSettingsAnimation() {
        settings1.animate().translationX(0).setDuration(600);
        settings1.animate().setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                settings1.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        settings2.animate().translationY(0).setDuration(600);
        settings2.animate().setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                settings2.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    private void doMenuAnimations() {
        if (toggleMenu) {
            enterAnimation();
            toggleMenu = false;
        } else {
            resetAnimations();
        }
    }

    private void doSettingsAnimations() {
        if (toggleSettings) {
            enterSettingsAnimation();
            toggleSettings = false;
        } else {
            exitSettingsAnimation();
            toggleSettings = true;
        }
    }

    private void doSearchAnimations() {
        if (toggleSearch) {
            searchInput.setVisibility(View.VISIBLE);
            searchInput.animate().translationX(-320).setDuration(600);
            searchInput.animate().setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            toggleSearch = false;
        } else {
            searchInput.animate().translationX(320).setDuration(600);
            searchInput.animate().setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    searchInput.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            searchInput.setText("");
            toggleSearch = true;
        }
    }

    private void resetAnimations() {
        exitSettingsAnimation();
        toggleSettings = true;
        exitAnimation();
        searchInput.animate().translationX(320).setDuration(600);
        searchInput.animate().setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                searchInput.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        searchInput.setText("");
        toggleSearch = true;
        toggleMenu = true;
    }

    //endregion
}
