package com.ljl.note.collection;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@ImportResource(locations = {
		"classpath*:applicationContext-dubbo.xml"
})
@EnableDubboConfiguration
@SpringBootApplication
public class LiveRecordServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiveRecordServiceApplication.class, args);
	}

}
