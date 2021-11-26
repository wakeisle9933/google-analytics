package com.google.googleanalytics.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchBlogSummaryModel {

    int number;
    String name;
    int pageviews;
    Double adsenseRevenue;
    int adsenseAdsClicks;

}
