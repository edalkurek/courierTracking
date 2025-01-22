# Amazon Corretto 17 tabanlı bir imaj kullanıyoruz
FROM amazoncorretto:21

# Uygulama için çalışma dizini oluştur
WORKDIR /app

# Maven tarafından üretilen JAR dosyasını Docker imajına kopyala
COPY target/courierTracking-0.0.1-SNAPSHOT.jar app.jar

# Uygulamayı çalıştır
ENTRYPOINT ["java", "-jar", "app.jar"]
