package com.xiornis.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.xiornis.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.xiornis.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.xiornis.domain.User.class.getName());
            createCache(cm, com.xiornis.domain.Authority.class.getName());
            createCache(cm, com.xiornis.domain.User.class.getName() + ".authorities");
            createCache(cm, com.xiornis.domain.Order.class.getName());
            createCache(cm, com.xiornis.domain.Order.class.getName() + ".shipmentActivities");
            createCache(cm, com.xiornis.domain.Order.class.getName() + ".wallets");
            createCache(cm, com.xiornis.domain.Product.class.getName());
            createCache(cm, com.xiornis.domain.Product.class.getName() + ".categories");
            createCache(cm, com.xiornis.domain.Product.class.getName() + ".orders");
            createCache(cm, com.xiornis.domain.PaymentMethod.class.getName());
            createCache(cm, com.xiornis.domain.PaymentMethod.class.getName() + ".orders");
            createCache(cm, com.xiornis.domain.Category.class.getName());
            createCache(cm, com.xiornis.domain.Category.class.getName() + ".categories");
            createCache(cm, com.xiornis.domain.OrderBuyerDetails.class.getName());
            createCache(cm, com.xiornis.domain.PinCodes.class.getName());
            createCache(cm, com.xiornis.domain.PickupAddress.class.getName());
            createCache(cm, com.xiornis.domain.PickupAddress.class.getName() + ".orders");
            createCache(cm, com.xiornis.domain.Manifest.class.getName());
            createCache(cm, com.xiornis.domain.Escalation.class.getName());
            createCache(cm, com.xiornis.domain.Escalation.class.getName() + ".manifests");
            createCache(cm, com.xiornis.domain.CourierCompany.class.getName());
            createCache(cm, com.xiornis.domain.CourierCompany.class.getName() + ".manifests");
            createCache(cm, com.xiornis.domain.ReasonEscalation.class.getName());
            createCache(cm, com.xiornis.domain.ReasonEscalation.class.getName() + ".escalations");
            createCache(cm, com.xiornis.domain.ReturnReason.class.getName());
            createCache(cm, com.xiornis.domain.ReturnLabel.class.getName());
            createCache(cm, com.xiornis.domain.ReturnLabel.class.getName() + ".returnReasons");
            createCache(cm, com.xiornis.domain.ShipmentActivity.class.getName());
            createCache(cm, com.xiornis.domain.Wallet.class.getName());
            createCache(cm, com.xiornis.domain.Wallet.class.getName() + ".transactions");
            createCache(cm, com.xiornis.domain.Transaction.class.getName());
            createCache(cm, com.xiornis.domain.Order.class.getName() + ".products");
            createCache(cm, com.xiornis.domain.Wallet.class.getName() + ".orders");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
