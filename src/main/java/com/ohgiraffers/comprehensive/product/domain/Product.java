package com.ohgiraffers.comprehensive.product.domain;

import com.ohgiraffers.comprehensive.product.domain.type.ProductStatusType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.time.LocalDateTime;
import static com.ohgiraffers.comprehensive.product.domain.type.ProductStatusType.USABLE;
import static javax.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_product")
@NoArgsConstructor(access = PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @Id
    private Long productCode;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private Long productPrice;

    @Column(nullable = false)
    private String productDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryCode")
    private Category category;

    @Column(nullable = false)
    private String productImageUrl;

    @Column(nullable = false)
    private Long productStock;

    @CreatedDate    // jpa에서 감지된다. 해당 행이 수정된 순간 자동으로 처리해준다.
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate // ..
    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private ProductStatusType status = USABLE;
}
