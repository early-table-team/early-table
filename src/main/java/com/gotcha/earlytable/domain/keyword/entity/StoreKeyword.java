package com.gotcha.earlytable.domain.keyword.entity;

import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "store_keyword")
public class StoreKeyword extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeKeywordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_id")
    private Keyword keyword;


    public StoreKeyword(Store store, Keyword keyword) {
        addStore(store);
        addKeyword(keyword);
    }

    public StoreKeyword() {

    }

    public void addStore(Store store) {
        this.store = store;
        store.getStoreKeywordList().add(this);
    }

    public void addKeyword(Keyword keyword) {
        this.keyword = keyword;
        keyword.getStoreKeywordList().add(this);
    }
}
