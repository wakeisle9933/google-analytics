package com.google.googleanalytics.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "view_id")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExistViewIdEntity {

    @Id
    @Column(name = "view_id")
    String viewId;
    String remark;

}