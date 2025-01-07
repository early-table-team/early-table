package com.gotcha.earlytable.domain.store.entity;

import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "interest_store")
public class InterestStore extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interestStoreId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    public InterestStore() {}

    public InterestStore(User user, Store store) {
        addUser(user);
        this.store = store;
    }

    private void addUser(User user) {
        this.user = user;
        user.getInterestStoreList().add(this);
    }
}
