cmd /C mvnw clean package -Dmaven.test.skip -f pom.xml
cmd /C docker-compose up
