package com.licenta.mihai.givemebook_android.Models.BaseModels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mihai on 12.06.2017.
 */

public class Book {

    private Long bId = 0l;
    private String title = "";
    private String author = "";
    private String description = "";
    private String cover_photo = "";

    private List<Categories> categories = new ArrayList<>();
    private List<BookReview> bookReviews = new ArrayList<>();
}
