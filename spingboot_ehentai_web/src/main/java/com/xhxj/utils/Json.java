package com.xhxj.utils;

import com.alibaba.fastjson.JSON;
import com.xhxj.daomain.EheitaiSearch;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Json {
    /**
     * json转换
     */
    public static List<EheitaiSearch> changejson() {
        BufferedReader reader = null;
        String laststr = "";
        try {
            FileInputStream fileInputStream = new FileInputStream("E://json");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                laststr += tempString;
            }
//            System.out.println("转换前:" + laststr);
            List<EheitaiSearch> eheitaiSearches = JSON.parseArray(laststr, EheitaiSearch.class);
            reader.close();
            return eheitaiSearches;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
