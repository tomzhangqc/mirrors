package controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangqingchun
 * @date 2021/2/4
 * @description
 */
@RestController
public class MirrorController {

    @GetMapping("/")
    public String start(){
        return "success";
    }
}
