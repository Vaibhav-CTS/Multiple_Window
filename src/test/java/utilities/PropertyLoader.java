package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyLoader {

    /**
     * Loads properties from the 'info.properties' file located in the project root.
     *
     * @return Properties object containing key-value pairs from the file.
     */
    public static Properties getProperties() {
        Properties props = new Properties();
        FileInputStream fs = null;

        try {
            // Load the properties file
            fs = new FileInputStream("./info.properties");
            props.load(fs);
        } catch (IOException e) {
            System.out.println("ERROR: Failed to load properties file - " + e.getMessage());
        } finally {
            // Close the file stream to avoid resource leaks
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    System.out.println("WARNING: Failed to close FileInputStream - " + e.getMessage());
                }
            }
        }

        return props;
    }
}
