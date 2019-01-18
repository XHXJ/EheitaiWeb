package com.xhxj.Component;

import com.xhxj.service.EheitaiCatalogService;
import com.xhxj.service.EheitaiDetailPageService;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.*;

@Component
public class DownloadStart {
    @Autowired
    Download download;

    @Autowired
    EheitaiCatalogService eheitaiCatalogService;
    @Autowired
    EheitaiDetailPageService eheitaiDetailPageService;


    /**
     * 开始接受作品完成消息
     *
     * @throws JMSException
     */
    @Scheduled(initialDelay = 1000, fixedDelay =  60*1000)
    public void Test() throws JMSException {


        while (true) {

            //试试不睡能不能撑住,不行,主要是代理服务器撑不住.宽带太小了.
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        //创建ConnectionFactory
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.211.131:61616");
        //创建会话对象


            //创建连接对象Connection
            Connection connection = connectionFactory.createConnection();

            //开启连接
            connection.start();


            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            //创建需要接收的消息地址
            Queue test = session.createQueue("completeGid");


            //接收消息
            MessageConsumer consumer = session.createConsumer(test);

            //不让他睡会的话高并发起来就被打死了....
            System.out.println("mq接收还活着");
            //等待10秒，在10秒内一直处于接收消息状态


            Message message = consumer.receive();
            if (message != null) {
                if (message instanceof TextMessage) {

                    TextMessage textMessage = (TextMessage) message;

                    System.out.println("完成爬取的作品：" + textMessage.getText());

                    String text = textMessage.getText();

                    String[] split = text.split("@");


                    //这里需要分别调用spingboot的多线程new 的多线程无法使用
                    download.downloadImgurl(split);
                    //关闭资源

                    session.close();
                    connection.close();

                }
            }
        }



    }


}
