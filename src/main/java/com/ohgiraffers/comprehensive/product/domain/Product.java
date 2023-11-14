package com.ohgiraffers.comprehensive.product.domain;

import com.ohgiraffers.comprehensive.product.domain.type.ProductStatusType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.time.LocalDateTime;
import static com.ohgiraffers.comprehensive.product.domain.type.ProductStatusType.USABLE;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_product")
@NoArgsConstructor(access = PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class) // JPA에서 엔터티의 생명주기 이벤트를 수신하는 리스너를 지정하기 위한 어노테이션
@SQLDelete(sql = "UPDATE tbl_product SET status = 'DELETED' WHERE product_code = ?")
public class Product {

    @Id
    @GeneratedValue(strategy = IDENTITY) // MySql
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


    public Product(String productName, Long productPrice, String productDescription, Category category, String productImageUrl, Long productStock) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.category = category;
        this.productImageUrl = productImageUrl;
        this.productStock = productStock;
    }

    public static Product of(
            final String productName, final Long productPrice, final String productDescription,
            final Category category, final String productImageUrl, final Long productStock
                                ) {

        return new Product(
                productName,
                productPrice,
                productDescription,
                category,
                productImageUrl,
                productStock
        );
    }

    public void updateProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public void update(String productName, Long productPrice, String productDescription,
                       Category category, Long productStock, ProductStatusType status) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.category = category;
        this.productStock = productStock;
        this.status = status;
    }


}
