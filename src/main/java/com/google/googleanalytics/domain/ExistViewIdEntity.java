package com.google.googleanalytics.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "view_id")
public class ExistViewIdEntity {

    @Id
    @Column(name = "view_id")
    String viewId;
    String remark;

}