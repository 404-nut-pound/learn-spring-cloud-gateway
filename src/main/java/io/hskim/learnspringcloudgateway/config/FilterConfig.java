package io.hskim.learnspringcloudgateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

// @Configuration
public class FilterConfig {

  // @Bean
  public RouteLocator routeLocator(RouteLocatorBuilder builder) {
    return builder
      .routes()
      .route(r ->
        r
          .path("/first-service/**")
          .filters(f ->
            f
              .addRequestHeader("gateway-request", "first-service-request")
              .addResponseHeader("gateway-response", "first-service-response")
          )
          .uri("http://localhost:8081")
      )
      .route(r ->
        r
          .path("/second-service/**")
          .filters(f ->
            f
              .addRequestHeader("gateway-request", "second-service-request")
              .addResponseHeader("gateway-response", "second-service-response")
          )
          .uri("http://localhost:8082")
      )
      .build();
  }
}
