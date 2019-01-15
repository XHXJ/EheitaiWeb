package com.xhxj.spingboot_ehentai_download.Component;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.*;

@Component
public class start {

    /**
     * 开始接受作品完成消息
     * @throws JMSException
     */
    @Scheduled(initialDelay = 1000, fixedDelay = 1 * 60 * 60 * 1000)
    public void Test() throws JMSException {

        System.out.println("等待接受消息");
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

        while (true){
            //等待10秒，在10秒内一直处于接收消息状态
            Message message = consumer.receive(10000);

            if(message!=null){
                if(message instanceof  TextMessage){
                    TextMessage textMessage = (TextMessage) message;

                    System.out.println("完成爬取的作品："+textMessage.getText());
                    continue;
                }
            }
        }

        //关闭资源
//        session.close();
//        connection.close();
    }
}

