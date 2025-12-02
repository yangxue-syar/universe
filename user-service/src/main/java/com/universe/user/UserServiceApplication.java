package com.universe.user;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.universe.user.feign")
@MapperScan("com.universe.user.mapper")
public class UserServiceApplication {
    private  static  final Logger LOG= LoggerFactory.getLogger(UserServiceApplication.class);
    public static void main(String[] args) throws UnknownHostException {
        SpringApplication application = new SpringApplication(UserServiceApplication.class);
//        SpringApplication.run(MovieServiceApplication.class, args);
        ConfigurableApplicationContext context = application.run(args);  // 只调用一次
        Environment env = context.getEnvironment();
        LOG.info("启动成功！！！");
        String serverPort = env.getProperty("server.port", "9102");
        String contextPath = env.getProperty("server.servlet.context-path", "");
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        LOG.info("测试地址:\thttp://{}:{}{}/", new Object[]{hostAddress, serverPort, contextPath});
        System.out.println("========================================");
        System.out.println("✅ 用户服务启动成功！");
        System.out.println("📝 Swagger: http://localhost:8082/swagger-ui.html");
        System.out.println("========================================");
    }
}
