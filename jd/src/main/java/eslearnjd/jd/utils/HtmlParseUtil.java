package eslearnjd.jd.utils;

import eslearnjd.jd.pojo.Content;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ：zsy
 * @date ：Created 2021/6/9 15:24
 * @description：解析网页
 */
public class HtmlParseUtil {
    public static void main(String[] args) throws IOException {
        HtmlParseUtil.parseJD("java").forEach(System.out::println);
    }

    public static List<Content> parseJD(String keyWord) throws IOException {
        String url = "https://search.jd.com/Search?keyword=" + keyWord;
        Document document = Jsoup.parse(new URL(url), 30000);
        Element e = document.getElementById("J_goodsList");
        //System.out.println(e.html());
        List<Content> list = new ArrayList<>();
        Elements lis = e.getElementsByTag("li");
        for (Element li : lis) {
            //所有图片都是延时加载的
            String img = li.getElementsByTag("img").eq(0).attr("data-lazy-img");
            String price = li.getElementsByClass("p-price").eq(0).text();
            String name = li.getElementsByClass("p-name").eq(0).text();
            //System.out.println("http:" + img);
            //System.out.println(price);
            //System.out.println(name);
            list.add(new Content("http:" + img, price, name));
        }
        return list;
    }
}
