package mirror.controller;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import mirror.context.AsyncTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangqingchun
 * @date 2021/2/4
 * @description
 */
@Slf4j
@RestController
public class AdminController {

    private Multimap<String, AsyncTask> dataIdContext = Multimaps.synchronizedSetMultimap(HashMultimap.create());

    private ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("longPolling-timeout-checker-%d")
            .build();
    private ScheduledExecutorService timeoutChecker = new ScheduledThreadPoolExecutor(1, threadFactory);


    @GetMapping("/mirror")
    public String getConfig(){
        return "zhang";
    }

    @GetMapping("/listener")
    public void listener(HttpServletRequest request, HttpServletResponse response) {
        String dataId = request.getParameter("dataId");
        AsyncContext asyncContext = request.startAsync(request, response);
        AsyncTask asyncTask = new AsyncTask(asyncContext, true);
        dataIdContext.put(dataId, asyncTask);
        timeoutChecker.schedule(() -> {
            if (asyncTask.isTimeout()) {
                dataIdContext.remove(dataId, asyncTask);
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                asyncContext.complete();
            }
        }, 30000, TimeUnit.MILLISECONDS);
    }

    @RequestMapping("/publishConfig")
    @SneakyThrows
    public String publishConfig(String dataId, String configInfo) {
        Collection<AsyncTask> asyncTasks = dataIdContext.removeAll(dataId);
        for (AsyncTask asyncTask : asyncTasks) {
            asyncTask.setTimeout(false);
            HttpServletResponse response = (HttpServletResponse)asyncTask.getAsyncContext().getResponse();
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(configInfo);
            asyncTask.getAsyncContext().complete();
        }
        return "success";
    }

}
