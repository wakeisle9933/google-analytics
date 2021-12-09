package com.google.googleanalytics.service;

import com.google.api.services.analyticsreporting.v4.model.DateRange;
import com.google.api.services.analyticsreporting.v4.model.Dimension;
import com.google.api.services.analyticsreporting.v4.model.Metric;
import com.google.api.services.analyticsreporting.v4.model.OrderBy;
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

    // 정렬 조건 반환
    public List<OrderBy> SummaryOrderList() {
        List<OrderBy> orderBys = new ArrayList<>();
        OrderBy orderBy = new OrderBy().setFieldName("pageviews")
                .setSortOrder("descending");
        orderBys.add(orderBy);

        return orderBys;
    }

    // 요약 조회용 Metrics List 반환
    public List<Metric> SummarySearchMetricsList(String[] metricsArray) {
        // Metrics(조회할 컬럼) 객체 생성
        List<Metric> metricsList = new ArrayList<>();

        for(String s : metricsArray) {
            Metric metric = new Metric().setExpression("ga:" + s)
                                        .setAlias(s);
            metricsList.add(metric);
        }

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
