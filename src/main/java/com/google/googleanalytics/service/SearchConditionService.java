package com.google.googleanalytics.service;

import com.google.api.services.analyticsreporting.v4.model.Dimension;
import com.google.api.services.analyticsreporting.v4.model.Metric;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchConditionService {

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
        Dimension pagePath = new Dimension().setName("ga:pagePath");
        Dimension pageTitle = new Dimension().setName("ga:pageTitle");

        dimensionsList.add(pagePath);
        dimensionsList.add(pageTitle);

        return dimensionsList;
    }






}
