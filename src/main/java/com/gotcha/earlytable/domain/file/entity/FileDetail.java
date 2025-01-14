package com.gotcha.earlytable.domain.file.entity;


import com.gotcha.earlytable.domain.file.dto.FileDetailDto;
import com.gotcha.earlytable.domain.file.enums.FileStatus;
import com.gotcha.earlytable.domain.file.enums.FileType;
import com.gotcha.earlytable.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "file_detail")
public class FileDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
        updateFile(file);
        this.fileSeq = fileSeq;

    }

    public FileDetail() {

    }

    public void updateFile(File file) {
        this.file = file;
        file.getFileDetailList().add(this);
    }

    public void updateFileStatus(FileStatus fileStatus) {
        this.fileStatus = fileStatus;
    }

    public void updateSeq(int seq) {
        this.fileSeq = seq;
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
