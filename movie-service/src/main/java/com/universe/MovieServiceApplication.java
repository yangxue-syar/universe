package com.universe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
public class MovieServiceApplication {
    private  static  final Logger LOG= LoggerFactory.getLogger(MovieServiceApplication.class);
    public static void main(String[] args) throws UnknownHostException {
        SpringApplication application = new SpringApplication(MovieServiceApplication.class);
//        SpringApplication.run(MovieServiceApplication.class, args);
        ConfigurableApplicationContext context = application.run(args);  // 只调用一次
        Environment env = context.getEnvironment();
        LOG.info("启动成功！！！");
        String serverPort = env.getProperty("server.port", "9103");
        String contextPath = env.getProperty("server.servlet.context-path", "");
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        LOG.info("测试地址:\thttp://{}:{}{}/", new Object[]{hostAddress, serverPort, contextPath});
    }

}
