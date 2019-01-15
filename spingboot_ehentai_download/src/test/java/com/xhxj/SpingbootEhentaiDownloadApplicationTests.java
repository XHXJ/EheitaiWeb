package com.xhxj;

import com.xhxj.Component.Download;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.apache.logging.log4j.Logger;

import javax.jms.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpingbootEhentaiDownloadApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void TestMq() throws JMSException {
        //创建ConnectionFactory
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.211.128:61616");

        //创建连接对象Connection
        Connection connection = connectionFactory.createConnection();

        //开启连接
        connection.start();

        //创建会话对象
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //创建需要接收的消息地址
        Queue test = session.createQueue("completeGid");


        //接收消息
        MessageConsumer consumer = session.createConsumer(test);

        while (true) {
            //等待10秒，在10秒内一直处于接收消息状态
            Message message = consumer.receive(10000);

            if (message != null) {
                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;

                    System.out.println("完成爬取的作品：" + textMessage.getText());
                    continue;
                }
            }
        }

        //关闭资源
//        session.close();
//        connection.close();


    }


    @Autowired
    Download start;

    private static final Logger logger = LogManager.getLogger(SpingbootEhentaiDownloadApplication.class);

}

