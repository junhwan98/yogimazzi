# 1. 빌드 이미지 설정
FROM eclipse-temurin:21-jdk AS builder

# 2. 작업 디렉토리 생성
WORKDIR /app

# 3. Gradle 관련 파일 복사
COPY gradlew gradlew.bat settings.gradle build.gradle ./
COPY gradle ./gradle

# 4. 모든 모듈 복사
COPY team1-api ./team1-api
COPY team1-domain ./team1-domain
COPY team1-infra ./team1-infra
COPY team1-common ./team1-common

# 5. Gradle 빌드
RUN chmod +x gradlew
RUN ./gradlew :team1-api:clean :team1-api:build -x test

# 6. 실행 이미지 생성
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/team1-api/build/libs/*.jar app.jar

# 7. 실행 명령어 설정
CMD ["java", "-jar", "app.jar"]
