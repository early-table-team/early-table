package com.gotcha.earlytable.domain.file.entity;


import com.gotcha.earlytable.domain.file.FileStatus;
import com.gotcha.earlytable.domain.file.FileType;
import com.gotcha.earlytable.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "image_file")
public class ImageFile extends BaseEntity {
    @Id
    @GeneratedValue
    private Long imageFileId;

    private String fileName;

    private String fileUniqueName;

    private String fileUrl;

    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private FileType fileType;

    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private FileStatus fileStatus;

    private Double fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private File file;

    private Integer fileSeq;


    public ImageFile(String fileName, String fileUniqueName, String fileUrl,
                     FileType fileType, FileStatus fileStatus, Double fileSize, File file, Integer fileSeq) {
        this.fileName = fileName;
        this.fileUniqueName = fileUniqueName;
        this.fileUrl = fileUrl;
        this.fileType = fileType;
        this.fileStatus = fileStatus;
        this.fileSize = fileSize;
        this.file = file;
        this.fileSeq = fileSeq;

    }

    public ImageFile() {

    }

}
