package com.gotcha.earlytable.domain.file.entity;


import com.gotcha.earlytable.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@Table(name = "file")
@BatchSize(size = 100)
public class File extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FileDetail> fileDetailList = new ArrayList<>();


    public File() {

    }

}
