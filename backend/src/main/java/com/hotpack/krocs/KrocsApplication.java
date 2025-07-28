package com.hotpack.krocs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
    info = @Info(title = "Krocs API", version = "v1", description = "Krocs API 명세서")
)
@SpringBootApplication
public class KrocsApplication {

  public static void main(String[] args) {
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    SpringApplication.run(KrocsApplication.class, args);
  }

}
