package mirror.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author winters
 */
@Slf4j
@Order(0)
public class MirrorSource implements PropertySourceLocator {

    private MirrorProperties mirrorProperties;

    public MirrorSource(MirrorProperties mirrorProperties) {
        this.mirrorProperties = mirrorProperties;
    }

    @Override
    public PropertySource<?> locate(Environment environment) {
        RestTemplate restTemplate = new RestTemplate();
        String config = restTemplate.getForObject(mirrorProperties.getServerAddress(), String.class);
        log.info("+++++++++++++++config is {}", config);
        Map<String, Object> map = new HashMap<>();
        map.put("hello.name", "1");
        MirrorPropertySource mirrorPropertySource = new MirrorPropertySource("mirrorConfig", map);
        CompositePropertySource composite = new CompositePropertySource("mirrorConfig");
        composite.addFirstPropertySource(mirrorPropertySource);
        return composite;
    }

}
