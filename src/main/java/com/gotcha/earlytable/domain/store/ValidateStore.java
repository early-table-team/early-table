package com.gotcha.earlytable.domain.store;

import org.springframework.stereotype.Component;

@Component
public interface ValidateStore {

    void validateStoreOwner(Long storeId, Long userId);

}
