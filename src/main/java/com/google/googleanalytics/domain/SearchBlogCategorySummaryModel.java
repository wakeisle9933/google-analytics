package com.google.googleanalytics.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SearchBlogCategorySummaryModel {

    int postNumber;
    String category;
    String categoryName;
    String totalPageViews;
    String totalAdsenseRevenue;
    String totalAdsenseAdsClicks;

}
