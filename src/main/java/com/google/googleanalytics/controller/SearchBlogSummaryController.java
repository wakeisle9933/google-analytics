package com.google.googleanalytics.controller;

import com.google.api.services.analyticsreporting.v4.model.*;
import com.google.googleanalytics.service.SearchBlogSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@RestController
@RequestMapping("/search")
public class SearchBlogSummaryController {

    @Autowired
    SearchBlogSummaryService searchBlogSummaryService;

    // 조회수 순 조회
    @GetMapping("searchBlogSummaryBasePageViews")
    public ModelAndView SearchBlogSummary() throws IOException {
        return searchBlogSummaryService.SearchBlogSummary();
    }

    // 수익 순 조회
    @GetMapping("searchBlogSummaryBaseRevenue")
    public ModelAndView SearchBlogSummaryBaseRevenue() throws IOException {
        return searchBlogSummaryService.SearchBlogSummaryBaseRevenue();
    }

    // 신규 글 순으로 조회

    // 카테고리 별 조회
    @GetMapping("searchBlogSummaryCategory")
    public ModelAndView SearchBlogSummaryCategory() throws IOException {
        return searchBlogSummaryService.SearchBlogSummaryCategory();
    }

    // 블로그 내부 검색 리스트 조회

    // 조회 후 특정 키워드만 분리하여 수익 계산

}
