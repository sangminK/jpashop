package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")   // order by 때문에 orders 많이 사
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name="order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // 외래키가 있는 주문을 연관관계의 주인으로 정하는 것이 좋음 (비즈니스상 우위에 있다고 주인으로 정하는 것 X)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;  // 1:1관계의 경우, FK를 양쪽에 둘 다 둘 수 있다 > 자주 접근하는 쪽에 두는게

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)  // ORDINAL(default) : 숫자, STRING : 문자
    private OrderStatus status; // 주문상태 [ORDER, CANCEL]
}
