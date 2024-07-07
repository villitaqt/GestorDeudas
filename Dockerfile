# Usamos una imagen base de Java
FROM openjdk:17-jdk-slim

# Configuramos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el archivo JAR generado por Maven en el directorio de trabajo
COPY target/gestor-deudas-0.0.2-SNAPSHOT.jar app.jar

# Exponemos el puerto en el que nuestra aplicación escuchará
EXPOSE 8080

# Definimos el comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
