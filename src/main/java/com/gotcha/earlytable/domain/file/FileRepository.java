package com.gotcha.earlytable.domain.file;

import com.gotcha.earlytable.domain.file.entity.File;
import com.gotcha.earlytable.domain.file.entity.ImageFile;
import com.gotcha.earlytable.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

}
