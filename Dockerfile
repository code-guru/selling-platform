FROM java

WORKDIR /app
COPY src/ src/
COPY test/ test/
COPY resources/ resources/
COPY gradle/ gradle/
COPY scripts/ scripts/

COPY build.gradle gradle.properties gradlew settings.gradle ./

RUN ./gradlew -q --no-daemon installDist

FROM java

WORKDIR /app
COPY --from=0 /app/build/install/selling-platform .
COPY --from=0 /app/scripts ./scripts

ENTRYPOINT ["/app/scripts/start.sh"]


