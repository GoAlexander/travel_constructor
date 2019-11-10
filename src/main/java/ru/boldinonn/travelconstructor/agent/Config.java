/* Course work
 * -----------
 *
 * Config.java
 *
 * Author: Alexander Dydychkin
 * email: AlexanderDydychkin@yandex.ru
 *
 */

package ru.boldinonn.travelconstructor.agent;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Config {
    private static Properties prop = new Properties();
    private String path;

    public Config(String path) {
        System.out.println("./config/" + path + ".conf");
        this.path = path;
    }

    //To generate config file again
    public static void main(String[] args) {

        try (OutputStream output = new FileOutputStream("./config/MyCognitiveAgent.conf")) {
            // set the properties value
            prop.setProperty("internalOnt", "http://x556uq/RequirementsOfOrderManagerToDelivery-reification.owl");

            // save properties to project root folder
            prop.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    //TODO Remove? (not used anywhere)
    public String getProperty(String key) {
        String result = null;
        InputStream input = null;

        try {
            input = new FileInputStream("./config/" + path + ".conf");

            // load a properties file
            prop.load(input);
            result = prop.getProperty(key);
            System.out.println(result);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }
}
