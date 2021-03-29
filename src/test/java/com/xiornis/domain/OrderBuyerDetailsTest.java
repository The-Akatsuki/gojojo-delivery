package com.xiornis.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.xiornis.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderBuyerDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderBuyerDetails.class);
        OrderBuyerDetails orderBuyerDetails1 = new OrderBuyerDetails();
        orderBuyerDetails1.setId(1L);
        OrderBuyerDetails orderBuyerDetails2 = new OrderBuyerDetails();
        orderBuyerDetails2.setId(orderBuyerDetails1.getId());
        assertThat(orderBuyerDetails1).isEqualTo(orderBuyerDetails2);
        orderBuyerDetails2.setId(2L);
        assertThat(orderBuyerDetails1).isNotEqualTo(orderBuyerDetails2);
        orderBuyerDetails1.setId(null);
        assertThat(orderBuyerDetails1).isNotEqualTo(orderBuyerDetails2);
    }
}
