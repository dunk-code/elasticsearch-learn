package eslearnjd.jd.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;/**
 * @author ：zsy
 * @date ：Created 2021/6/9 16:17
 * @description：商品
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Content {
    private String img;
    private String price;
    private String name;
}


