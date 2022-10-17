package us.codecraft.webmagic.processor.example;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.component.HashSetDuplicateRemover;

import java.util.ArrayList;
import java.util.List;

/**
 * @author code4crafter@gmail.com <br>
 * @since 0.4.0
 */
public class TjuPageProcessor implements PageProcessor {

    private Site site = Site.me()//.setHttpProxy(new HttpHost("127.0.0.1",8888))
            .setRetryTimes(3).setSleepTime(1000).setUseGzip(true);

    @Override
    public void process(Page page) {
        List<String> Alinks = page.getHtml().css("div.Pic1").links().regex("https://see.tongji.edu.cn/info/\\S+").all();
        page.addTargetRequests(Alinks);
        List<String> Blinks = page.getHtml().links().regex("https://see.tongji.edu.cn/szdw/dsmd/axkhf/jsjkxyjsxk\\S+").all();
        page.addTargetRequests(Blinks);
        page.putField("name", page.getHtml().xpath("//table[@class=\"jstab\"]/allText()"));
        //page.putField("All", page.getHtml().xpath("//tr/allText()").all());
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void run() {
        Spider.create(new TjuPageProcessor())
                .addUrl("https://see.tongji.edu.cn/szdw/dsmd/axkhf/jsjkxyjsxk.htm")                             //初始网址
                .setScheduler(new QueueScheduler().setDuplicateRemover(new HashSetDuplicateRemover()))          //查重
                .addPipeline(new JsonFilePipeline("./src/main/resources/result"))                          //保存
                .thread(5)                                                                                      //多线程
                .run();                                                                                         //开始



    }
}
