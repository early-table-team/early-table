package com.gotcha.earlytable.domain.file.entity;


import com.gotcha.earlytable.domain.file.dto.FileDetailDto;
import com.gotcha.earlytable.domain.file.enums.FileStatus;
import com.gotcha.earlytable.domain.file.enums.FileType;
import com.gotcha.earlytable.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "image_file")
public class FileDetail extends BaseEntity {
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

    private Long fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private File file;

    private Integer fileSeq;


    public FileDetail(String fileName, String fileUniqueName, String fileUrl,
                      FileType fileType, FileStatus fileStatus, Long fileSize, File file, Integer fileSeq) {
        this.fileName = fileName;
        this.fileUniqueName = fileUniqueName;
        this.fileUrl = fileUrl;
        this.fileType = fileType;
        this.fileStatus = fileStatus;
        this.fileSize = fileSize;
        this.file = file;
        this.fileSeq = fileSeq;

    }

    public FileDetail() {

    }

    public static FileDetail toEntity(FileStatus fileStatus, File file, int seq,
                                      FileType extension, FileDetailDto fileDetailDto) {

        return new FileDetail(
                fileDetailDto.getFileName(), fileDetailDto.getFileUniqueName(),
                fileDetailDto.getFileUrl(), extension,
                fileStatus, fileDetailDto.getFileSize(),
                file, seq
        );
    }
}
