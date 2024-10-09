package com.luv2code.spring_boot_library.config;

import com.luv2code.spring_boot_library.entity.Book;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    // CORS 설정에서 허용할 도메인 (프론트엔드)
    private String theAllowedOrigins = "http://localhost:3000";

    @Override
    // 비활성화할 HTTP 메소드 정의/CORS 설정 구성
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config,
                                                     CorsRegistry cors) {

        // 비활성화할 HTTP 메소드 (POST, PATCH, DELETE, PUT) 정의
        HttpMethod[] theUnsupportedActions = {HttpMethod.POST, HttpMethod.PATCH, HttpMethod.DELETE, HttpMethod.PUT};

        // Book 엔티티의 ID 값을 API 응답에 포함하여 데이터 식별 가능하도록 설정
        config.exposeIdsFor(Book.class);

        // Book 엔티티에 대해 정의한 HTTP 메소드 비활성화 적용
        disableHttpMethods(Book.class, config, theUnsupportedActions);

        // CORS 설정 (/api/** 경로에 대해 특정 도메인(프론트엔드)에서의 접근 허용)
        cors.addMapping(config.getBasePath() + "/**")
                .allowedOrigins(theAllowedOrigins);
    }


    // 지정된 엔티티(Book)에 대해 비활성화할 HTTP 메서드를 실제로 적용
    private void disableHttpMethods(Class theClass,
                                    RepositoryRestConfiguration config,
                                    HttpMethod[] theUnsupportedActions) {
        config.getExposureConfiguration()
                .forDomainType(theClass)
                // 아이템 단위(api/book/1)로 접근할 때 비활성화된 HTTP 메소드 적용
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                // 컬렉션 단위(api/book)로 접근할 때 비활성화된 HTTP 메소드 적용
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));
    }
}
