package mirror.config;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.endpoint.event.RefreshEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.*;

/**
 * @author zhangqingchun
 * @date 2021/2/4
 * @description
 */
@Slf4j
public class RefreshSource implements ApplicationListener<ApplicationReadyEvent>, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private MirrorProperties mirrorProperties;

    private CloseableHttpClient httpClient;
    private RequestConfig requestConfig;

    public RefreshSource(MirrorProperties mirrorProperties) {
        this.httpClient = HttpClientBuilder.create().build();
        this.requestConfig = RequestConfig.custom().setSocketTimeout(40000).build();
        this.mirrorProperties = mirrorProperties;
    }

    public void start() {
        ExecutorService executorService =
                Executors.newScheduledThreadPool(1, r -> {
                    Thread t = new Thread(r);
                    t.setName("config get thread" );
                    t.setDaemon(true);
                    return t;
                });
        Callable callable = (Callable<Object>) () -> {
            while (true) {
                longPolling(mirrorProperties.getServerAddress() + "/listener", "1111");
                refresh();
            }
        };
        executorService.submit(callable);
    }

    @SneakyThrows
    public void longPolling(String url, String dataId) {
        String endpoint = url + "?dataId=" + dataId;
        HttpGet request = new HttpGet(endpoint);
        request.setConfig(requestConfig);
        CloseableHttpResponse response = httpClient.execute(request);
        switch (response.getStatusLine().getStatusCode()) {
            case 200: {
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity()
                        .getContent()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                response.close();
                String configInfo = result.toString();
                log.info("dataId: [{}] changed, receive configInfo: {}", dataId, configInfo);
                break;
            }
            case 304: {
                log.info("longPolling dataId: [{}] once finished, configInfo is unchanged, longPolling again", dataId);
                break;
            }
            default: {
                throw new RuntimeException("unExcepted HTTP status code");
            }
        }
    }

    public void refresh() {
        applicationContext.publishEvent(new RefreshEvent(this, null, "Refresh config"));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

    }
}
