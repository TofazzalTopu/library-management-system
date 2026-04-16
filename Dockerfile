# -------- Base Image (Distroless for security) --------
FROM eclipse-temurin:11-jre-alpine AS builder

WORKDIR /build
COPY target/api-gateway-service.jar app.jar

# -------- Extract Layers (Spring Boot Layered JAR) --------
RUN java -Djarmode=layertools -jar app.jar extract

# -------- Runtime Image --------
FROM gcr.io/distroless/java11

# -------- Metadata --------
LABEL application="api-gateway-service"

# -------- App Directory --------
WORKDIR /app

# -------- Copy Layered Files --------
COPY --from=builder /build/dependencies/ ./
COPY --from=builder /build/spring-boot-loader/ ./
COPY --from=builder /build/snapshot-dependencies/ ./
COPY --from=builder /build/application/ ./

# -------- Expose Port --------
EXPOSE 8080

# -------- JVM Options --------
ENV JAVA_OPTS="-XX:+UseContainerSupport \
               -XX:MaxRAMPercentage=75.0 \
               -XX:+UseG1GC \
               -XX:MaxGCPauseMillis=200 \
               -XX:+ExitOnOutOfMemoryError"

# -------- Health Check (Distroless workaround via Java) --------
HEALTHCHECK --interval=30s --timeout=5s --start-period=20s --retries=3 \
  CMD ["java", "-cp", "application:dependencies/*", "org.springframework.boot.loader.JarLauncher", "--spring.main.web-application-type=none", "--check.health=true"]

# -------- Run Application --------
ENTRYPOINT ["java", "-cp", "application:dependencies/*:spring-boot-loader/*", "org.springframework.boot.loader.JarLauncher"]