package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)  // 외래키가 있는 주문상품이 연관관계의 주인
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문 가격

    private int count;      // 주문 수량

    /*
    * Order.createOrderItem 으로 생성하는데
    * 누군가는 OrderItem orderItem = new OrderItem(); orderItem.set...으로 생성해버릴 수 있다.
    * 막아버리기 위해
    * 방법1. protected OrderItem() {}
    * 방법2. @NoArgsConstructor(access = AccessLevel.PROTECTED)
    * */

    //==생성 메서드==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count); // 주문 상품 생성과 동시에 재고를 까는 방식(EXTS.무통장 입금이라면?)
        return orderItem;
    }

    //==비즈니스 로직==//
    public void cancel() {
        getItem().addStock(count);
    }

    //==조회 로직==//
    /*
    * 주문상품 전체 가격 조회
    * */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
