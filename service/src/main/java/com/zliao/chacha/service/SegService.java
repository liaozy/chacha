package com.zliao.chacha.service;

import com.hankcs.hanlp.HanLP;
import org.springframework.stereotype.Component;

@Component
public class SegService {
    public String hanlp_seg(String input){
        System.out.println(HanLP.segment("你好，欢迎使用HanLP汉语处理包！"));
        return HanLP.segment(input).toString();
    }
}
