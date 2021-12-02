package com.google.googleanalytics.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.AnalyticsReportingScopes;
import com.google.googleanalytics.domain.ConnectionModel;
import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;

@Controller
@Getter
@RequestMapping("/")
public class AnalyticsConnectionController {

    public static final String APPLICATION_NAME = "Analytics Report";
    public static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    public static final String KEY_FILE_LOCATION = "C:\\workspace\\analytics-web\\src\\main\\resources\\userdata\\analytics.json";
    public static String VIEW_ID = "";
    public static AnalyticsReporting service;

    // DB에 등록된 회원정보 조회
    @GetMapping("searchExistViewId")
    @ResponseBody
    public String searchExistViewId() {




        return "1111";
    }

    // Google Analytics API VIEW_ID 설정 및 연동처리
    @PostMapping("connect")
    public String analyticsConnectPost(ConnectionModel form) {
        try {
            VIEW_ID = form.getViewId(); // VIEW_ID는 API 호출 시 사용됨
            service = initializeAnalyticsReporting();
            return "SearchMain";
        } catch (Exception e) {
            e.printStackTrace();
            return "CredentialErrorMain";
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
