package com.xhxj.web;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import com.xhxj.dao.EheitaiCatalogDao;
import com.xhxj.daomain.EheitaiCatalog;
import com.xhxj.daomain.EheitaiSearch;
import com.xhxj.service.EheitaiCatalogService;
import com.xhxj.utils.Json;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;

@PropertySource("classpath:Configuration.properties")
@Component
public class AnalysisUrl {


    /**
     * 公用资源
     */
    @Value("${catalog}")
    private String catalog;


    @Autowired
    EheitaiCatalogService eheitaiCatalogDao;


    //使用连接池
    public static PoolingHttpClientConnectionManager cm = null;

    /**
     * 解析页面
     */
    public void analysisHtml() {
        String content = null;
        try {
            content = FileUtils.readFileToString(new File(catalog), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("读取静态页面报错,检查抓取的页面");
        }
        Document doc = Jsoup.parse(content);
        //查找总的
        Element itg = doc.getElementsByClass("itg").first();
        Elements tbody = itg.select("tbody>tr");


        int i = 0;
        for (Element element : tbody) {
            EheitaiCatalog eheitaiCatalog = new EheitaiCatalog();
            //第一个css样式跳过
            if (i != 0) {
                //第一个作品是有连接的所以跳过
                if (i == 1) {
                    Elements it2 = element.select(".it2");

                    System.out.println("-------------------------------------");
                    String id = it2.first().id();

                    eheitaiCatalog.setGid(Integer.parseInt(id.replace("i", "")));
                    Elements img = it2.select("img");
                    eheitaiCatalog.setImgUrl(img.attr("src"));
                    eheitaiCatalog.setTitle(img.attr("alt"));
                    eheitaiCatalog.setUrl(element.select(".it5>a").attr("href"));


                } else {
                    //从这里开始第二个作品
                    Elements it2 = element.select(".it2");
                    String id = it2.first().id();
                    eheitaiCatalog.setGid(Integer.parseInt(id.replace("i", "")));
                    //获取所需要的其他信息
                    String[] split = it2.text().split("~");
//        https://exhentai.org/t/2d/fb/2dfb69d52de3a6f534c0ef7ce676167d2399c93d-455677-1061-1500-jpg_l.jpg

                    eheitaiCatalog.setImgUrl("https://" + split[1] + "/" + split[2]);
                    eheitaiCatalog.setTitle(split[split.length - 1]);
//                //获取作品链接
//
                    eheitaiCatalog.setUrl(element.select(".it5>a").attr("href"));

                }
            }
            //把采集的数据保存到数据库
            //确定采集的数据没问题不为空
            if (eheitaiCatalog.getGid() != null && !eheitaiCatalog.getGid().equals("")) {
                //如果没有才保存,有不保存

                List<EheitaiCatalog> byDivId = eheitaiCatalogDao.findByGid(eheitaiCatalog.getGid());

                if (byDivId == null || byDivId.size() == 0) {
//                    System.out.println("是哪个报错了?eheitaiCatalog="+eheitaiCatalog);

                    eheitaiCatalogDao.save(eheitaiCatalog);
                    System.out.println("已保存:" + eheitaiCatalog + "\n");
                } else {
                    System.out.println("sql中有重复数据:" + byDivId + "\n" + "未保存");
                }

            }
            i++;


        }
        System.out.println("一共:" + (i - 1));
    }


    //静态初始化连接池
    public AnalysisUrl() {

        cm = new PoolingHttpClientConnectionManager();
        //设置连接池的最大连接数
        cm.setMaxTotal(100);
    }

    //设置获取时的等待参数
    public RequestConfig setConfig() {
        //设置代理服务器
        HttpHost proxy = new HttpHost("127.0.0.1", 1081, "http");
        System.out.println(proxy);

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(3000)//创建连接的超时时间,单位毫秒.
                .setConnectionRequestTimeout(300)//从连接池中创建连接的超时时间,单位毫秒
                .setSocketTimeout(10 * 1000)//数据的传输超时时间
                .setProxy(proxy)
                .setCookieSpec(CookieSpecs.DEFAULT)
                .build();
        return requestConfig;
    }


    /**
     * 读取转换cookie为HashMap
     *
     * @return
     */
    public static HashMap<String, String> change() {
        String cookie = "igneous=6c63cbdc0;ipb_member_id=805259;ipb_pass_hash=1a0592e854f1b08bcb9c2eb40b6455de;yay=0;lv=1546915603-1546935734;";
        String[] split = cookie.split(";");
        HashMap<String, String> hashMap = new HashMap<>();
        for (String s : split) {
            String[] split1 = s.split("=");
            hashMap.put(split1[0], split1[1]);
        }

        return hashMap;
    }


    /**
     * 获取http静态页面
     *
     * @param
     */
    public String getHttp() {

        CookieStore cookieStore = new BasicCookieStore();
//        System.out.println("传入cookieStore前的数据:" + cookieStore.toString());
        CloseableHttpClient client = HttpClients.custom().setConnectionManager(cm)
                //设置cookieStore,传入
                .setDefaultCookieStore(cookieStore)
                .build();


        //创建请求地址
        String html = "";
        URIBuilder uriBuilder = null;
        HttpGet httpGet = null;
        List<EheitaiSearch> changejson = Json.changejson();
        try {
            uriBuilder = new URIBuilder("https://exhentai.org/");
            for (EheitaiSearch eheitaiSearch : changejson) {
                uriBuilder.setParameter("f_search", eheitaiSearch.getF_search());
            }

            httpGet = new HttpGet(uriBuilder.build());

            //设置获取时的等待参数
            httpGet.setConfig(setConfig());
            //设置请求头
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3664.3 Safari/537.36");

            httpGet.setHeader("Cookie", "igneous=6c63cbdc0; ipb_member_id=805259; ipb_pass_hash=1a0592e854f1b08bcb9c2eb40b6455de; yay=0; lv=1546944712-1546960410");

            System.out.println("发起请求的信息：" + httpGet);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        CloseableHttpResponse response = null;
        //接受响应的脚本
        //接受返回的网页
        try {
            response = client.execute(httpGet);
            //如果成功请求
            if (response.getStatusLine().getStatusCode() == 200) {

                HttpEntity httpEntity = response.getEntity();


                System.out.println("是不是这里:" + httpEntity.toString());


                InputStream inputStream = httpEntity.getContent();

//                提取出一个io流去操作

                //查看cookie
                catcookie(cookieStore);
                //                把文件写出去看看
                getFileOutputStream(httpEntity.getContent(), catalog);

                //看看到底用的是啥编码格式
                String coding = CharsetDetector(inputStream);


                inputStream.close();

                //转换成字符串给jsup解析
//                if (httpEntity != null) {
//                    html = EntityUtils.toString(httpEntity);
//                }
                return coding;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("接收网页出错");
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("关流失败");
            }
        }
        return null;
    }

    /**
     * 查看cookie
     *
     * @param cookieStore
     */
    private void catcookie(CookieStore cookieStore) {
        //查看Cookie
        List<Cookie> log = cookieStore.getCookies();
        if (log.isEmpty()) {
            System.out.println("None");
        } else {
            for (int i = 0; i < log.size(); i++) {
                System.out.println("- " + log.get(i).toString());
            }
        }
    }

    /**
     * 查看网页使用的编码格式
     *
     * @param content
     */
    private static String CharsetDetector(InputStream content) {
        //在关闭之前检测网页编码
        CharsetDetector detector = new CharsetDetector();
        String httpName = null;
        try {

            BufferedInputStream bufferedInputStream = new BufferedInputStream(content);
            detector.setText(bufferedInputStream);
            //获取最可能的编码格式
            CharsetMatch match = detector.detect();
            httpName = match.getName();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("检查编码时报错");
            return "UTF-8";
        }
        return httpName;
    }

    /**
     * 写出文件看看咋回事
     *
     * @param content 爬取网站的io流
     * @throws IOException
     */
    public void getFileOutputStream(InputStream content, String url) {
        try {
            //写出文件看看乍回事
            FileOutputStream downloadFile = new FileOutputStream(url);
            int index;
            byte[] bytes = new byte[1024];
            while ((index = content.read(bytes)) != -1) {
                downloadFile.write(bytes, 0, index);
                downloadFile.flush();
            }

            downloadFile.close();
        } catch (IOException e) {
            System.out.println("在写出网站的时候出错");
            e.printStackTrace();
        }
    }


}
