#!/bin/bash
if [ -f /.dockerenv ]; then
   echo "You can not start the development environment inside a docker container"
else
	DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
	pushd $DIR >/dev/null
	DOCKER_BUILDKIT=1 docker build -f src/dev/docker/Dockerfile -t quarkus/reactive-mongo-events-error:dev .
	if [ $? -eq 0 ]; then
		DOCKER_PARAMS="--rm --name quarkus_reactive_mongo_events_error_dev -v /var/run/docker.sock:/var/run/docker.sock -p 5005:5005 -p 8080:8080 -it"
		if [[ "$OSTYPE" == "darwin"* ]]; then
			DOCKER_PARAMS="$DOCKER_PARAMS -e TESTCONTAINERS_HOST_OVERRIDE=docker.for.mac.host.internal"
		fi
		docker run $DOCKER_PARAMS -v "${HOME}/.m2":/root/.m2  -v "${PWD}":/app quarkus/reactive-mongo-events-error:dev /bin/bash
		./stopDevelopmentEnvironment.sh
	fi
	popd >/dev/null
fi
