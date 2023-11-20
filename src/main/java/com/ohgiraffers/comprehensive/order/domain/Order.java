package com.ohgiraffers.comprehensive.order.domain;

import com.ohgiraffers.comprehensive.product.domain.Product;
import lombok.AccessLevel;
import lombok.Cleanup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name="tbl_order")
@NoArgsConstructor(access = PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class) // 발생 시간 (수정은 x)
public class Order {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long orderCode;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "productCode")
    private Product product;

    @Column(nullable = false)
    private Long memberCode;

    @Column(nullable = false)
    private String orderPhone;

    @Column(nullable = false)
    private String orderEmail;

    @Column(nullable = false)
    private String orderReceiver;

    @Column(nullable = false)
    private String orderAddress;

    @Column(nullable = false)
    private Long orderAmount;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime orderDate;

    public Order(Product product, Long memberCode, String orderPhone, String orderEmail, String orderReceiver, String orderAddress, Long orderAmount) {
        this.product = product;
        this.memberCode = memberCode;
        this.orderPhone = orderPhone;
        this.orderEmail = orderEmail;
        this.orderReceiver = orderReceiver;
        this.orderAddress = orderAddress;
        this.orderAmount = orderAmount;
    }

    public static Order of(Product product, Long memberCode, String orderPhone, String orderEmail, String orderReceiver, String orderAddress, Long orderAmount) {

        return new Order(
          product, memberCode, orderPhone, orderEmail, orderReceiver, orderAddress, orderAmount
        );
    }
}
