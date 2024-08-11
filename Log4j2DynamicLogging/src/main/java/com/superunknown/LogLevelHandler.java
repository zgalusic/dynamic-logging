package com.superunknown;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

class LogLevelHandler implements HttpHandler {

    private static final Logger logger = LogManager.getLogger(LogLevelHandler.class);

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if ("PUT".equalsIgnoreCase(exchange.getRequestMethod())) {

            Map<String, String> queryParams = queryToMap(exchange.getRequestURI().getQuery());
            String loggerName = queryParams.get("logger");
            String level = queryParams.get("level");

            if (loggerName != null && level != null) {
                // Change the logging level
                LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
                LoggerConfig loggerConfig = ctx.getConfiguration().getLoggerConfig(loggerName);
                loggerConfig.setLevel(Level.toLevel(level));
                ctx.updateLoggers();

                String response = "Logger level for " + loggerName + " set to " + level;
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();

            } else {

                String response = "Missing 'logger' or 'level' parameter";
                exchange.sendResponseHeaders(400, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }

        } else if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {

            Map<String, String> queryParams = queryToMap(exchange.getRequestURI().getQuery());
            String loggerName = queryParams.get("logger");

            if (loggerName != null) {
                // Get the logging level
                LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
                LoggerConfig loggerConfig = ctx.getConfiguration().getLoggerConfig(loggerName);
                loggerConfig.getLevel();

                logger.debug("This is a debug message.");
                logger.info("This is an info message.");
                logger.warn("This is a warn message.");
                logger.error("This is an error message.");

                String response = "Current log level for logger: " + loggerName + " is: " + loggerConfig.getLevel();
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();

            } else {

                String response = "Missing 'logger'";
                exchange.sendResponseHeaders(400, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }

        } else {

            String response = "Unsupported HTTP method";
            exchange.sendResponseHeaders(405, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    // Helper method to parse query parameters
    private Map<String, String> queryToMap(String query) {

        Map<String, String> result = new HashMap<>();

        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length > 1) {
                    result.put(keyValue[0], keyValue[1]);
                } else {
                    result.put(keyValue[0], "");
                }
            }
        }

        return result;
    }
}
