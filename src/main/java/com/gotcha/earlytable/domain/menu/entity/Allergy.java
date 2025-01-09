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
    @JoinColumn(name = "allergy_stuff_id")
    private AllergyStuff allergyStuff;

    public Allergy(Menu menu, AllergyStuff allergyStuff) {
        addMenu(menu);
        addAllergyStuff(allergyStuff);
    }

    public Allergy() {

    }

    public void addMenu(Menu menu) {
        this.menu = menu;
        menu.getAllergyList().add(this);
    }

    public void addAllergyStuff(AllergyStuff allergyStuff) {
        this.allergyStuff = allergyStuff;
        allergyStuff.getAllergyList().add(this);
    }


}
