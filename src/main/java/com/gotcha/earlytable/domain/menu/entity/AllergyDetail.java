package com.gotcha.earlytable.domain.menu.entity;

import com.gotcha.earlytable.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "allergy_detail")
public class AllergyDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long allergyDetailId;

    @Column(nullable = false)
    private String allergyName;

    @Column(nullable = false)
    private String allergyStuff;

    private String allergyContents;

    @OneToMany(mappedBy = "allergyDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Allergy> allergyList = new ArrayList<>();

    public AllergyDetail(String allergyName, String allergyStuff, String allergyContents) {
        this.allergyName = allergyName;
        this.allergyStuff = allergyStuff;
        this.allergyContents = allergyContents;
    }

    public AllergyDetail() {

    }
}
