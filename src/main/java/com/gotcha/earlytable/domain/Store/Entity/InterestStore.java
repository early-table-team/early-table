package com.gotcha.earlytable.domain.Store.Entity;

import com.gotcha.earlytable.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "interest_store")
public class InterestStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interestStoreId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store storeId;

    public InterestStore() {}
}
