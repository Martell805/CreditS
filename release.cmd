cmd /C mvnw clean package -Dmaven.test.skip -f pom.xml
cmd /C docker build -t martell805/credits:1.0 .
cmd /C docker push martell805/credits:1.0
