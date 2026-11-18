package config;

import java.io.IOException;
import java.util.logging.*;

public final class LoggerConfig {

    private LoggerConfig() {}

    public static void setup() throws IOException {

        Logger rootLogger = Logger.getLogger("");

        for(Handler handler: rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
        }

        rootLogger.setLevel(Level.ALL);

        Formatter formatter = new SimpleFormatter();

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        consoleHandler.setFormatter(formatter);

        FileHandler fileHandler = new FileHandler("group_app.log", true);
        fileHandler.setLevel(Level.ALL);
        fileHandler.setFormatter(formatter);

        rootLogger.addHandler(consoleHandler);
        rootLogger.addHandler(fileHandler);
    }
}
