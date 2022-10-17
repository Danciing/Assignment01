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
public class WhuPageProcessor implements PageProcessor {

    private Site site = Site.me()//.setHttpProxy(new HttpHost("127.0.0.1",8888))
            .setRetryTimes(3).setSleepTime(1000).setUseGzip(true);

    @Override
    public void process(Page page) {
        List<String> Alinks = page.getHtml().css("div.teacher_zc_list").links().regex("http://cs.whu.edu.cn/\\S+").all();
        page.addTargetRequests(Alinks);
        List<String> Blinks = page.getHtml().css("div.info").links().regex("http://cs.whu.edu.cn/\\S+").all();
        page.addTargetRequests(Blinks);
        page.putField("name", page.getHtml().xpath("//div[@class=\"bg-wrap\"]/allText()"));
        //page.putField("All", page.getHtml().xpath("//tr/allText()").all());
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void run() {
        Spider.create(new WhuPageProcessor())
                .addUrl("http://cs.whu.edu.cn/teacher.aspx?showtype=jobtitle&typename=%e6%95%99%e6%8e%88")      //初始网址
                .setScheduler(new QueueScheduler().setDuplicateRemover(new HashSetDuplicateRemover()))          //查重
                .addPipeline(new JsonFilePipeline("./src/main/resources/result"))                     //保存
                .thread(5)                                                                                      //多线程
                .run();                                                                                         //开始



    }
}
