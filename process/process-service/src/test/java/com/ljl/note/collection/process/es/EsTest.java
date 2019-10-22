package com.ljl.note.collection.process.es;

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
        userNodeEsService.createIndex(userNodeEsService.indexName());
    }
}
