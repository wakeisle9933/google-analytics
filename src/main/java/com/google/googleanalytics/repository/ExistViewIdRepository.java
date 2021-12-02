package com.google.googleanalytics.repository;

import com.google.googleanalytics.domain.ExistViewIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExistViewIdRepository extends JpaRepository<ExistViewIdEntity, String> {
}