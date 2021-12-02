package com.google.googleanalytics.service;

import com.google.googleanalytics.domain.ExistViewIdEntity;
import com.google.googleanalytics.repository.ExistViewIdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Service
public class AnalyticsConnectionService {

    @Autowired
    public ExistViewIdRepository existViewIdRepository;

    public List<ExistViewIdEntity> searchExistViewId() {
        return existViewIdRepository.findAll();
    }



}
