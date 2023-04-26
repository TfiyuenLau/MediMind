package edu.hbmu.outpatient.spider;

import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * WebMagic爬取病症、药品描述
 */
public class YiXuePageProcess implements PageProcessor {
    private final static String PATH = "D:\\Projects\\JavaProject\\MediMind\\outpatient-service\\src\\main\\resources\\dict\\";

    private final Site site = Site.me().setRetryTimes(3).setSleepTime(500).setTimeOut(10000);

    private final BufferedWriter bw;

    {
        try {
            bw = new BufferedWriter(new FileWriter(PATH + "medicine_description.txt", true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    /**
     * 爬取逻辑
     *
     * @param page Page对象
     */
    @SneakyThrows
    @Override
    public void process(Page page) {
        String medicine = URLDecoder.decode(page.getRequest().getUrl().split("/")[3], StandardCharsets.UTF_8);
        String summaryDiv = page.getHtml().xpath("//*[@id=\"mw-content-text\"]/div/p[1]").get();// 获取summary
        String res = Jsoup.parse(summaryDiv).text();// Jsoup解析
        if (res.length() == 0) {
            res = "待补充...";
        }
        System.out.println(res);
//        bw.write(medicine + "," + res);
//        bw.newLine();
//        bw.flush();
    }

    public static void main(String[] args) throws IOException {
        String path = PATH + "drug.txt";
        BufferedReader br = new BufferedReader(new FileReader(path));

        String disease = null;
        while ((disease = br.readLine()) != null) {
            disease = URLEncoder.encode(disease, StandardCharsets.UTF_8);// url编码
            String url = "https://www.yixue.com/" + disease;// 基础URL
            Spider spider = Spider.create(new YiXuePageProcess())
                    .addUrl(url)
                    .thread(5);

            spider.run();
        }
        br.close();
    }
}
