package com.gotcha.earlytable.domain.file.entity;


import com.gotcha.earlytable.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;


@Getter
@Entity
@Table(name = "file")
public class File extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    public File() {

    }

}
