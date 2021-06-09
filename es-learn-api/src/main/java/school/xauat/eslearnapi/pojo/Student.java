package school.xauat.eslearnapi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author ：zsy
 * @date ：Created 2021/6/7 23:01
 * @description：学生
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Student {
    private String name;
    private int age;
    private String desc;
    private List<String> tags;
}
