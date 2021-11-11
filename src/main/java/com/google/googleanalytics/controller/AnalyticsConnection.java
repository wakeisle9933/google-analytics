package com.google.googleanalytics.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.AnalyticsReportingScopes;
import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@Getter
@RequestMapping("/")
public class AnalyticsConnection {

    public static final String APPLICATION_NAME = "Analytics Report";
    public static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    public static final String KEY_FILE_LOCATION = "";
    public static final String VIEW_ID = "";
    public static AnalyticsReporting service;

    @GetMapping("connect")
    public String analyticsConnect() {
        try {
            service = initializeAnalyticsReporting();
            return "연결 성공";
        } catch (Exception e) {
            e.printStackTrace();
            return "연결 실패";
        }
    }

    // Google Analytic 연동
    private static AnalyticsReporting initializeAnalyticsReporting() throws GeneralSecurityException, IOException {

        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(KEY_FILE_LOCATION))
                                                      .createScoped(AnalyticsReportingScopes.all());

        // 구글 애널리틱스 Service 객체 생성
        return new AnalyticsReporting.Builder(httpTransport, JSON_FACTORY, credential)
                                     .setApplicationName(APPLICATION_NAME)
                                     .build();
    }


}
