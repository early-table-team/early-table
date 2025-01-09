package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.store.entity.AvailableTable;

import java.time.LocalTime;

public interface AvailableTableRepositoryQuery {

    AvailableTable findByPersonnelCountAndTime(Integer personnelCount, LocalTime reservationTime);
}
