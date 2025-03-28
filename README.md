# WEB3_4_HelloWorld_BE

> 프로그래머스 데브코스 3기 4회차 HelloWorld 팀 백엔드

<br>

# GymMate (backend)

GymMate 백엔드 레포지토리

<br>

## Swagger

swagger: [서버 실행 후 접속](http://localhost:8080/swagger-ui/index.html)

<br>

## ERD

[ERDCloud (팀 공개)](https://www.erdcloud.com/d/WcjqbEfh4n8qsvEaA)

<br>

## 환경 구축

### 개발 환경 구축

```
docker-compose -f docker/gymmate-dev/docker-compose.dev.yml up --build
```

데이터베이스 초기화 시 docker volume 삭제 필요:

```
//volume 목록 확인
docker volume ls

//volume 삭제
docker volume rm volume_name
```

