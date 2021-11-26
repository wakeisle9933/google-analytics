package com.google.googleanalytics.controller;

import com.google.api.services.analyticsreporting.v4.model.*;
import com.google.googleanalytics.service.SearchBlogSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchBlogSummaryController {

    @Autowired
    SearchBlogSummaryService searchBlogSummaryService;

    @GetMapping("searchTotalPageviews")
    public ModelAndView SearchBlogSummary() throws IOException {
        return searchBlogSummaryService.SearchBlogSummary();
    }

}
