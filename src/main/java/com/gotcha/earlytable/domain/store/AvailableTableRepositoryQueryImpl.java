package com.gotcha.earlytable.domain.store;


import com.gotcha.earlytable.domain.store.entity.AvailableTable;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.time.LocalTime;

import static com.gotcha.earlytable.domain.store.entity.QAvailableTable.availableTable;
import static com.gotcha.earlytable.domain.store.entity.QReservationMaster.reservationMaster;
import static com.gotcha.earlytable.domain.store.entity.QStoreTable.storeTable;


public class AvailableTableRepositoryQueryImpl implements AvailableTableRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    public AvailableTableRepositoryQueryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public AvailableTable findByPersonnelCountAndTime(Integer personnelCount, LocalTime reservationTime) {
        return queryFactory.selectFrom(availableTable)
                .leftJoin(availableTable.reservationMaster, reservationMaster)
                .leftJoin(reservationMaster.storeTable, storeTable)
                .fetchJoin()
                .where(
                        reservationMaster.reservationTime.eq(reservationTime),
                        storeTable.tableCount.eq(personnelCount)
                )
                .fetchOne();
    }


}
