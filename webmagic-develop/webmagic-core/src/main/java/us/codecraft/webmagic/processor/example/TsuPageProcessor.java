package us.codecraft.webmagic.processor.example;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author code4crafter@gmail.com <br>
 * @since 0.4.0
 */
public class TsuPageProcessor implements PageProcessor {

    private Site site = Site.me()//.setHttpProxy(new HttpHost("127.0.0.1",8888))
            .setRetryTimes(3).setSleepTime(1000).setUseGzip(true);

    @Override
    public void process(Page page) {
        List<String> links = page.getHtml().css("div.people01").links().regex("https://www.cs.tsinghua.edu.cn/info/\\S+").all();
        page.addTargetRequests(links);
        page.putField("name", page.getHtml().xpath("//div[@class=\"v_news_content\"]/allText()"));
        //page.putField("All", page.getHtml().xpath("//tbody/allText()"));
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void run() {
        //single download
        Spider.create(new TsuPageProcessor())
                .addUrl("https://www.cs.tsinghua.edu.cn/szzk/jzgml.htm")
                .addPipeline(new JsonFilePipeline("./src/main/resources/result"))                     //保存
                .thread(5)
                .run();
    }
}
