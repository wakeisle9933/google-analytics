package com.google.googleanalytics.service;

import com.google.api.services.analyticsreporting.v4.model.DateRange;
import com.google.api.services.analyticsreporting.v4.model.Dimension;
import com.google.api.services.analyticsreporting.v4.model.Metric;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchConditionService {

    // 날짜 범위 설정
    public DateRange SummaryDateRange() {
        DateRange dateRange = new DateRange();
        /*
        dateRange.setStartDate("2021-11-29");
        dateRange.setEndDate("2021-11-30");
*/

        dateRange.setStartDate("7DaysAgo");
        dateRange.setEndDate("today");

        return dateRange;
    }

    // 요약 조회용 Metrics List 반환
    public List<Metric> SummarySearchMetricsList() {
        // Metrics(조회할 컬럼) 객체 생성
        List<Metric> metricsList = new ArrayList<>();
        Metric pageviews = new Metric().setExpression("ga:pageviews")
                .setAlias("pageviews");

        Metric adsenseRevenue = new Metric().setExpression("ga:adsenseRevenue")
                .setAlias("adsenseRevenue");

        Metric adsenseAdsClicks = new Metric().setExpression("ga:adsenseAdsClicks")
                .setAlias("adsenseAdsClicks");

        metricsList.add(pageviews);
        metricsList.add(adsenseRevenue);
        metricsList.add(adsenseAdsClicks);

        return metricsList;
    }

    // 요약 조회용 Dimensions List 반환
    public List<Dimension> SummarySearchDimensionsList() {
        List<Dimension> dimensionsList = new ArrayList<>();
        Dimension pageTitle = new Dimension().setName("ga:pageTitle");
        dimensionsList.add(pageTitle);

        return dimensionsList;
    }

    // 카테고리별 조회용 Dimensions List 반환
    public List<Dimension> SummaryCategorySearchDimensionsList() {
        List<Dimension> dimensionsList = new ArrayList<>();
        Dimension pagePath = new Dimension().setName("ga:pagePath");
        dimensionsList.add(pagePath);

        return dimensionsList;
    }

}
