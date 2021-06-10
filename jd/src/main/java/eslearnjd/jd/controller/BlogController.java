package eslearnjd.jd.controller;

import eslearnjd.jd.service.JDService;
import lombok.AllArgsConstructor;
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
 * @date ：Created 2021/6/10 11:03
 * @description：博客解析
 */
@Controller
public class BlogController {

    @Autowired
    JDService jdService;

    @ResponseBody
    @GetMapping("/parseBlog")
    public Boolean parse() throws IOException {
        return jdService.analyticalBlog();
    }

    @ResponseBody
    @GetMapping("/searchBlog/{keyword}")
    public List<Map<String, Object>> search(@PathVariable String keyword) throws IOException {
        return jdService.searchBlog(keyword);
    }
}
