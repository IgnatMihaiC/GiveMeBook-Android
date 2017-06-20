package com.licenta.mihai.givemebook_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookDetailsActivity extends AppCompatActivity {

    @BindView(R.id.book_review_activity_tab_host)
    TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        ButterKnife.bind(this);


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
