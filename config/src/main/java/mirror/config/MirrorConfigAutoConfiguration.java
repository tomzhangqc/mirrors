package mirror.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author winters
 */
@Configuration
@ConditionalOnProperty(name = "spring.mirror.flag",matchIfMissing = true)
public class MirrorConfigAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(MirrorSource.class)
    public MirrorSource source(){
        return new MirrorSource();
    }
}
