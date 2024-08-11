package com.superunknown;

import com.sun.net.httpserver.HttpServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Log4j2DynamicLogging {

    private static final Logger logger = LogManager.getLogger(Log4j2DynamicLogging.class);

    public static void main(String[] args) throws IOException {

        // Start a simple HTTP server on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/loggers", new LogLevelHandler());
        server.setExecutor(null);
        server.start();

        logger.info("Server started on port 8080");

        // Sample logging statements
        logger.debug("This is a debug message.");
        logger.info("This is an info message.");
        logger.warn("This is a warn message.");
        logger.error("This is an error message.");
    }

}

