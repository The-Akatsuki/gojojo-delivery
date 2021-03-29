package com.xiornis.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.xiornis.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShipmentActivityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShipmentActivity.class);
        ShipmentActivity shipmentActivity1 = new ShipmentActivity();
        shipmentActivity1.setId(1L);
        ShipmentActivity shipmentActivity2 = new ShipmentActivity();
        shipmentActivity2.setId(shipmentActivity1.getId());
        assertThat(shipmentActivity1).isEqualTo(shipmentActivity2);
        shipmentActivity2.setId(2L);
        assertThat(shipmentActivity1).isNotEqualTo(shipmentActivity2);
        shipmentActivity1.setId(null);
        assertThat(shipmentActivity1).isNotEqualTo(shipmentActivity2);
    }
}
