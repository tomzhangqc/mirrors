package mirror.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangqingchun
 * @date 2021/2/4
 * @description
 */
@RefreshScope
@RestController
public class MirrorController {

    @Value("${name}")
    private String name;

    @GetMapping("/get")
    public String start(){
        return name;
    }

}
