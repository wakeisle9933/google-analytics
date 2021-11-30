package com.google.googleanalytics.service;

import com.google.api.services.analyticsreporting.v4.model.*;
import com.google.googleanalytics.controller.AnalyticsConnectionController;
import com.google.googleanalytics.domain.SearchBlogSummaryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
public class SearchBlogSummaryService {

    @Autowired
    SearchConditionService searchConditionService;


    public ModelAndView SearchBlogSummary() throws IOException {
        // 정렬기준 설정
        List<OrderBy> orderBys = new ArrayList<>();
        OrderBy orderBy = new OrderBy().setFieldName("pageviews")
                .setSortOrder("descending");
        orderBys.add(orderBy);

        // ReportRequest 객체 생성.
        ReportRequest request = new ReportRequest().setViewId(AnalyticsConnectionController.VIEW_ID)
                                                   .setDateRanges(Arrays.asList(searchConditionService.SummaryDateRange()))
                                                   .setMetrics(searchConditionService.SummarySearchMetricsList())
                                                   .setDimensions(searchConditionService.SummarySearchDimensionsList())
                                                   .setPageSize(100000)
                                                   .setOrderBys(orderBys);

        ArrayList<ReportRequest> requests = new ArrayList<>();
        requests.add(request);

        // GetReportsRequest 객체 생성
        GetReportsRequest getReport = new GetReportsRequest().setReportRequests(requests);

        // batchGet 메소드 생성해서 response 받아오기
        GetReportsResponse response = AnalyticsConnectionController.service.reports().batchGet(getReport).execute();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("Result");
        modelAndView.addObject("response", response.getReports());

        // 반환용 List
        LinkedList<SearchBlogSummaryModel> summaryList = new LinkedList<>();

        for(Report report: response.getReports()) {

            ColumnHeader header = report.getColumnHeader();
            List<String> dimensionHeaders = header.getDimensions();
            List<MetricHeaderEntry> metricHeaders = header.getMetricHeader().getMetricHeaderEntries();
            List<ReportRow> rows = report.getData().getRows();
            int count = 1;

            for(ReportRow row: rows) {
                List<String> dimensions = row.getDimensions();
                List<DateRangeValues> metrics = row.getMetrics();
                SearchBlogSummaryModel model = new SearchBlogSummaryModel();
                model.setPostNumber(count); // postNumber
                count++;

                for (int i = 0; i < dimensionHeaders.size() && i < dimensions.size(); i++) {
                    model.setPagePath(dimensions.get(0)); // pagePath
                    model.setPostName(dimensions.get(1)); // postName
                }

                for (int j = 0; j < metrics.size(); j++) {
                    DateRangeValues values = metrics.get(j);
                    model.setPageViews(values.getValues().get(0)); // pageViews
                    model.setAdsenseRevenue(BigDecimal.valueOf(Double.parseDouble(values.getValues().get(1))).toString()); // adsenseRevenue
                    model.setAdsenseAdsClicks(values.getValues().get(2)); // adsenseAdsClicks
                }
                summaryList.add(model);
            }
        }

        modelAndView.addObject("summaryModel", summaryList);

        return modelAndView;
    }

    public ModelAndView SearchBlogSummaryBaseRevenue() throws IOException {
        // 날짜 범위 설정
        DateRange dateRange = new DateRange();
        dateRange.setStartDate("7DaysAgo");
        dateRange.setEndDate("today");

        // Metrics(조회할 컬럼) 객체 생성
        List<Metric> findMetrics = new ArrayList<>();
        Metric pageviews = new Metric().setExpression("ga:pageviews")
                                       .setAlias("pageviews");

        Metric adsenseRevenue = new Metric().setExpression("ga:adsenseRevenue")
                                            .setAlias("adsenseRevenue");

        Metric adsenseAdsClicks = new Metric().setExpression("ga:adsenseAdsClicks")
                                              .setAlias("adsenseAdsClicks");

        findMetrics.add(pageviews);
        findMetrics.add(adsenseRevenue);
        findMetrics.add(adsenseAdsClicks);

        Dimension pageTitle = new Dimension().setName("ga:pageTitle");

        // 정렬기준 설정
        List<OrderBy> orderBys = new ArrayList<>();
        OrderBy orderBy = new OrderBy().setFieldName("ga:adsenseRevenue")
                .setSortOrder("descending");
        orderBys.add(orderBy);

        // ReportRequest 객체 생성.
        ReportRequest request = new ReportRequest().setViewId(AnalyticsConnectionController.VIEW_ID)
                .setDateRanges(Arrays.asList(dateRange))
                .setMetrics(findMetrics)
                .setDimensions(Arrays.asList(pageTitle))
                .setPageSize(100)
                .setOrderBys(orderBys);

        ArrayList<ReportRequest> requests = new ArrayList<>();
        requests.add(request);

        // GetReportsRequest 객체 생성
        GetReportsRequest getReport = new GetReportsRequest().setReportRequests(requests);

        // batchGet 메소드 생성해서 response 받아오기
        GetReportsResponse response = AnalyticsConnectionController.service.reports().batchGet(getReport).execute();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("Result");
        modelAndView.addObject("response", response.getReports());

        // 반환용 List
        LinkedList<SearchBlogSummaryModel> summaryList = new LinkedList<>();

        for(Report report: response.getReports()) {

            ColumnHeader header = report.getColumnHeader();
            List<String> dimensionHeaders = header.getDimensions();
            List<MetricHeaderEntry> metricHeaders = header.getMetricHeader().getMetricHeaderEntries();
            List<ReportRow> rows = report.getData().getRows();
            int count = 1;

            for(ReportRow row: rows) {
                List<String> dimensions = row.getDimensions();
                List<DateRangeValues> metrics = row.getMetrics();
                SearchBlogSummaryModel model = new SearchBlogSummaryModel();
                model.setPostNumber(count); // postNumber
                count++;

                for (int i = 0; i < dimensionHeaders.size() && i < dimensions.size(); i++) {
                    model.setPostName(dimensions.get(i)); // postName
                }

                for (int j = 0; j < metrics.size(); j++) {
                    DateRangeValues values = metrics.get(j);
                    model.setPageViews(values.getValues().get(0)); // pageViews
                    model.setAdsenseRevenue(BigDecimal.valueOf(Double.parseDouble(values.getValues().get(1))).toString()); // adsenseRevenue
                    model.setAdsenseAdsClicks(values.getValues().get(2)); // adsenseAdsClicks
                }
                summaryList.add(model);
            }
        }

        modelAndView.addObject("summaryModel", summaryList);

        return modelAndView;
    }



    // 결과 출력 (지금 미사용중)
    public static void printResponse(GetReportsResponse response) {
        for (Report report: response.getReports()) {
            ColumnHeader header = report.getColumnHeader();
            List<String> dimensionHeaders = header.getDimensions();
            List<MetricHeaderEntry> metricHeaders = header.getMetricHeader().getMetricHeaderEntries();
            List<ReportRow> rows = report.getData().getRows();

            if (rows == null) {
                System.out.println("No data found for " + AnalyticsConnectionController.VIEW_ID);
                return;
            }

            for (ReportRow row: rows) {
                List<String> dimensions = row.getDimensions();
                List<DateRangeValues> metrics = row.getMetrics();

                for (int i = 0; i < dimensionHeaders.size() && i < dimensions.size(); i++) {
                    System.out.println(dimensionHeaders.get(i) + ": " + dimensions.get(i));
                }

                for (int j = 0; j < metrics.size(); j++) {
                    System.out.println("Date Range (" + j + "): ");
                    DateRangeValues values = metrics.get(j);
                    for (int k = 0; k < values.getValues().size() && k < metricHeaders.size(); k++) {
                        System.out.println(metricHeaders.get(k).getName() + ": " + values.getValues().get(k));
                    }
                }
            }
        }
    }
}
