package com.gotcha.earlytable.domain.reservation.dto;

import lombok.Getter;

@Getter
public class KakaoPayRequestDto {

    private final String cid;

    private final String partner_order_id;

    private final String partner_user_id;

    private final String item_name;

    private final int quantity;

    private final long total_amount;

    private final long vat_amount;

    private final long tax_free_amount;

    private final String approval_url;

    private final String fail_url;

    private final String cancel_url;


    public KakaoPayRequestDto(String cid,  String partner_order_id, String partner_user_id, String item_name, int quantity
            , long total_amount, long vat_amount, long tax_free_amount, String approval_url, String fail_url, String cancel_url) {

        this.cid = cid;
        this.partner_order_id = partner_order_id;
        this.partner_user_id = partner_user_id;
        this.item_name = item_name;
        this.quantity = quantity;
        this.total_amount = total_amount;
        this.vat_amount = vat_amount;
        this.tax_free_amount = tax_free_amount;
        this.approval_url = approval_url;
        this.fail_url = fail_url;
        this.cancel_url = cancel_url;

    }

}
