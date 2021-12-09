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
import java.util.*;

@Service
public class SearchBlogSummaryService {

    @Autowired
    SearchConditionService searchConditionService;

    public ModelAndView SearchBlogSummary() throws IOException {

        String[] metricsArray = {"pageviews", "adsenseRevenue", "adsenseAdsClicks"};
        String[] dimensionsArray = {"ga:pageTitle"};
        String[] orderByArray = {"pageviews"};

        // ReportRequest 객체 생성.
        ReportRequest request = new ReportRequest().setViewId(AnalyticsConnectionController.VIEW_ID)
                                                   .setDateRanges(Arrays.asList(searchConditionService.SummaryDateRange()))
                                                   .setMetrics(searchConditionService.SummarySearchMetricsList(metricsArray))
                                                   .setDimensions(searchConditionService.SummarySearchDimensionsList(dimensionsArray))
                                                   .setPageSize(100000)
                                                   .setOrderBys(searchConditionService.SummaryOrderList(orderByArray));

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
                    model.setPostName(dimensions.get(0)); // postName
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

    // 카테고리별 조회수 / 수익 구하기
    public ModelAndView SearchBlogSummaryCategory() throws IOException {

        String[] metricsArray = {"pageviews", "adsenseRevenue", "adsenseAdsClicks"};

        // ReportRequest 객체 생성.
        ReportRequest request = new ReportRequest().setViewId(AnalyticsConnectionController.VIEW_ID)
                                                    .setDateRanges(Arrays.asList(searchConditionService.SummaryDateRange()))
                                                    .setMetrics(searchConditionService.SummarySearchMetricsList(metricsArray))
                                                    .setDimensions(searchConditionService.SummaryCategorySearchDimensionsList())
                                                    .setPageSize(100000);

        ArrayList<ReportRequest> requests = new ArrayList<>();
        requests.add(request);

        // GetReportsRequest 객체 생성
        GetReportsRequest getReport = new GetReportsRequest().setReportRequests(requests);

        // batchGet 메소드 생성해서 response 받아오기
        GetReportsResponse response = AnalyticsConnectionController.service.reports().batchGet(getReport).execute();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("CategorySummaryResult");
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

        HashMap<String, HashSet<String>> map = new HashMap<>();
        LinkedHashSet<String> categorySet = new LinkedHashSet<>();

        for(SearchBlogSummaryModel model : summaryList) {
            if(model.getPagePath().contains("?category=")) { // 카테고리가 들어있을 경우
                String categoryValue = model.getPagePath();
                String[] paramFinder = categoryValue.split("");
                int paramIndex = Arrays.asList(paramFinder).indexOf("=") + 1;
                int paramQuestionIndex = Arrays.asList(paramFinder).indexOf("?");
                HashSet<String> set = new HashSet<>();

                System.out.println("인덱스 위치 : " + paramIndex + " CHK!!! : " + model.getPagePath() + " " +  categoryValue.substring(paramIndex));
                categorySet.add(categoryValue.substring(paramIndex));

                if(categoryValue.substring(0,2).equals("/m")) { // 모바일일 경우
                    System.out.println("Mobile : " + categoryValue.substring(3, paramQuestionIndex));
                    set.add(categoryValue.substring(3, paramQuestionIndex));
                } else { // 그 외의 경우
                    System.out.println("PC : " + categoryValue.substring(1, paramQuestionIndex));
                    set.add(categoryValue.substring(1, paramQuestionIndex));
                }

                map.put(categoryValue.substring(paramIndex), set);

                // map.put(model.getPagePath().substring(paramIndex, 10), 1);

                // CHK!!! : /12?category=728465
                // CHK!!! : /m/529?category=748186
            } else {
                // nothing
            }
        }

        System.out.println("categorySet : " + categorySet);
        System.out.println(" MAP : " + map); // map에

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
