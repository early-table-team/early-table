package com.gotcha.earlytable.domain.file;

import com.gotcha.earlytable.domain.file.entity.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageFileRepository extends JpaRepository<ImageFile, Long> {


    @Query("SELECT i FROM ImageFile i JOIN i.file f JOIN Store s ON s.file = f WHERE s.storeId = :storeId AND i.fileStatus = :fileStatus")
    ImageFile findByFileStoreStoreIdAndFileStatus(Long storeId, FileStatus fileStatus);
}
