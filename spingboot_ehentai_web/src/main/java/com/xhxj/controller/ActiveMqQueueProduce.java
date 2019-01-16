package com.xhxj.controller;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.stereotype.Component;

import javax.jms.*;

@Component
public class ActiveMqQueueProduce {
    /**
     *发送mq消息通知下载器
     * @param gid
     * @param imgUrl
     * @param fileLog
     */
    public  void postMessage(String s) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.211.128:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);

        TextMessage textMessage = session.createTextMessage();
        textMessage.setText(s);

        Queue test = session.createQueue("completeGid");
        MessageProducer producer = session.createProducer(test);

        producer.send(textMessage);

        session.close();
        connection.close();



    }
}
