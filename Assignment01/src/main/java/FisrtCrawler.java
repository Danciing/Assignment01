import us.codecraft.webmagic.processor.example.TjuPageProcessor;
import us.codecraft.webmagic.processor.example.TsuPageProcessor;
import us.codecraft.webmagic.processor.example.WhuPageProcessor;

public class FisrtCrawler {
    public static void main(String[] args) {
        //直接调用对应类方法开始爬虫
        WhuPageProcessor.run();
        TsuPageProcessor.run();
        TjuPageProcessor.run();
    }
}