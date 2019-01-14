package com.xhxj.utils;


import com.xhxj.daomain.ErrorProxy;
import com.xhxj.service.ErrorProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@Component
public class ErrorProxyUtils {


    @Autowired
    ErrorProxyService errorProxyService;

    /**
     * 我有特殊的去重技巧
     * 程序员看了想打死我
     * 写都写了以后再优化
     */
    public void analysis() {
        FileWriter fileWriter = null;

        try {
            List<String> banProxyIP = Files.readAllLines(Paths.get("./error/banProxyIP.txt"), StandardCharsets.UTF_8);

            //读取完毕之后就清除之前的
            fileWriter = new FileWriter("./error/banProxyIP.txt");
            fileWriter.write("");

            List<String> listProxy = Files.readAllLines(Paths.get("./error/errorProxy.txt"), StandardCharsets.UTF_8);

            //读取完毕之后就清除之前的
            fileWriter = new FileWriter("./error/errorProxy.txt");
            fileWriter.write("");

            ErrorProxy errorProxy = null;
            String[] split = null;

            for (String s : banProxyIP) {
                int i = 0;
                errorProxy = new ErrorProxy();
                String[] split1 = s.split(":");
                for (String s1 : banProxyIP) {
                    String[] split2 = s1.split(":");
                    if (split1[0].equals(split2[0])) {
                        i++;
                        errorProxy.setCounter(i);
                    }
                }
                split = s.split(":");
                errorProxy.setHost(split[0]);
                errorProxy.setPort(Integer.valueOf(split[1]));
                errorProxy.setTxt(split[2]);
                errorProxy.setDate(new Date());
                errorProxy.setCounter(i);
                errorProxy.setState("ban");
                ErrorProxy errorProxy1 = errorProxyService.finByHost(split[0]);

                if (errorProxy1 == null) {
                    errorProxyService.save(errorProxy);
                }

                i = 0;

            }


            ErrorProxy errorProxy2 = null;

            for (String s : listProxy) {
                int i = 0;
                errorProxy2 = new ErrorProxy();
                String[] split1 = s.split(":");
                for (String s1 : listProxy) {
                    String[] split2 = s1.split(":");
                    if (split1[0].equals(split2[0])) {
                        i++;
                        errorProxy2.setCounter(i);
                    }
                }
                split = s.split(":");
                errorProxy2.setHost(split[0]);
                errorProxy2.setPort(Integer.valueOf(split[1]));
                errorProxy2.setDate(new Date());
                errorProxy2.setCounter(i);
                errorProxy2.setState("error");
                ErrorProxy errorall = errorProxyService.finByHost(split[0]);

                if (errorall == null) {
                    errorProxyService.save(errorProxy2);
                }

                i = 0;
            }


        } catch (IOException e) {

            System.out.println("读取error文件时报错");
            e.printStackTrace();
        }


    }
}
