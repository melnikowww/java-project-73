clean:
	./gradlew clean

build:
	SPRING_PROFILES_ACTIVE=development ./gradlew clean build

install:
	./gradlew clean install

start:
	SPRING_PROFILES_ACTIVE=development ./gradlew bootRun

start-prod:
	./gradlew bootRun --args='--spring.profiles.active=production'

run-dist:
	./build/install/app/bin/app

start-dist:
	APP_ENV=production ./build/install/app/bin/app

run:
	./gradlew bootRun

test:
	SPRING_PROFILES_ACTIVE=development ./gradlew test

report:
	./gradlew jacocoTestReport

lint:
	./gradlew checkstyleMain checkstyleTest

update-deps:
	./gradlew useLatestVersions

stage:
	./gradlew stage

swagger:
	gradle clean generateOpenApiDocs

.PHONY: build

