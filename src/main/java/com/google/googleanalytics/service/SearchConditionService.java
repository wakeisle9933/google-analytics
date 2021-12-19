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
    public DateRange SummaryDateRange(String fromDate, String toDate) {
        DateRange dateRange = new DateRange();

        dateRange.setStartDate(fromDate);
        dateRange.setEndDate(toDate);

        /*
        dateRange.setStartDate("1DaysAgo");
        dateRange.setEndDate("today");
        */

        return dateRange;
    }

    // 정렬 조건 반환
    public List<OrderBy> SummaryOrderList(String[] ordersArray) {
        List<OrderBy> orderBys = new ArrayList<>();

        for(String s : ordersArray) {
            OrderBy orderBy = new OrderBy().setFieldName(s)
                                           .setSortOrder("descending");
            orderBys.add(orderBy);
        }

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
    public List<Dimension> SummarySearchDimensionsList(String[] dimensionsArray) {
        List<Dimension> dimensionsList = new ArrayList<>();

        for(String s : dimensionsArray) {
            Dimension dimension = new Dimension().setName(s);
            dimensionsList.add(dimension);
        }

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