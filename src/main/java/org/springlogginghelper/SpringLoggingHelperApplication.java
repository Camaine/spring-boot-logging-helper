package org.springlogginghelper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringLoggingHelperApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringLoggingHelperApplication.class, args);
    }

//    @Bean
//    @Logging
//    public void test() {
//
//    }
//
//    @Bean
//    @Logging
//    public void exceptionTest() throws Exception {
//        try {
//            Exception e = new Exception();
//            throw e;
//        } catch (Exception e) {
//            new LoggingException(e.getMessage());
//        }
//    }
}
