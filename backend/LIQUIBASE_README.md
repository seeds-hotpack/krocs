# Liquibase 데이터베이스 마이그레이션 가이드

## 개요
이 프로젝트는 Liquibase를 사용하여 데이터베이스 스키마를 관리합니다.

## 디렉토리 구조
```
src/main/resources/
├── db/
│   └── changelog/
│       ├── db.changelog-master.yaml          # 마스터 changelog 파일
│       └── changes/                          # 개별 변경사항 파일들
│           ├── 001-initial-schema.yaml       # 초기 스키마
│           ├── 002-add-new-table.yaml        # 새 테이블 추가
│           └── 003-add-column.yaml           # 컬럼 추가
```

## 설정

### 개발 환경 (application-dev.yml)
```yaml
spring:
  liquibase:
    enabled: true
    change-log: classpath:db/changelog/db.changelog-master.yaml
    contexts: dev
    default-schema: public
```

### 테스트 환경 (application-test.yml)
```yaml
spring:
  liquibase:
    enabled: true
    change-log: classpath:db/changelog/db.changelog-master.yaml
    contexts: test
    default-schema: PUBLIC
```

### 프로덕션 환경 (application-prod.yml)
```yaml
spring:
  liquibase:
    enabled: true
    change-log: classpath:db/changelog/db.changelog-master.yaml
    contexts: prod
    default-schema: public
```

## 새로운 마이그레이션 추가하기

### 1. 새로운 changelog 파일 생성
`src/main/resources/db/changelog/changes/` 디렉토리에 새로운 YAML 파일을 생성합니다.

예시: `004-add-user-table.yaml`
```yaml
databaseChangeLog:
  - changeSet:
      id: 004
      author: developer
      changes:
        - createTable:
            tableName: users
            columns:
              - column:
                  name: user_id
                  type: bigint
                  autoIncrement: true
                  constraints:
                    primaryKey: true
                    nullable: false
              - column:
                  name: username
                  type: varchar(50)
                  constraints:
                    nullable: false
                    unique: true
```

### 2. 마스터 changelog에 포함시키기
`db.changelog-master.yaml` 파일에 새로운 changelog를 추가합니다.

```yaml
databaseChangeLog:
  - include:
      file: db/changelog/changes/001-initial-schema.yaml
      relativeToChangelogFile: false
  - include:
      file: db/changelog/changes/004-add-user-table.yaml
      relativeToChangelogFile: false
```

## 주요 Liquibase 명령어

### 테이블 생성
```yaml
- createTable:
    tableName: table_name
    columns:
      - column:
          name: column_name
          type: data_type
          constraints:
            nullable: false
            primaryKey: true
```

### 컬럼 추가
```yaml
- addColumn:
    tableName: table_name
    columns:
      - column:
          name: new_column
          type: varchar(100)
          constraints:
            nullable: true
```

### 인덱스 생성
```yaml
- createIndex:
    tableName: table_name
    indexName: idx_column_name
    columns:
      - column:
          name: column_name
```

### 외래키 추가
```yaml
- addForeignKeyConstraint:
    baseTableName: child_table
    baseColumnNames: foreign_key_column
    constraintName: fk_constraint_name
    referencedTableName: parent_table
    referencedColumnNames: primary_key_column
```

## 실행

### 애플리케이션 시작 시 자동 실행
Spring Boot 애플리케이션을 시작하면 Liquibase가 자동으로 실행됩니다.

### 수동 실행
```bash
# Gradle을 통한 실행
./gradlew bootRun

# 또는 JAR 파일로 실행
java -jar build/libs/krocs-0.0.1-SNAPSHOT.jar
```

## 주의사항

1. **ChangeSet ID는 고유해야 합니다**: 각 changeSet의 id는 전체 프로젝트에서 고유해야 합니다.
2. **Rollback 고려**: 가능하면 rollback을 지원하는 변경사항을 작성하세요.
3. **테스트**: 새로운 마이그레이션을 추가하기 전에 테스트 환경에서 먼저 테스트하세요.
4. **백업**: 프로덕션 환경에 적용하기 전에 데이터베이스 백업을 수행하세요.

## 문제 해결

### 마이그레이션 실패 시
1. 데이터베이스 연결 확인
2. changelog 파일 문법 확인
3. 이미 적용된 changeSet 확인
4. 로그 확인

### 롤백
```yaml
- changeSet:
    id: 001
    author: system
    changes:
      - createTable:
          tableName: example
          columns:
            - column:
                name: id
                type: bigint
    rollback:
      - dropTable:
          tableName: example
``` 