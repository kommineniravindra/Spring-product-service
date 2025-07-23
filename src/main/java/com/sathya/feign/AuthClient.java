package com.sathya.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth-service", url = "https://spring-auth-service.onrender.com")
public interface AuthClient {

    @GetMapping("/api/auth/validate")
   public  String validateToken(@RequestHeader("Authorization") String token);
}
