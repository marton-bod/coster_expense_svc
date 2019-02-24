FROM adoptopenjdk/openjdk11:alpine-slim
ADD /expense_svc-*.jar /expense_svc.jar
ARG JAVA_ARGS
ENTRYPOINT ["sh", "-c", "java $JAVA_ARGS -jar expense_svc.jar"]
