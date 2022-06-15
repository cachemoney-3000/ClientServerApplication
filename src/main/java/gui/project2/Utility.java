/*
    Name: Joshua Samontanez
    Course: CNT 4714 Summer 2022
    Assignment title: Project 2 – A Two-tier Client-Server Application
    Date: June 26, 2022
    Class: C001
*/

package gui.project2;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Utility {
    public String[] readProperties(String propertiesName) {
        // Get the file path of the .property file depending on the property name that the user choose
        String filename = switch (propertiesName) {
            case "client.properties" -> "src/main/java/gui/project2/properties/client.properties";
            case "root.properties" -> "src/main/java/gui/project2/properties/root.properties";
            case "operations.properties" -> "src/main/java/gui/project2/properties/operations.properties";
            default -> "";
        };

        // Load the property file
        Properties prop = loadProperties(filename);

        // Extract the information from the property file and return
        String user = prop.getProperty("user");
        String pass = prop.getProperty("pass");
        String url = prop.getProperty("url");
        String driver = prop.getProperty("driver");

        return new String[]{user, pass, url, driver};
    }

    private static Properties loadProperties(String fileName) {
        Properties prop = new Properties();
        // Try to load the .properties file
        try (FileInputStream inputStream = new FileInputStream(fileName)){
            prop.load(inputStream);
        } catch(IOException e) {
            e.printStackTrace();
        }

        return prop;
    }
}
