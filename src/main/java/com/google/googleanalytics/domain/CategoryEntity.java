package com.google.googleanalytics.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "category")
@Table(name = "category")
@IdClass(CategoryEntityId.class)
public class CategoryEntity implements Serializable {

    @Id
    String view_id;
    @Id
    String minor_category;
    String major_category;
    String major_category_name;
    String minor_category_name;

}