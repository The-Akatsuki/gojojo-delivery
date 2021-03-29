package com.xiornis.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.xiornis.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReturnReasonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReturnReason.class);
        ReturnReason returnReason1 = new ReturnReason();
        returnReason1.setId(1L);
        ReturnReason returnReason2 = new ReturnReason();
        returnReason2.setId(returnReason1.getId());
        assertThat(returnReason1).isEqualTo(returnReason2);
        returnReason2.setId(2L);
        assertThat(returnReason1).isNotEqualTo(returnReason2);
        returnReason1.setId(null);
        assertThat(returnReason1).isNotEqualTo(returnReason2);
    }
}
