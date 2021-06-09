package eslearnjd.jd.controller;

import eslearnjd.jd.pojo.Content;
import eslearnjd.jd.service.JDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author ：zsy
 * @date ：Created 2021/6/9 17:40
 * @description：商品
 */
@Controller
public class ContentController {

    @Autowired
    JDService jdService;

    @GetMapping("/parse/{keyword}")
    @ResponseBody
    public Boolean parse(@PathVariable String keyword) throws IOException {
        return jdService.analyticalData(keyword);
    }

    @ResponseBody
    @GetMapping("/search/{keyword}/{pageNo}/{pageSize}")
    public List<Map<String, Object>> search(
            @PathVariable String keyword,
            @PathVariable int pageNo,
            @PathVariable int pageSize) throws IOException {
        return jdService.search(keyword, pageNo, pageSize);
    }

    @ResponseBody
    @GetMapping("/searchfromjd/{keyword}")
    public List<Content> searchFromJd(@PathVariable String keyword) throws IOException {
        return jdService.search(keyword);
    }
}
