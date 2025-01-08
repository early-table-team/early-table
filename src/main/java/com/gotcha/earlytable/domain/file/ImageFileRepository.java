package com.gotcha.earlytable.domain.file;

import com.gotcha.earlytable.domain.file.entity.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageFileRepository extends JpaRepository<ImageFile, Long> {

    ImageFile findByFileStoreStoreIdAndFileStatus(Long storeId, FileStatus fileStatus);
}
