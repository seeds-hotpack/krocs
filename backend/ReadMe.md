# Krocs Backend

## 개요
Krocs 프로젝트의 백엔드 애플리케이션입니다.

## 기술 스택
- Java 21
- Spring Boot 3.5.3
- Spring Data JPA
- Liquibase (데이터베이스 마이그레이션)
- H2 Database (개발/테스트)
- PostgreSQL (프로덕션)

## 개발 환경 설정

### 1. Java 21 설치
```bash
# macOS
brew install openjdk@21

# 환경 변수 설정
export JAVA_HOME=/opt/homebrew/opt/openjdk@21
export PATH=$JAVA_HOME/bin:$PATH
```

### 2. 애플리케이션 실행
```bash
# 개발 환경
./gradlew bootRun --args='--spring.profiles.active=dev'

# 테스트 환경
./gradlew bootRun --args='--spring.profiles.active=test'
```

### 3. 데이터베이스 접속

#### H2 콘솔 (개발 환경)
- **URL**: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:file:./data/krocs_dev`
- **Username**: `sa`
- **Password**: (비어있음)

#### DataGrip 연결
- **URL**: `jdbc:h2:file:/{PROJECT ROOT}/backend/data/krocs_dev`
- **Username**: `sa`
- **Password**: (비어있음)

## 데이터베이스 마이그레이션

이 프로젝트는 Liquibase를 사용하여 데이터베이스 스키마를 관리합니다.

### 마이그레이션 파일 위치
```
src/main/resources/db/changelog/
├── db.changelog-master.yaml          # 마스터 changelog
└── changes/                          # 개별 변경사항
    └── 001-initial-schema.yaml       # 초기 스키마
```

### 새로운 마이그레이션 추가
1. `changes/` 디렉토리에 새로운 YAML 파일 생성
2. `db.changelog-master.yaml`에 include 추가
3. 애플리케이션 재시작

자세한 내용은 [LIQUIBASE_README.md](LIQUIBASE_README.md)를 참조하세요.

## 프로젝트 구조
```
src/main/java/com/hotpack/krocs/
├── common/
│   ├── dto/           # DTO 클래스들
│   ├── entity/        # JPA 엔티티들
│   └── exception/     # 예외 처리
└── KrocsApplication.java
```

## 빌드 및 테스트
```bash
# 빌드
./gradlew clean build

# 테스트 실행
./gradlew test
```

## 환경별 설정
- **개발**: `application-dev.yml` (H2 인메모리)
- **테스트**: `application-test.yml` (H2 인메모리)
- **프로덕션**: `application-prod.yml` (PostgreSQL)

## 주의사항
- `data/` 디렉토리의 데이터베이스 파일은 Git에 포함되지 않습니다
- 민감한 정보는 `application-secret.yml`에 저장하고 Git에 포함하지 마세요
