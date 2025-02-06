package com.gotcha.earlytable.domain.file.entity;


import com.gotcha.earlytable.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@Table(name = "file")
public class File extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FileDetail> fileDetailList = new ArrayList<>();


    public File() {

    }

    // 테스트 용
    public void setId(long l) {
        fileId = l;
    }
}
