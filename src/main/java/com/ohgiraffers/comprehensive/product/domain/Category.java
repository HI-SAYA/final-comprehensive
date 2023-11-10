package com.ohgiraffers.comprehensive.product.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_category")
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Category {

    @Id
    private Long categoryCode;

    @Column(nullable = false)
    private String categoryName;
}
