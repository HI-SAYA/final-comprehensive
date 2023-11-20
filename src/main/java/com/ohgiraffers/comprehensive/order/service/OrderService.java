package com.ohgiraffers.comprehensive.order.service;

import com.ohgiraffers.comprehensive.common.exception.ConflictException;
import com.ohgiraffers.comprehensive.common.exception.NotFoundException;
import com.ohgiraffers.comprehensive.jwt.CustomUser;
import com.ohgiraffers.comprehensive.order.domain.Order;
import com.ohgiraffers.comprehensive.order.dto.reqeust.OrderCreateRequest;
import com.ohgiraffers.comprehensive.order.domain.repository.OrderRepository;
import com.ohgiraffers.comprehensive.order.dto.response.OrderResponse;
import com.ohgiraffers.comprehensive.product.domain.Product;
import com.ohgiraffers.comprehensive.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ohgiraffers.comprehensive.common.exception.type.ExceptionCode.NOT_ENOUGH_STOCK;
import static com.ohgiraffers.comprehensive.common.exception.type.ExceptionCode.NOT_FOUND_PRODUCT_CODE;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    public void save(OrderCreateRequest orderRequest, CustomUser customUser) {

        Product product = productRepository.findById(orderRequest.getProductCode())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_PRODUCT_CODE));

        /* 주문 가능 여부 확인 */
        if(product.getProductStock() < orderRequest.getOrderAmount()) {
            throw new ConflictException(NOT_ENOUGH_STOCK);
        }

        /* 재고수정 */
        product.updateStock(orderRequest.getOrderAmount());

        final Order newOrder = Order.of(
                product,
                customUser.getMemberCode(),
                orderRequest.getOrderPhone(),
                orderRequest.getOrderEmail(),
                orderRequest.getOrderReceiver(),
                orderRequest.getOrderAddress(),
                orderRequest.getOrderAmount()
        );

        /* 주문 저장 */
        orderRepository.save(newOrder);
    }

    private Pageable getPageable(final Integer page) {
        return PageRequest.of(page -1, 5, Sort.by("orderCode").descending());
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> getOrders(Integer page, CustomUser customUser) {

        Page<Order> orders = orderRepository.findByMemberCode(getPageable(page), customUser.getMemberCode());

        return orders.map(order -> OrderResponse.from(order));
    }
}
