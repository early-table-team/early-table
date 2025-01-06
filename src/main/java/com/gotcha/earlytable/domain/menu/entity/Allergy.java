package com.gotcha.earlytable.domain.menu.entity;

import com.gotcha.earlytable.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "allergy")
public class Allergy extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long allergyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "allergy_detail_id")
    private AllergyDetail allergyDetail;

    public Allergy(Menu menu, AllergyDetail allergyDetail) {
        addMenu(menu);
        addAllergyDetail(allergyDetail);
    }

    public Allergy() {

    }

    public void addAllergyDetail(AllergyDetail allergyDetail) {
        this.allergyDetail = allergyDetail;
        allergyDetail.getAllergyList().add(this);
    }

    public void addMenu(Menu menu) {
        this.menu = menu;
        menu.getAllergyList().add(this);
    }
}
