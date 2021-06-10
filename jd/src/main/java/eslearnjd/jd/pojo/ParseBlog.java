package eslearnjd.jd.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ：zsy
 * @date ：Created 2021/6/10 10:21
 * @description：博客
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ParseBlog {
    private String name;
    private String desc;
    private String img;
    private String content;
}
