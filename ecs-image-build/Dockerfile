FROM samrjthompson/ci-corretto-build-21:latest

WORKDIR /opt
COPY ./ecs-image-build/docker_start.sh .

COPY target/application.jar application.jar

RUN chmod +x \
    ./docker_start.sh

CMD ["./docker_start.sh"]