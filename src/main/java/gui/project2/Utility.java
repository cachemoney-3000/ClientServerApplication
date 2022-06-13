package gui.project2;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Utility {
    public String[] readProperties(String propertiesName) throws IOException {
        String filename = "";
        if (propertiesName.equals("client.properties"))
            filename = "src/main/java/gui/project2/properties/client.properties";
        else if (propertiesName.equals("root.properties"))
            filename = "src/main/java/gui/project2/properties/root.properties";
        else if (propertiesName.equals("operations.properties"))
            filename = "src/main/java/gui/project2/properties/operations.properties";

        Properties prop = loadProperties(filename);

        String user = prop.getProperty("user");
        String pass = prop.getProperty("pass");
        String database = prop.getProperty("database");

        return new String[]{user, pass, database};
    }

    private static Properties loadProperties(String fileName) throws IOException {
        FileInputStream inputStream = null;
        Properties prop = null;

        try {
            inputStream = new FileInputStream(fileName);
            prop = new Properties();
            prop.load(inputStream);
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            inputStream.close();
        }

        return prop;
    }
}
