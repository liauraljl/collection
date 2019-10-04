package com.ljl.note.collection.liveRecord;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;
import tk.mybatis.spring.annotation.MapperScan;

@ImportResource(locations = {
		"classpath*:applicationContext-dubbo.xml"
})
@MapperScan(basePackages = "com.ljl.note.collection.liveRecord.mapper")
@EnableDubboConfiguration
@EnableAsync
@SpringBootApplication
public class LiveRecordServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiveRecordServiceApplication.class, args);
	}

}
