package com.gotcha.earlytable.domain.file;

import com.gotcha.earlytable.domain.file.entity.FileDetail;
import com.gotcha.earlytable.domain.file.enums.FileStatus;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileDetailRepository extends JpaRepository<FileDetail, Long> {

    default FileDetail findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

    FileDetail findByFileUrl(String fileUrl);

    @Query("SELECT i FROM FileDetail i JOIN i.file f JOIN Store s ON s.file = f WHERE s.storeId = :storeId AND i.fileStatus = :fileStatus")
    Optional<FileDetail> findByFileStoreStoreIdAndFileStatus(Long storeId, FileStatus fileStatus);

    void deleteByFileUrlIn(List<String> uniqueFileUrls);
}
