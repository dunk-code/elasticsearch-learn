package eslearnjd.jd.service;

import eslearnjd.jd.pojo.Content;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author ：zsy
 * @date ：Created 2021/6/9 16:45
 * @description：jd搜索
 */
public interface JDService {

    //解析数据放入es
    Boolean analyticalData(String keyword) throws IOException;

    List<Map<String, Object>> search(String keyword, int pageNo, int pageSize) throws IOException;

    List<Content> search(String keyword) throws IOException;
}
