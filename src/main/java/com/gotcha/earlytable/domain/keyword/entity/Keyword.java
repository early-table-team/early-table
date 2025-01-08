package com.gotcha.earlytable.domain.keyword.entity;

import com.gotcha.earlytable.domain.keyword.dto.KeywordRequestDto;
import com.gotcha.earlytable.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "keyword")
public class Keyword extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long keywordId;

    @Column(nullable = false)
    private String keyword;

    @OneToMany(mappedBy = "keyword", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoreKeyword> storeKeywordList = new ArrayList<>();

    public Keyword(String keyword) {
        this.keyword = keyword;
    }

    public Keyword() {

    }

    public void updateKeyword(KeywordRequestDto requestDto) {
        if(requestDto.getKeyword() != null) {
            this.keyword = requestDto.getKeyword();
        }
    }
}
