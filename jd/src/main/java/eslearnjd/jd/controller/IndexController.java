package eslearnjd.jd.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author ：zsy
 * @date ：Created 2021/6/9 16:31
 * @description：首页
 */
@Slf4j
@Controller
public class IndexController {


    @GetMapping({"/","/index"})
    public String index(){
        log.info("{==================初始化........=================}");
        return "index";
    }


}