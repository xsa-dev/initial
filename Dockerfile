FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY /build/libs /app/lib
CMD ["--spring.config.location=classpath:/srv/app/configs/application.properties"]

ENTRYPOINT ["java","-cp","app:app/lib/*","test.Application"]