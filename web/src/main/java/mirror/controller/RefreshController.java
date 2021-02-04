package mirror.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.endpoint.event.RefreshEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangqingchun
 * @date 2021/2/4
 * @description
 */
@RestController
public class RefreshController {

    @Autowired
    ApplicationContext applicationContext;


    @RequestMapping("/refresh")
    public void refresh() {
        applicationContext.publishEvent(new RefreshEvent(this, null, "Refresh config"));
    }
}
