package com.gotcha.earlytable.domain.allergy.entity;

import com.gotcha.earlytable.domain.allergy.dto.AllergyCategoryRequestDto;
import com.gotcha.earlytable.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "allergy_category")
public class AllergyCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long allergyCategoryId;

    @Column(nullable = false)
    private String allergyCategory;

    public AllergyCategory(String allergyCategory) {
        this.allergyCategory = allergyCategory;
    }

    public AllergyCategory() {

    }

    public void update(AllergyCategoryRequestDto allergyCategoryRequestDto) {
        if(allergyCategoryRequestDto.getAllergyCategory() != null) {
            this.allergyCategory = allergyCategoryRequestDto.getAllergyCategory();
        }
    }
}
