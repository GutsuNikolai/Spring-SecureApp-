<?xml version="1.0" encoding="UTF-8"?>
<configuration>

    <!-- Папка, где будут логи -->
    <property name="LOG_PATH" value="logs" />

    <!-- Формат -->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_PATH}/security-app.log</file>

        <!-- Автоматическая ротация логов -->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${LOG_PATH}/security-app.%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>7</maxHistory> <!-- Храним 7 дней -->
        </rollingPolicy>

        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <!-- Включаем логгирование в файл -->
    <root level="INFO">
        <appender-ref ref="FILE" />
    </root>

</configuration>
