package com.xiornis.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.xiornis.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReasonEscalationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReasonEscalation.class);
        ReasonEscalation reasonEscalation1 = new ReasonEscalation();
        reasonEscalation1.setId(1L);
        ReasonEscalation reasonEscalation2 = new ReasonEscalation();
        reasonEscalation2.setId(reasonEscalation1.getId());
        assertThat(reasonEscalation1).isEqualTo(reasonEscalation2);
        reasonEscalation2.setId(2L);
        assertThat(reasonEscalation1).isNotEqualTo(reasonEscalation2);
        reasonEscalation1.setId(null);
        assertThat(reasonEscalation1).isNotEqualTo(reasonEscalation2);
    }
}
