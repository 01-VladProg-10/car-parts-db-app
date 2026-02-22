package util;

import java.io.InputStream;
import java.util.Properties;

/**
 * Application configuration loader.
 * Loads properties from application.properties file.
 */
public class Config {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = Config.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {

            if (input == null) {
                throw new RuntimeException("application.properties file not found");
            }

            properties.load(input);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load application configuration", e);
        }
    }

    /**
     * Returns property value by key.
     *
     * @param key configuration key
     * @return property value
     */
    public static String get(String key) {
        return properties.getProperty(key);
    }
}