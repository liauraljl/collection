package com.ljl.note.collection.liveRecord;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;
import tk.mybatis.spring.annotation.MapperScan;


@MapperScan(basePackages = "com.ljl.note.collection.liveRecord.mapper")
@EnableDubboConfiguration
@EnableAsync
@ImportResource(locations = {
		"classpath*:applicationContext-dubbo.xml"
})
@SpringBootApplication
public class LiveRecordServiceApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(LiveRecordServiceApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(LiveRecordServiceApplication.class, args);
	}

}
