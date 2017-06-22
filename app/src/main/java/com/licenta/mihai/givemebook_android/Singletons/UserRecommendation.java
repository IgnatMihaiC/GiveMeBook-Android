package com.licenta.mihai.givemebook_android.Singletons;

import com.licenta.mihai.givemebook_android.Models.BaseModels.Recommendations;

import java.util.List;

/**
 * Created by mihai on 20.06.2017.
 */

public class UserRecommendation {
    private static final UserRecommendation ourInstance = new UserRecommendation();

    public static UserRecommendation getInstance() {
        return ourInstance;
    }

    private List<Recommendations> recommendationsList;

    private UserRecommendation() {
    }


    public List<Recommendations> getRecommendationsList() {
        return recommendationsList;
    }

    public void setRecommendationsList(List<Recommendations> recommendationsList) {
        this.recommendationsList = recommendationsList;
    }
}


