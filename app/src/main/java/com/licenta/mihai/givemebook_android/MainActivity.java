package com.licenta.mihai.givemebook_android;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.licenta.mihai.givemebook_android.Adapters.Books.BookCell;
import com.licenta.mihai.givemebook_android.Adapters.Books.BookGridAdapter;
import com.licenta.mihai.givemebook_android.Adapters.Friend.FriendListCell;
import com.licenta.mihai.givemebook_android.Adapters.Friend.FriendRecyclerAdapterAdapter;
import com.licenta.mihai.givemebook_android.CustomViews.CustomText.TextViewOpenSansBold;
import com.licenta.mihai.givemebook_android.CustomViews.Popups.UserAddBook;
import com.licenta.mihai.givemebook_android.CustomViews.Popups.UserPreferencesPopup;
import com.licenta.mihai.givemebook_android.CustomViews.Popups.UserSettingsPopup;
import com.licenta.mihai.givemebook_android.Models.BaseModels.Interactions;
import com.licenta.mihai.givemebook_android.Models.BaseModels.Recommendations;
import com.licenta.mihai.givemebook_android.Models.BaseModels.SharedUser;
import com.licenta.mihai.givemebook_android.Network.RestClient;
import com.licenta.mihai.givemebook_android.Singletons.User;
import com.licenta.mihai.givemebook_android.Singletons.UserRecommendation;
import com.licenta.mihai.givemebook_android.Utils.UploadPhoto;
import com.licenta.mihai.givemebook_android.Utils.Util;
import com.squareup.picasso.Picasso;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    //region initView
    @BindView(R.id.main_view_profile_picture)
    ImageView profilePicture;

    @BindView(R.id.main_view_add_book)
    ImageView addBook;

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

    @BindView(R.id.noLayoutMessage)
    TextViewOpenSansBold noInfos;
    //endregion

    private Boolean toggleMenu = true;
    private Boolean toggleSettings = true;
    private Boolean toggleSearch = true;
    private UserSettingsPopup userSettingsPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        getAdditionUserData();
        initComponents();
        initFriendList();

    }

    private void initComponents() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        getRecommendations();
        if (User.getInstance().getCurrentUser().getPhotoUrl() != null) {
            Picasso.with(MainActivity.this)
                    .load(User.getInstance().getCurrentUser().getPhotoUrl())
                    .into(profilePicture);
        }


    }

    private void getRecommendations() {
        RestClient.networkHandler().getRecommendations(User.getInstance().getCurrentUser().getToken(), User.getInstance().getCurrentUser().getUid())
                .enqueue(new Callback<List<Recommendations>>() {
                    @Override
                    public void onResponse(Call<List<Recommendations>> call, Response<List<Recommendations>> response) {
                        setUpRecommendations(response);
                    }

                    @Override
                    public void onFailure(Call<List<Recommendations>> call, Throwable t) {

                    }
                });
    }

    private void setUpRecommendations(Response<List<Recommendations>> response) {
        UserRecommendation.getInstance().setRecommendationsList(response.body());
        if (UserRecommendation.getInstance().getRecommendationsList().size() != 0) {
            noInfos.setVisibility(View.GONE);
            List<BookCell> allBooks = new ArrayList<>();
            for (Recommendations r : response.body()) {
                allBooks.add(new BookCell(r.getBook(), r.getUser().getPhotoUrl()));
            }
            BookGridAdapter adapter = new BookGridAdapter(MainActivity.this, allBooks);
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MainActivity.this, BookDetailsActivity.class);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
            });
        } else {
            noInfos.setText("No recommendation yet");
            noInfos.setVisibility(View.VISIBLE);
        }
    }

    public void getAdditionUserData() {

    }

    private void initFriendList() {
        final List<FriendListCell> cells = new ArrayList<>();
        for (Interactions interactions : User.getInstance().getCurrentUser().getInteractions()) {
            if (interactions.getType() != 3) {
                RestClient.networkHandler().getUserById(User.getInstance().getCurrentUser().getToken(), interactions.getRefId())
                        .enqueue(new Callback<SharedUser>() {
                            @Override
                            public void onResponse(Call<SharedUser> call, Response<SharedUser> response) {
                                User.getInstance().getFriendList().add(response.body());
                                cells.add(new FriendListCell(response.body().getUsername(), response.body().getPhotoUrl()));
                            }

                            @Override
                            public void onFailure(Call<SharedUser> call, Throwable t) {

                            }
                        });
            }
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        friendsList.setLayoutManager(layoutManager);
        FriendRecyclerAdapterAdapter adapter = new FriendRecyclerAdapterAdapter(MainActivity.this, cells, new FriendRecyclerAdapterAdapter.CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(MainActivity.this, FriendActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
        friendsList.setAdapter(adapter);
        if (cells.size() == 0) {
            friendsList.setVisibility(View.GONE);
        } else {
            friendsList.setVisibility(View.VISIBLE);
        }
    }


    @OnClick({R.id.main_view_profile_picture, R.id.main_view_add_book, R.id.main_view_profile_settings, R.id.main_view_search, R.id.main_view_profile_preferences, R.id.main_view_profile_settings_gen})
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
                userSettingsPopup = new UserSettingsPopup(MainActivity.this, onDismiss);
                userSettingsPopup.init();
                userSettingsPopup.showUserPopup();
                break;
            case R.id.main_view_search:
                doSearchAnimations();
                break;
            case R.id.main_view_add_book:
                DialogInterface.OnDismissListener onDismissBook = new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                    }
                };
                UserAddBook userAddBook = new UserAddBook(MainActivity.this, onDismissBook);
                userAddBook.init();
                userAddBook.showUserPopup();
                break;
        }
    }


    //region menu animations
    private void enterAnimation() {

        profileSearch.animate().translationX(-250).setDuration(600);
        ObjectAnimator friendsAnimX;
        friendsAnimX = ObjectAnimator.ofFloat(addBook, "translationX", -150);
        friendsAnimX.setDuration(600);
        friendsAnimX.start();
        ObjectAnimator friendsAnimY;
        friendsAnimY = ObjectAnimator.ofFloat(addBook, "translationY", 100);
        friendsAnimY.setDuration(600);
        friendsAnimY.start();
        profileSettings.animate().translationY(250).setDuration(600);
    }

    private void exitAnimation() {
        profileSearch.animate().translationX(0).setDuration(600);
        ObjectAnimator friendsAnimX;
        friendsAnimX = ObjectAnimator.ofFloat(addBook, "translationX", 150);
        friendsAnimX.setDuration(600);
        friendsAnimX.start();

        ObjectAnimator friendsAnimY;
        friendsAnimY = ObjectAnimator.ofFloat(addBook, "translationY", -100);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (userSettingsPopup.uploadPhoto.handleOnActivityResult(requestCode, resultCode, data)) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(UploadPhoto.getPicturePath(), options);
            final int REQUIRED_SIZE = 1024;
            int scale = 1;
            while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                    && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;
            options.inSampleSize = scale;
            options.inJustDecodeBounds = false;

            Bitmap bitmap = BitmapFactory.decodeFile(UploadPhoto.getPicturePath(), options);
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            if (height > width) {
                width = (int) ((width * 1000) / height);
                height = 1000;
            } else {
                height = (int) ((height * 1000) / width);
                width = 1000;
            }
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(UploadPhoto.getPicturePathEdited());
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            userSettingsPopup.isPictureReady();
        }
    }
}
