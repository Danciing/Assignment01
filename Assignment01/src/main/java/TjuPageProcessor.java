import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.component.HashSetDuplicateRemover;

public class TjuPageProcessor implements PageProcessor {

    private Site site = Site.me()
            .setRetryTimes(3)                           //重试次数
            .setSleepTime(1000)                         //重试间隔
            .setUseGzip(true);

    @Override
    public void process(Page page) {
        page.addTargetRequests(page.getHtml().css("div.Pic1").links().regex("https://see.tongji.edu.cn/info/\\S+").all());       //添加下一页
        page.addTargetRequests(page.getHtml().links().regex("https://see.tongji.edu.cn/szdw/dsmd/axkhf/jsjkxyjsxk\\S+").all());         //添加每个老师的个人主页
        page.putField("Data", page.getHtml().xpath("//table[@class=\"jstab\"]/allText()").toString());                              //拉取特定内容
        if (page.getResultItems().get("Data")==null){                                                                                   //将无用页面排除
            page.setSkip(true);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void run() {
        Spider.create(new TjuPageProcessor())
                .addUrl("https://see.tongji.edu.cn/szdw/dsmd/axkhf/jsjkxyjsxk.htm")                             //初始网址
                .setScheduler(new QueueScheduler().setDuplicateRemover(new HashSetDuplicateRemover()))          //查重
                .addPipeline(new JsonFilePipeline("./result"))                                             //保存
                .thread(5)                                                                                      //多线程
                .run();                                                                                         //开始



    }
}
