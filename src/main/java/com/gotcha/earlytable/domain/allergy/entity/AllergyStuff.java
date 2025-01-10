package com.gotcha.earlytable.domain.allergy.entity;

import com.gotcha.earlytable.domain.allergy.dto.AllergyStuffRequestDto;
import com.gotcha.earlytable.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "allergy_stuff")
public class AllergyStuff extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long allergyStuffId;

    @Column(nullable = false)
    private String allergyStuff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "allergy_category_id")
    private AllergyCategory allergyCategory;

    @OneToMany(mappedBy = "allergyStuff", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Allergy> allergyList = new ArrayList<>();

    public AllergyStuff() {

    }

    public AllergyStuff(String allergyStuff, AllergyCategory allergyCategory) {
        this.allergyStuff = allergyStuff;
        this.allergyCategory = allergyCategory;
    }

    public void update(AllergyStuffRequestDto allergyStuffRequestDto) {
        if(allergyStuffRequestDto.getAllergyStuff() != null) {
            this.allergyStuff = allergyStuffRequestDto.getAllergyStuff();
        }
    }
}
