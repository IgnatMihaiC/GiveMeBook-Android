package com.licenta.mihai.givemebook_android;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TabHost;

import com.licenta.mihai.givemebook_android.Adapters.Books.BookCell;
import com.licenta.mihai.givemebook_android.Adapters.Books.BookGridAdapter;
import com.licenta.mihai.givemebook_android.CustomViews.CustomText.TextViewOpenSansBold;
import com.licenta.mihai.givemebook_android.Models.BaseModels.BookState;
import com.licenta.mihai.givemebook_android.Models.BaseModels.Interactions;
import com.licenta.mihai.givemebook_android.Models.BaseModels.Recommendations;
import com.licenta.mihai.givemebook_android.Models.BaseModels.SharedUser;
import com.licenta.mihai.givemebook_android.Models.NetModels.Replay.NetInteractions;
import com.licenta.mihai.givemebook_android.Models.NetModels.Response.NetStringResponse;
import com.licenta.mihai.givemebook_android.Network.RestClient;
import com.licenta.mihai.givemebook_android.Singletons.User;
import com.licenta.mihai.givemebook_android.Singletons.UserRecommendation;
import com.licenta.mihai.givemebook_android.Utils.Util;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDetailsActivity extends AppCompatActivity {

    @BindView(R.id.friend_activity_tab_host)
    TabHost tabHost;

    @BindView(R.id.friend_activity_userPhoto)
    CircleImageView userPhoto;

    @BindView(R.id.friend_activity_userName)
    TextViewOpenSansBold userName;

    @BindView(R.id.addFriendButton)
    LikeButton addFriend;

    @BindView(R.id.friend_books_fav)
    GridView favGridView;


    @BindView(R.id.friend_books_liked)
    GridView likeGridView;

    @BindView(R.id.noLayoutMessage_fav)
    TextViewOpenSansBold noFavView;

    @BindView(R.id.noLayoutMessage_liked)
    TextViewOpenSansBold noLikeView;

    private SharedUser userDet;
    private List<BookState> userBookState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        int pos = intent.getIntExtra("position", -1);
        if (pos != -1) {
            userDet = User.getInstance().getFriendList().get(pos);
            populateView(userDet);
        } else {
            getNonFriendUser(intent.getLongExtra("fid", 0));
        }


        initComponents();

    }

    private void populateView(SharedUser sharedUser) {
        Picasso.with(this).load(sharedUser.getPhotoUrl()).into(userPhoto);
        userName.setText(sharedUser.getUsername().toUpperCase());
        for (Interactions i : User.getInstance().getCurrentUser().getInteractions()) {
            if (i.getRefId() == userDet.getUid()) {
                addFriend.setLiked(true);
            }
        }
        if (sharedUser.getUid() == User.getInstance().getCurrentUser().getUid()) {
            addFriend.setVisibility(View.GONE);
        }
        getUserBookStates(sharedUser.getUid());
    }

    public void initComponents() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

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

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String arg0) {
                setTabColor(tabHost);
            }
        });
        setTabColor(tabHost);


        addFriend.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                NetInteractions netInteraction = new NetInteractions();
                netInteraction.setType(1);
                netInteraction.setFriendID(userDet.getUid());
                netInteraction.setMyID(User.getInstance().getCurrentUser().getUid());
                RestClient.networkHandler().addInteractions(User.getInstance().getCurrentUser().getToken(), netInteraction)
                        .enqueue(new Callback<Interactions>() {
                            @Override
                            public void onResponse(Call<Interactions> call, Response<Interactions> response) {
                                User.getInstance().getCurrentUser().getInteractions().add(response.body());
                            }

                            @Override
                            public void onFailure(Call<Interactions> call, Throwable t) {

                            }
                        });
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                final NetInteractions netInteraction = new NetInteractions();
                netInteraction.setType(1);
                netInteraction.setFriendID(userDet.getUid());
                netInteraction.setMyID(User.getInstance().getCurrentUser().getUid());
                RestClient.networkHandler().removeInteraction(User.getInstance().getCurrentUser().getToken(), netInteraction)
                        .enqueue(new Callback<NetStringResponse>() {
                            @Override
                            public void onResponse(Call<NetStringResponse> call, Response<NetStringResponse> response) {
                                User.getInstance().getCurrentUser().getInteractions().remove(User.getInstance().getCurrentUser().getInteractionIndex(netInteraction.getFriendID()));
                            }

                            @Override
                            public void onFailure(Call<NetStringResponse> call, Throwable t) {

                            }
                        });
            }
        });
    }


    public void getUserBookStates(Long uid) {
        RestClient.networkHandler().getBookStateByUser(User.getInstance().getCurrentUser().getToken(), uid)
                .enqueue(new Callback<List<BookState>>() {
                    @Override
                    public void onResponse(Call<List<BookState>> call, Response<List<BookState>> response) {
                        userBookState = response.body();
                        Util.showObjectLog(userBookState);
                        populateGrids(userBookState);

                    }

                    @Override
                    public void onFailure(Call<List<BookState>> call, Throwable t) {

                    }
                });
    }

    public void getNonFriendUser(Long uid) {
        RestClient.networkHandler().getUserById(User.getInstance().getCurrentUser().getToken(), uid)
                .enqueue(new Callback<SharedUser>() {
                    @Override
                    public void onResponse(Call<SharedUser> call, Response<SharedUser> response) {
                        userDet = response.body();
                        populateView(response.body());
                    }

                    @Override
                    public void onFailure(Call<SharedUser> call, Throwable t) {

                    }
                });
    }

    public void populateGrids(List<BookState> bookStates) {

        List<BookCell> favBooks = new ArrayList<>();
        BookGridAdapter favAdapter = new BookGridAdapter(UserDetailsActivity.this, favBooks);
        favGridView.setAdapter(favAdapter);


        List<BookCell> likedBooks = new ArrayList<>();
        BookGridAdapter likeAdapter = new BookGridAdapter(UserDetailsActivity.this, likedBooks);
        likeGridView.setAdapter(likeAdapter);
        int fbS = getBsNrType(1, bookStates);
        int lbS = getBsNrType(2, bookStates);
        int bbS = getBsNrType(3, bookStates);


        for (BookState bookState : bookStates) {
            switch (bookState.getType()) {
                case 1:
                    if (fbS != 0) {
                        noFavView.setVisibility(View.GONE);
                        insertGridCell(favBooks, favAdapter, bookState);
                    }
                    break;
                case 2:
                    if (lbS != 0) {
                        noLikeView.setVisibility(View.GONE);
                        insertGridCell(likedBooks, likeAdapter, bookState);
                    }
                    break;
                case 3:
                    if (bbS != 0) {
                        noLikeView.setVisibility(View.GONE);
                        noFavView.setVisibility(View.GONE);
                        insertGridCell(favBooks, favAdapter, bookState);
                        insertGridCell(likedBooks, likeAdapter, bookState);
                    }
                    break;

            }
        }
    }

    private int getBsNrType(Integer type, List<BookState> bookStatesList) {
        int nr = 0;
        for (BookState bs : bookStatesList) {
            if (bs.getType().equals(type))
                nr++;
        }
        return nr;
    }

    private void insertGridCell(List<BookCell> favBooksBooks, BookGridAdapter favAdapter, BookState bookState) {
        favBooksBooks.add(new BookCell(bookState.getBook(), userDet.getPhotoUrl()));
        favAdapter.notifyDataSetChanged();
        favGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(UserDetailsActivity.this, BookDetailsActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }

    public void setTabColor(TabHost tabhost) {

        for (int i = 0; i < tabhost.getTabWidget().getChildCount(); i++)
            tabhost.getTabWidget().getChildAt(i).setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight)); //unselected

        if (tabhost.getCurrentTab() == 0)
            tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark)); //1st tab selected
        else
            tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark)); //2nd tab selected
    }

}
