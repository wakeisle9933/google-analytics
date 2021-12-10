package com.google.googleanalytics.repository;

import com.google.googleanalytics.domain.CategoryEntity;
import com.google.googleanalytics.domain.CategoryEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Id;
import java.util.List;

@Repository
public interface CategoryEntityRepository extends JpaRepository<CategoryEntity, CategoryEntityId> {
    @Query(value = "select view_id, minor_category, major_category, major_category_name, minor_category_name from category where view_id = :view_id and minor_category = :minor_category", nativeQuery=true)
    List<CategoryEntity> searchCategoryName(@Param("view_id") String view_id, @Param("minor_category") String minor_category);
}