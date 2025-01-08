package com.gotcha.earlytable.domain.menu;

import com.gotcha.earlytable.domain.menu.entity.Menu;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    default Menu findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

    @Query("SELECT m FROM Menu m WHERE m.store = :store AND m.menuStatus = '추천'")
    String findByStore(Store store);

}
