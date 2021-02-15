package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name="category_id")
    private Long id;

    private String name;

    @ManyToMany // 실무에서는 지양 : 중간 테이블에 컬럼을 추가할 수 없고, 세밀한 쿼리 실행이 어려움
    @JoinTable(name="category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )        // 일대다, 다대일을 풀어내는 중간테이블 필요
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    //==연관관계 메서드==//
    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }
}

/*
모든 연관관계는 지연로딩으로 설정!!
- 즉시로딩은(EAGER)은 예측이 어렵고, 어떤 SQL이 실행될지 추적하기 어려움
- 특히, JPQL을 실행할 때 N+1 문제가 자주 발생
- 실무에서 모든 연관관계는 지연로딩(LAZY)으로 설정해야 함
- 연관된 엔티티를 함께 DB에서 조회해야 하면, fetch join 또는 엔티티 그래프 기능을 사용
- @XToOne(OneToOne, ManyToOne) 관계는 기본이 즉시로딩이므로 직접 지연로딩으로 설정해야 함!!
* */