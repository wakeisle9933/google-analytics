package com.google.googleanalytics.domain;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CategoryEntityId implements Serializable {
    String view_id;
    String minor_category;
}
