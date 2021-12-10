package com.google.googleanalytics.service;

import com.google.api.services.analyticsreporting.v4.model.*;
import com.google.googleanalytics.controller.AnalyticsConnectionController;
import com.google.googleanalytics.domain.CategoryEntity;
import com.google.googleanalytics.domain.SearchBlogCategorySummaryModel;
import com.google.googleanalytics.domain.SearchBlogSummaryModel;
import com.google.googleanalytics.repository.CategoryEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchBlogSummaryService {

    @Autowired
    SearchConditionService searchConditionService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    CategoryEntityRepository categoryEntityRepository;

    public ModelAndView SearchBlogSummary() throws IOException {

        String[] metricsArray = {"pageviews", "adsenseRevenue", "adsenseAdsClicks"};
        String[] dimensionsArray = {"ga:pageTitle"};
        String[] orderByArray = {"pageviews"};

        // ReportRequest 객체 생성.
        ReportRequest request = new ReportRequest().setViewId(AnalyticsConnectionController.VIEW_ID)
                                                   .setDateRanges(Arrays.asList(searchConditionService.SummaryDateRange()))
                                                   .setMetrics(searchConditionService.SummarySearchMetricsList(metricsArray))
                                                   .setDimensions(searchConditionService.SummarySearchDimensionsList(dimensionsArray))
                                                   .setPageSize(100000) // max size
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

        // Response 보관
        LinkedList<SearchBlogSummaryModel> summaryList = new LinkedList<>();
        // 최종 반환용 List
        LinkedList<SearchBlogCategorySummaryModel> categorySummaryModel = new LinkedList<>();

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

        // API에서 호출한 데이터 가공(카테고리 꺼내오기)
        for(SearchBlogSummaryModel model : summaryList) {
            if(model.getPagePath().contains("?category=")) { // 카테고리가 들어있을 경우
                String categoryValue = model.getPagePath();
                String[] paramFinder = categoryValue.split("");
                int paramIndex = Arrays.asList(paramFinder).indexOf("=") + 1; // 인덱스 다음 글자부터 카테고리(총 6글자)
                int paramQuestionIndex = Arrays.asList(paramFinder).indexOf("?");

                // 티스토리 내 카테고리는 모두 6~7 글자임
                if(categoryValue.substring(paramIndex).length() == 6 || categoryValue.substring(paramIndex).length() == 7) {
                    categorySet.add(categoryValue.substring(paramIndex));
                }

            } else {
                // nothing
            }
        }

        // 카테고리에 해당하는 Page 주소 집어넣기
        for(String set : categorySet) {
            LinkedHashSet<String> pageSet = new LinkedHashSet<>();

            // 카테고리에 해당하는 Page 주소 집어넣기
            for(SearchBlogSummaryModel model : summaryList) {
                String[] paramFinder = model.getPagePath().split("");
                String categoryValue = model.getPagePath();
                int paramQuestionIndex = Arrays.asList(paramFinder).indexOf("?");

                if(model.getPagePath().contains("?category=") && model.getPagePath().contains(set)) {
                    if(categoryValue.substring(0,2).equals("/m")) { // 모바일일 경우
                        pageSet.add(categoryValue.substring(3, paramQuestionIndex));
                    } else { // PC
                        pageSet.add(categoryValue.substring(1, paramQuestionIndex));
                    }
                }
            }

            int accumulatePageViews = 0;
            Double accumulateAdsenseRevenue = 0D;
            int accumulateAdsenseAdsClicks = 0;

            // 카테고리별 누적 pageViews / adsenseRevenue / adsenseAdsClicks 계산
            System.out.println("category : " + set + " pageSet Test : " + pageSet);
            for(SearchBlogSummaryModel model : summaryList) {
                String[] paramFinder = model.getPagePath().split("");
                String categoryValue = model.getPagePath();
                int paramSlashIndex = Arrays.asList(paramFinder).indexOf("/");
                int paramQuestionIndex = Arrays.asList(paramFinder).indexOf("?");

                if(model.getPagePath().length() >= 3 && model.getPagePath().length() <= 6 && paramQuestionIndex == -1) {
                    if(model.getPagePath().substring(0,3).equals("/m/")) {
                        for(String s : pageSet) {
                            if(model.getPagePath().substring(4).equals(s)) {
                                accumulatePageViews = accumulatePageViews + Integer.parseInt(model.getPageViews());
                                accumulateAdsenseRevenue = accumulateAdsenseRevenue + Double.parseDouble(model.getAdsenseRevenue());
                                accumulateAdsenseAdsClicks = accumulateAdsenseAdsClicks + Integer.parseInt(model.getAdsenseAdsClicks());
                            }
                        }
                    } else {
                        for(String s : pageSet) {
                            if(model.getPagePath().substring(1).equals(s)) {
                                accumulatePageViews = accumulatePageViews + Integer.parseInt(model.getPageViews());
                                accumulateAdsenseRevenue = accumulateAdsenseRevenue + Double.parseDouble(model.getAdsenseRevenue());
                                accumulateAdsenseAdsClicks = accumulateAdsenseAdsClicks + Integer.parseInt(model.getAdsenseAdsClicks());
                            }
                        }
                    }
                } else if(model.getPagePath().contains("?category=")) {
                    if(model.getPagePath().substring(0,3).equals("/m/")) {
                        for(String s : pageSet) {
                            if(model.getPagePath().substring(4, paramQuestionIndex).equals(s)) {
                                accumulatePageViews = accumulatePageViews + Integer.parseInt(model.getPageViews());
                                accumulateAdsenseRevenue = accumulateAdsenseRevenue + Double.parseDouble(model.getAdsenseRevenue());
                                accumulateAdsenseAdsClicks = accumulateAdsenseAdsClicks + Integer.parseInt(model.getAdsenseAdsClicks());
                            }
                        }
                    } else {
                        for(String s : pageSet) {
                            if(model.getPagePath().substring(1, paramQuestionIndex).equals(s)) {
                                accumulatePageViews = accumulatePageViews + Integer.parseInt(model.getPageViews());
                                accumulateAdsenseRevenue = accumulateAdsenseRevenue + Double.parseDouble(model.getAdsenseRevenue());
                                accumulateAdsenseAdsClicks = accumulateAdsenseAdsClicks + Integer.parseInt(model.getAdsenseAdsClicks());
                            }
                        }
                    }
                }
            }

            SearchBlogCategorySummaryModel tempModel = new SearchBlogCategorySummaryModel();
            tempModel.setCategory(set);

            // category_name 가져오기
            List<CategoryEntity> categoryEntities = categoryEntityRepository.searchCategoryName(AnalyticsConnectionController.VIEW_ID, set);
            tempModel.setCategoryName(categoryEntities.get(0).getMinor_category_name());

            tempModel.setTotalPageViews(String.valueOf(accumulatePageViews));
            tempModel.setTotalAdsenseRevenue(String.valueOf(accumulateAdsenseRevenue));
            tempModel.setTotalAdsenseAdsClicks(String.valueOf(accumulateAdsenseAdsClicks));
            categorySummaryModel.add(tempModel);
        }

        // pageview 기준 정렬처리
        Collections.sort(categorySummaryModel, (a, b) -> Integer.parseInt(b.getTotalPageViews()) - Integer.parseInt(a.getTotalPageViews()));

        // 순번 설정
        int number = 1;
        for(int i = 0; i<categorySummaryModel.size(); i++) {
            categorySummaryModel.get(i).setPostNumber(number);
            number++;
        }

        modelAndView.addObject("summaryModel", categorySummaryModel);
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
