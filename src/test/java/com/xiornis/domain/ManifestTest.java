package com.xiornis.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.xiornis.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ManifestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Manifest.class);
        Manifest manifest1 = new Manifest();
        manifest1.setId(1L);
        Manifest manifest2 = new Manifest();
        manifest2.setId(manifest1.getId());
        assertThat(manifest1).isEqualTo(manifest2);
        manifest2.setId(2L);
        assertThat(manifest1).isNotEqualTo(manifest2);
        manifest1.setId(null);
        assertThat(manifest1).isNotEqualTo(manifest2);
    }
}
