package com.ljl.note.collection.process.es;

import com.alibaba.fastjson.JSON;
import com.ljl.note.collection.process.service.es.EsNodeUser;
import com.ljl.note.collection.process.service.es.UserNodeEsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EsTest {

    @Autowired
    private UserNodeEsService userNodeEsService;

    @Test
    public void createIndex(){
        userNodeEsService.createIndex(//类型定义
                "{\n" +
                        "\"tweet\": {\n" +
                        "\"properties\": {\n" +
                        "\"message\": {\n" +
                        "\"type\": \"text\"\n" +
                        "}\n" +
                        "}\n" +
                        "}\n" +
                        "}");//类型映射，需要的是一个JSON字符串
    }

    @Test
    public void updateModel(){
        EsNodeUser esNodeUser=new EsNodeUser();
        esNodeUser.setUserId("user1");
        esNodeUser.setNodeId("node1");
        esNodeUser.setCreateTime(System.currentTimeMillis());
        esNodeUser.setId(esNodeUser.getId()+"233");
        userNodeEsService.update(esNodeUser);
    }

    @Test
    public void findOne(){
        EsNodeUser esNodeUser=userNodeEsService.findOne("node1user1");
        System.err.println(JSON.toJSONString(esNodeUser));
    }
}
