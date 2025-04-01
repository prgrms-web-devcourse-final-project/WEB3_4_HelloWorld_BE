```
api:
  title: 짐메이트 (GymMate)
  description: 헬스장 정보를 한 눈에! PT 수업 결제까지~
  version: 1.0.0

server:
  url: http://localhost:8080
client:
  url: http://localhost:3000
  
spring:
  profiles:
    active: prod
  application:
    name: gymmate
  data:
    redis:
      port: 6379
      host: localhost
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 20MB

  security:
    oauth2:
      client:
        registration:
          kakao:
            client-id: ${OAUTH_KAKAO_CLIENT_ID}
            client-secret: ${OAUTH_KAKAO_CLIENT_SECRET}
            authorization-grant-type: authorization_code
            redirect-uri: ${server.url}/${app.oauth2.redirect-uri-suffix}
            client-name: Kakao
            client-authentication-method: client_secret_post

        provider:
          kakao:
            authorization-uri: https://kauth.kakao.com/oauth/authorize
            token-uri: https://kauth.kakao.com/oauth/token
            user-info-uri: https://kapi.kakao.com/v2/user/me
            user-name-attribute: id

app:
  oauth2:
    redirect-uri-suffix: ${OAUTH_KAKAO_REDIRECT_URI_SUFFIX}

jwt:
  secret: ${JWT_SECRET}
  redirect: ${client.url}
  redirect-login: ${client.url}/login
  access-token:
    expiration-time: 3600000
  refresh-token:
    expiration-time: 604800000
    
cloud:
  aws:
    s3:
      bucket: devcouse4-team12-bucket
    credentials:
      access-key: ${S3_ACCESS_KEY}
      secret-key: ${S3_SECRET_KEY}
    region:
      staticRegion: ap-northeast-2
      auto: false
    stack:
      auto: false
      
kakao-map:
  api:
    key: ${KAKAO_API_KEY}
    
business:
  url: ${BUSINESS_URL}
  serviceKey: ${BUSINESS_KEY}
```