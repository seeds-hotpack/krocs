## 로컬 Redis 실행 (Docker Compose)

1) Redis 컨테이너 실행

```bash
cd docker/redis
docker compose up -d
```

2) 종료/정리

```bash
docker compose down
```

3) 포트/환경변수

- `REDIS_PORT`로 호스트 포트를 조정할 수 있습니다. 기본값은 6379입니다.

> 컨테이너는 `krocs-redis-data` 볼륨을 사용하여 데이터를 보존합니다.



