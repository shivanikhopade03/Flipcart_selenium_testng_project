package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Reads key-value pairs from config.properties so environment/browser/URL
 * values are never hardcoded inside test or page classes.
 */
public class ConfigReader {

    private static Properties properties;
    private static final String CONFIG_PATH = "src/test/resources/config.properties";

    private ConfigReader() {
    }

    private static void loadProperties() {
        if (properties == null) {
            properties = new Properties();
            try (FileInputStream fis = new FileInputStream(CONFIG_PATH)) {
                properties.load(fis);
            } catch (IOException e) {
                throw new RuntimeException("Unable to load config.properties from " + CONFIG_PATH, e);
            }
        }
    }

    public static String get(String key) {
        loadProperties();
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property '" + key + "' not found in config.properties");
        }
        return value;
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }
}
