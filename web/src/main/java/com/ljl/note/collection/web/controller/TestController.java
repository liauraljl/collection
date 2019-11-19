package com.ljl.note.collection.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 压力测试
 */
@RestController
@RequestMapping("pressTestController")
public class TestController {

    private AtomicInteger n=new AtomicInteger();

    @RequestMapping("test")
    public String test(){
        System.err.println(n.incrementAndGet()+":test！！");
        return "test!!!!!!!!!!!!!!";
    }
}
