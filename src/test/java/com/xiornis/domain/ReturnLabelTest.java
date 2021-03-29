package com.xiornis.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.xiornis.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReturnLabelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReturnLabel.class);
        ReturnLabel returnLabel1 = new ReturnLabel();
        returnLabel1.setId(1L);
        ReturnLabel returnLabel2 = new ReturnLabel();
        returnLabel2.setId(returnLabel1.getId());
        assertThat(returnLabel1).isEqualTo(returnLabel2);
        returnLabel2.setId(2L);
        assertThat(returnLabel1).isNotEqualTo(returnLabel2);
        returnLabel1.setId(null);
        assertThat(returnLabel1).isNotEqualTo(returnLabel2);
    }
}
