package io.hskim.learnspringcloudgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LearnSpringCloudGatewayApplication {

  public static void main(String[] args) {
    SpringApplication.run(LearnSpringCloudGatewayApplication.class, args);
  }

  @Bean
  public HttpExchangeRepository httpExchange() {
    return new InMemoryHttpExchangeRepository();
  }
}
