package com.xiornis.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.xiornis.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PickupAddressTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PickupAddress.class);
        PickupAddress pickupAddress1 = new PickupAddress();
        pickupAddress1.setId(1L);
        PickupAddress pickupAddress2 = new PickupAddress();
        pickupAddress2.setId(pickupAddress1.getId());
        assertThat(pickupAddress1).isEqualTo(pickupAddress2);
        pickupAddress2.setId(2L);
        assertThat(pickupAddress1).isNotEqualTo(pickupAddress2);
        pickupAddress1.setId(null);
        assertThat(pickupAddress1).isNotEqualTo(pickupAddress2);
    }
}
