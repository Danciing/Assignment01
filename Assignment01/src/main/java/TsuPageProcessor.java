import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.Map;


public class TsuPageProcessor implements PageProcessor {

    private Site site = Site.me()
            .setRetryTimes(3)                           //重试次数
            .setSleepTime(1000)                         //重试间隔
            .setUseGzip(true);

    @Override
    public void process(Page page) {
        page.addTargetRequests(page.getHtml().css("div.people01").links().regex("https://www.cs.tsinghua.edu.cn/info/\\S+").all());     //添加每个老师的个人主页
        page.putField("Data", page.getHtml().xpath("//div[@class=\"v_news_content\"]/allText()").toString());                              //拉取特定内容
        if (page.getResultItems().get("Data")==null){                                                                                           //将无用页面排除
            page.setSkip(true);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void run() {
        //single download
        Spider.create(new TsuPageProcessor())
                .addUrl("https://www.cs.tsinghua.edu.cn/szzk/jzgml.htm")                              //设置初始网址
                .thread(5)                                                                            //多线程
                .run();                                                                               //开始运行
    }
}
