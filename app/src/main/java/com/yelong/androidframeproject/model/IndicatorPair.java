package com.yelong.androidframeproject.model;

import java.io.File;

/**
 * 引导图实体类
 * Created by eyetech on 16/4/26.
 */
public class IndicatorPair {
    public int bg = -1;
    public int pos = -1;
    public String url;
    public File imgCache;

    public IndicatorPair() {
    }

    public IndicatorPair(String url, int pos) {
        this.url = url;
        this.pos = pos;
    }

    public IndicatorPair(int bgId, String urlN, int pos) {
        bg = bgId;
        url = urlN;
        this.pos = pos;
    }

    public IndicatorPair(int bg, int pos, File imgCache) {
        this.bg = bg;
        this.pos = pos;
        this.imgCache = imgCache;
    }
}
