package com.xhxj.utils;

import com.alibaba.fastjson.JSON;
import com.xhxj.daomain.ErrorProxy;
import com.xhxj.daomain.Proxies;
import com.xhxj.daomain.ProxiesBean;
import com.xhxj.service.ErrorProxyService;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.proxy.Proxy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class GetProxy {

    @Value("${url}")
    private String url;

    @Autowired
    ErrorProxyService errorProxyService;

    /**
     * 获取网站上的代理服务器地址
     *
     * @return
     */
    public Proxy getHttpProxyOne() {
        //创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //创建HttpGet对象，设置url访问地址
        HttpGet httpGet = new HttpGet(url);

        CloseableHttpResponse response = null;


        Proxy objects = null;
        try {
            //使用HttpClient发起请求，获取response
            response = httpClient.execute(httpGet);

            //解析响应
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "utf8");


//                proxies = JSON.parseObject(content, Proxies.class);


                String[] split = content.split("\n");
                for (String s : split) {
                    String[] split1 = s.split(":");
                    //加个判断,如果在ip黑名单中就不去获取

                    //去把id相等的数据查出来
                    ErrorProxy errorProxy = errorProxyService.finByHost(split1[0]);

                    if (errorProxy == null) {
                        objects = new Proxy(split1[0], Integer.valueOf(split1[1]));

                    }

                }


            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭response

            try {
                response.close();

                return objects;

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public List<Proxy> getHttpProxyDao() {



        List<Proxy> objects = new ArrayList<>();

        Proxies proxies = null;

        while (objects.size() < 10) {

            try {
                Thread.sleep(300);
                //创建HttpClient对象
                CloseableHttpClient httpClient = HttpClients.createDefault();

                //创建HttpGet对象，设置url访问地址
                HttpGet httpGet = new HttpGet(url);

                CloseableHttpResponse response = null;


                //使用HttpClient发起请求，获取response
                response = httpClient.execute(httpGet);


                //解析响应
                if (response.getStatusLine().getStatusCode() == 200) {
                    String content = EntityUtils.toString(response.getEntity(), "utf8");


                    proxies = JSON.parseObject(content, Proxies.class);


                    List<ProxiesBean> proxies1 = proxies.getProxies();
                    for (ProxiesBean proxiesBean : proxies1) {
                        //去把id相等的数据查出来
                        ErrorProxy errorProxy = errorProxyService.finByHost(proxiesBean.getIp());
                        //要没有重复数据才添加
                        if (errorProxy == null) {
                            objects.add(new Proxy(proxiesBean.getIp(), proxiesBean.getPort()));
                        }
                        System.out.println("代理地址在黑名单中:"+proxiesBean.getIp()+"\n" +
                                "当前拥有代理:"+objects.size()+"个");
                    }

                }
                if (proxies.getProxies().size() == 0) {
                    System.out.println("访问大佬给的代理连接池失败!注意查看原因");
                }


                response.close();
                httpClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return objects;

    }


    public List<Proxy> getProxiesWuYou() {
        List<Proxy> proxies = new ArrayList<>();

        Proxy httpProxyOne = getHttpProxyOne();
        //记录耗时
        Date date = new Date();

        //获取连接池的总个数
        int sum = 5;

        while (proxies.size() < sum) {

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (httpProxyOne != null) {
                if (proxies.size() > 0) {
                    //如果不是第一个就去对比
                    for (Proxy proxy : proxies) {
                        String host = proxy.getHost();

                        if (!httpProxyOne.getHost().equals(host)) {
                            proxies.add(httpProxyOne);
                            //如果当前获取到的代理服务器已经大于三个了,就返回
                            break;

                        }
                        //别忘了重新访问赋值
                        httpProxyOne = getHttpProxyOne();

                    }
                } else {
                    //如果是第一个就添加一个代理服务器
                    proxies.add(httpProxyOne);
                }

            } else {
                //重新去调用获取代理服务器
                System.out.println("获取代理服务器失败检查连接池获取");
                getProxiesWuYou();
            }
        }

        Date date1 = new Date();

        System.out.println("获取连接池[" + sum + "]耗时:" + (date1.hashCode() - date.hashCode()) / 1000 + "秒");

        return proxies;
    }

}
