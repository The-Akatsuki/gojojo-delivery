package com.xiornis.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.xiornis.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EscalationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Escalation.class);
        Escalation escalation1 = new Escalation();
        escalation1.setId(1L);
        Escalation escalation2 = new Escalation();
        escalation2.setId(escalation1.getId());
        assertThat(escalation1).isEqualTo(escalation2);
        escalation2.setId(2L);
        assertThat(escalation1).isNotEqualTo(escalation2);
        escalation1.setId(null);
        assertThat(escalation1).isNotEqualTo(escalation2);
    }
}
