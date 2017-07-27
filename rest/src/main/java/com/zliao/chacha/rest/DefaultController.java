package com.zliao.chacha.rest;

import com.zliao.chacha.service.SegService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/")
public class DefaultController {
    private final static Logger logger = LoggerFactory.getLogger(DefaultController.class);

    @Autowired
    SegService segService;

    @RequestMapping(method= RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String hello (){
        logger.info("this is ai chat server");
        return "Hi, this is ai chat server";
    }

    @RequestMapping(value = "/hanlp", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String handleHaNLP(HttpServletRequest request) {
        return segService.hanlp_seg("测试实用的用具，喔，是用句语料");
    }
}
