package mirror.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.endpoint.event.RefreshEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

/**
 * @author zhangqingchun
 * @date 2021/2/4
 * @description
 */
@Component
public class RefreshSource {

    @Autowired
    ApplicationContext applicationContext;

    public void start() {
        Callable callable = new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                while (true){
                    refresh();
                }
            }
        };
        Thread thread=new Thread();
        thread.start();
    }

    public void refresh() {
        applicationContext.publishEvent(new RefreshEvent(this, null, "Refresh config"));
    }
}
