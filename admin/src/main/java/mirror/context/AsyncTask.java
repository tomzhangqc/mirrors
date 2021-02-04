package mirror.context;

import lombok.Data;

import javax.servlet.AsyncContext;

/**
 * @author zhangqingchun
 * @date 2021/2/4
 * @description
 */
@Data
public class AsyncTask {

    private AsyncContext asyncContext;

    private boolean timeout;

    public AsyncTask(AsyncContext asyncContext, boolean timeout) {
        this.asyncContext = asyncContext;
        this.timeout = timeout;
    }
}
