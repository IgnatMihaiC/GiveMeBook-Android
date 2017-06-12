package com.licenta.mihai.givemebook_android;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.licenta.mihai.givemebook_android.Adapters.Books.BookCell;
import com.licenta.mihai.givemebook_android.Adapters.Books.BookGridAdapter;
import com.licenta.mihai.givemebook_android.Adapters.Friend.FriendListCell;
import com.licenta.mihai.givemebook_android.Adapters.Friend.FriendRecyclerAdapterAdapter;
import com.licenta.mihai.givemebook_android.CustomViews.Popups.UserPreferencesPopup;
import com.licenta.mihai.givemebook_android.CustomViews.Popups.UserSettingsPopup;
import com.licenta.mihai.givemebook_android.Singletons.User;
import com.licenta.mihai.givemebook_android.Utils.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

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


    @BindView(R.id.main_view_profile_preferences)
    ImageView settingsPreferences;

    @BindView(R.id.main_view_profile_settings_gen)
    ImageView settingsGen;

    @BindView(R.id.main_view_search_input)
    EditText searchInput;

    @BindView(R.id.main_view_friend_list)
    RecyclerView friendsList;
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
        List<BookCell> allBooks = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            allBooks.add(new BookCell());
        }
        BookGridAdapter adapter = new BookGridAdapter(MainActivity.this, allBooks);
        gridView.setAdapter(adapter);
    }

    private void initComponents() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        if (User.getInstance().getCurrentUser().getPhotoUrl() != null) {
            Picasso.with(MainActivity.this)
                    .load("http://" + User.getInstance().getCurrentUser().getPhotoUrl())
                    .into(profilePicture);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        friendsList.setLayoutManager(layoutManager);
        List<FriendListCell> cells = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            cells.add(new FriendListCell("name", "photo"));

        }
        FriendRecyclerAdapterAdapter adapter = new FriendRecyclerAdapterAdapter(MainActivity.this, cells, new FriendRecyclerAdapterAdapter.CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Util.openActivity(MainActivity.this, FriendActivity.class);
            }
        });
        friendsList.setAdapter(adapter);

    }


    @OnClick({R.id.main_view_profile_picture, R.id.main_view_profile_friends, R.id.main_view_profile_settings, R.id.main_view_search, R.id.main_view_profile_preferences, R.id.main_view_profile_settings_gen})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_view_profile_picture:
                doMenuAnimations();
                break;
            case R.id.main_view_profile_settings:
                doSettingsAnimations();
                break;
            case R.id.main_view_profile_preferences:
                DialogInterface.OnDismissListener onDismissPrefs = new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        exitSettingsAnimation();
                        toggleSettings = true;
                    }
                };
                UserPreferencesPopup preffsPopup = new UserPreferencesPopup(MainActivity.this, onDismissPrefs);
                preffsPopup.init();
                preffsPopup.showUserPopup();
                break;
            case R.id.main_view_profile_settings_gen:
                DialogInterface.OnDismissListener onDismiss = new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        exitSettingsAnimation();
                        toggleSettings = true;
                    }
                };
                UserSettingsPopup userSettingsPopup = new UserSettingsPopup(MainActivity.this, onDismiss);
                userSettingsPopup.init();
                userSettingsPopup.showUserPopup();
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
        settingsPreferences.setVisibility(View.VISIBLE);
        settingsGen.setVisibility(View.VISIBLE);
        settingsPreferences.animate().translationX(-150).setDuration(600);
        settingsPreferences.animate().setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                settingsPreferences.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        settingsGen.animate().translationY(150).setDuration(600);
        settingsGen.animate().setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                settingsGen.setVisibility(View.VISIBLE);
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
        settingsPreferences.animate().translationX(0).setDuration(600);
        settingsPreferences.animate().setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                settingsPreferences.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        settingsGen.animate().translationY(0).setDuration(600);
        settingsGen.animate().setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                settingsGen.setVisibility(View.GONE);
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
            closeSoftKeyboard();
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
    private void closeSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}
