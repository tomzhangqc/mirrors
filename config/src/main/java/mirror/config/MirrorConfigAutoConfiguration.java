package mirror.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author winters
 */
@Configuration
@ConditionalOnProperty(name = "spring.cloud.mirror.flag", matchIfMissing = true)
public class MirrorConfigAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public MirrorProperties mirrorProperties() {
        return new MirrorProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public RefreshSource refreshSource(MirrorProperties mirrorProperties) {
        return new RefreshSource(mirrorProperties);
    }


    @Bean
    @ConditionalOnMissingBean(MirrorSourceLocator.class)
    public MirrorSourceLocator mirrorSourceLocator(MirrorProperties mirrorProperties) {
        return new MirrorSourceLocator(mirrorProperties);
    }
}
