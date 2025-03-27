test 커밋

# WEB3_4_HelloWorld_BE
프로그래머스 데브코스 3기 4회차 HelloWorld 백엔드

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

