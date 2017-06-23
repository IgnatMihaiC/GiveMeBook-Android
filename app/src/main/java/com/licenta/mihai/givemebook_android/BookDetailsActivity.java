package com.licenta.mihai.givemebook_android;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TabHost;

import com.licenta.mihai.givemebook_android.CustomViews.CustomText.BorderEditText;
import com.licenta.mihai.givemebook_android.CustomViews.CustomText.TextViewOpenSans;
import com.licenta.mihai.givemebook_android.CustomViews.CustomText.TextViewOpenSansBold;
import com.licenta.mihai.givemebook_android.Models.BaseModels.Book;
import com.licenta.mihai.givemebook_android.Models.BaseModels.BookReview;
import com.licenta.mihai.givemebook_android.Models.BaseModels.BookState;
import com.licenta.mihai.givemebook_android.Models.NetModels.Replay.NetBookReviewReplay;
import com.licenta.mihai.givemebook_android.Models.NetModels.Replay.NetBookStateReplay;
import com.licenta.mihai.givemebook_android.Models.NetModels.Response.NetStringResponse;
import com.licenta.mihai.givemebook_android.Network.RestClient;
import com.licenta.mihai.givemebook_android.Singletons.User;
import com.licenta.mihai.givemebook_android.Singletons.UserRecommendation;
import com.licenta.mihai.givemebook_android.Utils.Util;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetailsActivity extends AppCompatActivity {

    @BindView(R.id.book_review_activity_tab_host)
    TabHost tabHost;

    @BindView(R.id.book_review_activity_bookName)
    TextViewOpenSansBold bookName;

    @BindView(R.id.book_review_activity_bookPhoto)
    CircleImageView bookImage;

    @BindView(R.id.details_author)
    TextViewOpenSans author;

    @BindView(R.id.details_description)
    TextViewOpenSans description;

    @BindView(R.id.details_categories)
    TextViewOpenSans categories;

    @BindView(R.id.review_tab_list)
    LinearLayout reviewsHolder;

    @BindView(R.id.review_add_review)
    FloatingActionButton addReview;

    @BindView(R.id.details_uploadedBy)
    CircleImageView uploadeByImage;

    @BindView(R.id.bookDetailes_like_Button)
    LikeButton likeBookButton;

    @BindView(R.id.bookDetailes_favorite_Button)
    LikeButton favBookButton;

    private Book currentSelectedBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        ButterKnife.bind(this);
        initComponents();
        populateView(getIntent().getIntExtra("position", 0));
    }

    private void initComponents() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        tabHost.setup();
        TabHost.TabSpec spec = tabHost.newTabSpec("Tab One");
        spec.setContent(R.id.book_review_tab_details);
        spec.setIndicator("Details");
        tabHost.addTab(spec);

        //Tab 2
        spec = tabHost.newTabSpec("Tab Two");
        spec.setContent(R.id.book_review_tab_review);
        spec.setIndicator("Reviews");
        tabHost.addTab(spec);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String arg0) {
                setTabColor(tabHost);
            }
        });
        setTabColor(tabHost);

        likeBookButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                sendBookState();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                sendBookState();
            }
        });
        favBookButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                sendBookState();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                sendBookState();
            }
        });
    }


    private void populateView(Integer position) {
        currentSelectedBook = UserRecommendation.getInstance().getRecommendationsList().get(position).getBook();
        bookName.setText(currentSelectedBook.getTitle().toUpperCase());
        if (currentSelectedBook.getCover_photo() != null) {
            Picasso.with(BookDetailsActivity.this).load(currentSelectedBook.getCover_photo()).into(bookImage);
        } else {
            bookImage.setImageDrawable(getResources().getDrawable(R.drawable.book_default));
        }
        author.setText(currentSelectedBook.getAuthor());
        description.setText(currentSelectedBook.getDescription());
        categories.setText(currentSelectedBook.getCategories().get(0).getName());
        Picasso.with(BookDetailsActivity.this).load("http://192.168.100.217:8080/api/resource/image/user/" + currentSelectedBook.getUploaderID()).error(R.drawable.user_default).into(uploadeByImage);

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        for (BookReview bookReview : currentSelectedBook.getBookReviews()) {
            View myView = layoutInflater.inflate(R.layout.book_review_element, null);
            final BorderEditText editText = (BorderEditText) myView.findViewById(R.id.review_element_content);
            editText.setEnabled(false);
            editText.setText(bookReview.getReviewContent());
            CircleImageView circleImageView = (CircleImageView) myView.findViewById(R.id.review_element_userPhoto);
            Picasso.with(myView.getContext()).load("http://192.168.100.217:8080/api/resource/image/user/" + bookReview.getUserID()).error(R.drawable.user_default).into(circleImageView);
            reviewsHolder.addView(myView);
            if (bookReview.getUserID().equals(User.getInstance().getCurrentUser().getUid())) {
                addReview.setVisibility(View.GONE);
            }
        }

        for (BookState bookState : User.getInstance().getCurrentUser().getBookStates()) {
            if (currentSelectedBook.getbId().equals(bookState.getBook().getbId())) {
                switch (bookState.getType()) {
                    case 1:
                        favBookButton.setLiked(true);
                        break;
                    case 2:
                        likeBookButton.setLiked(true);
                        break;
                    case 3:
                        favBookButton.setLiked(true);
                        likeBookButton.setLiked(true);
                        break;
                }
            }
        }

    }

    public void sendBookState() {
        final NetBookStateReplay netBookStateReplay = new NetBookStateReplay();
        netBookStateReplay.setBookID(currentSelectedBook.getbId());
        netBookStateReplay.setUserID(User.getInstance().getCurrentUser().getUid());
        if (likeBookButton.isLiked()) {
            if (favBookButton.isLiked()) {
                netBookStateReplay.setType(3);
            } else {
                netBookStateReplay.setType(2);
            }
        } else {
            if (favBookButton.isLiked()) {
                netBookStateReplay.setType(1);
            } else {
                netBookStateReplay.setType(0);
            }
        }

        RestClient.networkHandler().addBookState(User.getInstance().getCurrentUser().getToken(), netBookStateReplay)
                .enqueue(new Callback<NetStringResponse>() {
                    @Override
                    public void onResponse(Call<NetStringResponse> call, Response<NetStringResponse> response) {
                        if (!response.isSuccessful()) {
                            switch (netBookStateReplay.getType()) {
                                case 1:
                                    if (favBookButton.isLiked())
                                        favBookButton.setLiked(false);
                                    else
                                        favBookButton.setLiked(true);
                                    break;
                                case 2:
                                    if (likeBookButton.isLiked())
                                        likeBookButton.setLiked(false);
                                    else
                                        likeBookButton.setLiked(true);
                                    break;
                                case 3:
                                    if (likeBookButton.isLiked())
                                        likeBookButton.setLiked(false);
                                    else
                                        likeBookButton.setLiked(true);
                                    if (favBookButton.isLiked())
                                        favBookButton.setLiked(false);
                                    else
                                        favBookButton.setLiked(true);
                                    break;
                            }
                        } else {
                            User.getInstance().getCurrentUser().getBookStates().get(User.getInstance().getCurrentUser().getBookStateIndex(currentSelectedBook.getbId())).setType(netBookStateReplay.getType());
                        }
                    }

                    @Override
                    public void onFailure(Call<NetStringResponse> call, Throwable t) {

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


    @OnClick({R.id.review_add_review, R.id.details_uploadedBy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.review_add_review:
                manageAddReview();
                break;
            case R.id.details_uploadedBy:
                Util.openActivity(BookDetailsActivity.this, UserDetailsActivity.class);
                break;
        }
    }

    private void manageAddReview() {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View myView = layoutInflater.inflate(R.layout.book_review_element, null);
        final LinearLayout addReviewOptions = (LinearLayout) myView.findViewById(R.id.add_review_userOption);
        addReviewOptions.setVisibility(View.VISIBLE);
        final BorderEditText editText = (BorderEditText) myView.findViewById(R.id.review_element_content);
        editText.setEnabled(true);
        CircleImageView circleImageView = (CircleImageView) myView.findViewById(R.id.review_element_userPhoto);
        Picasso.with(myView.getContext()).load(User.getInstance().getCurrentUser().getPhotoUrl()).into(circleImageView);
        CircularProgressButton saveButton = (CircularProgressButton) myView.findViewById(R.id.addReview_saveButton);
        CircularProgressButton discardButton = (CircularProgressButton) myView.findViewById(R.id.addReview_discardButton);
        reviewsHolder.addView(myView);

        discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviewsHolder.removeViewAt(reviewsHolder.getChildCount() - 1);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetBookReviewReplay bookReview = new NetBookReviewReplay();
                bookReview.setReviewContent(editText.getText().toString());
                bookReview.setUserID(User.getInstance().getCurrentUser().getUid());
                RestClient.networkHandler().addBookReview(User.getInstance().getCurrentUser().getToken(), currentSelectedBook.getbId(), bookReview)
                        .enqueue(new Callback<NetStringResponse>() {
                            @Override
                            public void onResponse(Call<NetStringResponse> call, Response<NetStringResponse> response) {
                                addReviewOptions.setVisibility(View.GONE);
                                editText.setEnabled(false);
                                addReview.setVisibility(View.GONE);

                            }

                            @Override
                            public void onFailure(Call<NetStringResponse> call, Throwable t) {

                            }
                        });
            }
        });
    }

}
