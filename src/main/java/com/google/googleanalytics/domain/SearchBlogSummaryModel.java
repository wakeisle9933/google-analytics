package com.google.googleanalytics.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SearchBlogSummaryModel {

    int postNumber;
    String pagePath;
    String postName;
    String pageViews;
    String adsenseRevenue;
    String adsenseAdsClicks;
    String totalPageViews;
    String totalAdsenseRevenue;
    String totalAdsenseAdsClicks;

}
