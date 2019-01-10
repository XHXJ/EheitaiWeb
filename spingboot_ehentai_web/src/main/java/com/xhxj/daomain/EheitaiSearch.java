package com.xhxj.daomain;


import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class EheitaiSearch {

    /**
     * f_doujinshi : 1
     * f_manga : 1
     * f_artistcg : 1
     * f_gamecg : 1
     * f_western : 1
     * f_non-h : 1
     * f_imageset : 1
     * f_cosplay : 1
     * f_asianporn : 1
     * f_misc : 1
     * f_search : guro
     * f_apply : Apply+Filter
     */

    private String f_doujinshi;
    private String f_manga;
    private String f_artistcg;
    private String f_gamecg;
    private String f_western;
    private String f_nonh;
    private String f_imageset;
    private String f_cosplay;
    private String f_asianporn;
    private String f_misc;
    private String f_search;
    private String f_apply;
}
