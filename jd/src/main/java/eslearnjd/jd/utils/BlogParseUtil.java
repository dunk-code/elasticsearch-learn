package eslearnjd.jd.utils;

import eslearnjd.jd.pojo.ParseBlog;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ：zsy
 * @date ：Created 2021/6/10 9:52
 * @description：博客解析
 */
public class BlogParseUtil {
    public static void main(String[] args) throws IOException {
        BlogParseUtil blogParseUtil = new BlogParseUtil();
        blogParseUtil.parseBlog();
    }

    public static List<ParseBlog> parseBlog() throws IOException {
        List<ParseBlog> blogs = new ArrayList<>();
        String url = "http://www.dunkcode.cn/";
        Document document = Jsoup.parse(new URL(url), 30000);
        Elements ui_attached_segment = document.getElementsByClass("ui attached segment");
        //System.out.println(ui_attached_segment.html());
        Element element = ui_attached_segment.get(0);
        Elements elements = element.getElementsByClass("ui padded vertical segment m-padded-tb-large");
        for (Element element1 : elements) {
            String name = element1.getElementsByTag("a").get(0).text();
            String content = parseContent(url + element1.getElementsByTag("a").get(0).attr("href"));
            String desc = element1.getElementsByTag("p").text();
            String img = element1.getElementsByTag("img").get(1).attr("src");
            blogs.add(new ParseBlog(name, desc, img, content));
        }
        return blogs;
    }

    private static String parseContent(String url) throws IOException {
        Document document = Jsoup.parse(new URL(url), 30000);
        Element content = document.getElementById("content");
        return content.text();
    }
}
