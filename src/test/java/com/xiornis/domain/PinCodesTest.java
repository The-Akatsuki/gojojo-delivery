package com.xiornis.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.xiornis.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PinCodesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PinCodes.class);
        PinCodes pinCodes1 = new PinCodes();
        pinCodes1.setId(1L);
        PinCodes pinCodes2 = new PinCodes();
        pinCodes2.setId(pinCodes1.getId());
        assertThat(pinCodes1).isEqualTo(pinCodes2);
        pinCodes2.setId(2L);
        assertThat(pinCodes1).isNotEqualTo(pinCodes2);
        pinCodes1.setId(null);
        assertThat(pinCodes1).isNotEqualTo(pinCodes2);
    }
}
