package mirror.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

/**
 * @author zhangqingchun
 * @date 2021/2/4
 * @description
 */
@Data
@ConfigurationProperties(MirrorProperties.PREFIX)
public class MirrorProperties {

    public static final String PREFIX = "spring.cloud.mirror.config";

    private String serverAddress;

    @Autowired
    private Environment environment;

    @PostConstruct
    public void init() {
        if (StringUtils.isEmpty(serverAddress)) {
            String serverAddress = environment
                    .resolvePlaceholders("${spring.cloud.mirror.config.server-address:}");
            this.setServerAddress(serverAddress);
        }
    }
}
