package com.ljl.note.collection.imClient.controller;

import com.ljl.note.collection.imClient.framework.netty.NettyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("nettyClient")
@RestController
public class NettyClientController {

    @Autowired
    private NettyClient nettyClient;

    @RequestMapping("sendTest")
    public void sendTest(){
        nettyClient.sendData();
    }

}
