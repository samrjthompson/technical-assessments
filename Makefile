artifact_name	:= template
version			:= unversioned

.PHONY: clean
clean:
	mvn clean
	rm -f ./$(artifact_name).jar
	rm -f ./$(artifact_name)-*.zip
	rm -f ./build-*
	rm -f ./build.log-*

.PHONY: test
test: clean
	mvn verify -Dskip.unit.tests=false -Dskip.integration.tests=false

.PHONY: unit-test
unit-test: clean
	mvn test

.PHONY: i-test
i-test: clean
	mvn verify -Dskip.unit.tests=true -Dskip.integration.tests=false

.PHONY: build
build: clean
	mvn versions:set -DnewVersion=$(version) -DgenerateBackupPoms=false
	mvn package -Dmaven.test.skip=true
	cp ./target/$(artifact_name)-$(version).jar ./$(artifact_name).jar