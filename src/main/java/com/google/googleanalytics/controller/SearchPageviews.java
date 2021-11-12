package com.google.googleanalytics.controller;

import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.model.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchPageviews {

    @GetMapping("searchTotalPageviews")
    public ModelAndView SearchTotalPageviews() throws IOException {
        // 날짜 범위 설정
        DateRange dateRange = new DateRange();
        dateRange.setStartDate("7DaysAgo");
        dateRange.setEndDate("today");

        // Metrics(조회할 컬럼) 객체 생성
        Metric pageviews = new Metric().setExpression("ga:pageviews")
                                       .setAlias("pageviews");

        Dimension pageTitle = new Dimension().setName("ga:pageTitle");

        List<OrderBy> orderBys = new ArrayList<>();
        OrderBy orderBy = new OrderBy().setFieldName("pageviews")
                                       .setSortOrder("descending");
        orderBys.add(orderBy);

        // ReportRequest 객체 생성.
        ReportRequest request = new ReportRequest().setViewId(AnalyticsConnection.VIEW_ID)
                                                   .setDateRanges(Arrays.asList(dateRange))
                                                   .setMetrics(Arrays.asList(pageviews))
                                                   .setDimensions(Arrays.asList(pageTitle))
                                                   .setPageSize(10)
                                                   .setOrderBys(orderBys);

        ArrayList<ReportRequest> requests = new ArrayList<ReportRequest>();
        requests.add(request);

        // GetReportsRequest 객체 생성
        GetReportsRequest getReport = new GetReportsRequest().setReportRequests(requests);

        // batchGet 메소드 생성해서 response 받아오기
        GetReportsResponse response = AnalyticsConnection.service.reports().batchGet(getReport).execute();
        // response 콘솔에 출력
        // printResponse(response);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("Result");
        modelAndView.addObject("response", response.getReports());

        LinkedList<String> nameList = new LinkedList<>();
        LinkedList<String> pageviewsList = new LinkedList<>();
        LinkedList<String> dimensionHeaderList = new LinkedList<>();
        LinkedList<String> dimensionList = new LinkedList<>();

        for(Report report: response.getReports()) {

            ColumnHeader header = report.getColumnHeader();
            List<String> dimensionHeaders = header.getDimensions();
            List<MetricHeaderEntry> metricHeaders = header.getMetricHeader().getMetricHeaderEntries();
            List<ReportRow> rows = report.getData().getRows();

            System.out.println(header);
            System.out.println(dimensionHeaders);
            System.out.println(metricHeaders);
            System.out.println(rows);

            for(ReportRow row: rows) {
                List<String> dimensions = row.getDimensions();
                List<DateRangeValues> metrics = row.getMetrics();

                for (int i = 0; i < dimensionHeaders.size() && i < dimensions.size(); i++) {
                    System.out.println(dimensionHeaders.get(i) + ": " + dimensions.get(i));
                    dimensionHeaderList.add(dimensionHeaders.get(i));
                    dimensionList.add(dimensions.get(i));
                }

                for (int j = 0; j < metrics.size(); j++) {
                    DateRangeValues values = metrics.get(j);
                    for (int k = 0; k < values.getValues().size() && k < metricHeaders.size(); k++) {
                        nameList.add(metricHeaders.get(k).getName());
                        pageviewsList.add(values.getValues().get(k));
                    }
                }
            }

            System.out.println("LINE");
            System.out.println(nameList);
            System.out.println(pageviewsList);
            System.out.println(dimensionHeaderList);
            System.out.println(dimensionList);

        }

        modelAndView.addObject("dimensionList", dimensionList);

        return modelAndView;
    }

    // 결과 출력
    public static void printResponse(GetReportsResponse response) {
        for (Report report: response.getReports()) {
            ColumnHeader header = report.getColumnHeader();
            List<String> dimensionHeaders = header.getDimensions();
            List<MetricHeaderEntry> metricHeaders = header.getMetricHeader().getMetricHeaderEntries();
            List<ReportRow> rows = report.getData().getRows();

            if (rows == null) {
                System.out.println("No data found for " + AnalyticsConnection.VIEW_ID);
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
