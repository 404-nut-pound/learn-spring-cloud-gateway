package io.hskim.learnspringcloudgateway.filter;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizationHeaderFilter
  extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

  @Value("${jwt.secret-key}")
  private String secretKey;

  public static class Config {}

  public AuthorizationHeaderFilter() {
    super(Config.class);
  }

  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) -> {
      ServerHttpRequest request = exchange.getRequest();

      if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
        return onError(
          exchange,
          "No authorization header.",
          HttpStatus.UNAUTHORIZED
        );
      }

      String authorizationHeader = request
        .getHeaders()
        .get(HttpHeaders.AUTHORIZATION)
        .get(0);

      String token = authorizationHeader.replace("Bearer ", "");

      if (!isJwtValid(token)) {
        return onError(exchange, "JWT is invalid.", HttpStatus.UNAUTHORIZED);
      }

      return chain.filter(exchange);
    };
  }

  private Mono<Void> onError(
    ServerWebExchange exchange,
    String errorMessage,
    HttpStatus httpStatus
  ) {
    ServerHttpResponse response = exchange.getResponse();

    response.setStatusCode(httpStatus);

    log.error(errorMessage);

    return response.setComplete();
  }

  private boolean isJwtValid(String token) {
    String subject = null;

    try {
      subject =
        Jwts
          .parser()
          .setSigningKey(secretKey)
          .parseClaimsJws(token)
          .getBody()
          .getSubject();
    } catch (Exception e) {
      return false;
    }

    if (!StringUtils.hasText(subject)) {
      return false;
    }

    return true;
  }
}
