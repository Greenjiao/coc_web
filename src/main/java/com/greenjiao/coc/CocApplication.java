package com.greenjiao.coc;

import com.greenjiao.coc.security.config.SecurityProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({SecurityProperties.class})
public class CocApplication {

    public static void main(String[] args) {
        SpringApplication.run(CocApplication.class, args);
    }

}
