/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources.config;

import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author nikli
 */
public class Config {

    private static Logger logger = LogManager.getLogger();

    private static String encoding = "UTF-8";

    //public static int BUFFER_SIZE = 1024;
    /**
     * @return the encoding
     */
    public static String getEncoding() {
        return encoding;
    }

    private Config() {

    }

    public static void loadConfig(Properties config) {
        encoding = config.getProperty("encoding", "UTF-8");
        /*try {
            BUFFER_SIZE = Integer.parseInt(config.getProperty("readBufferSize", "1024"));
        } catch (NumberFormatException ex) {
            BUFFER_SIZE = 1024;
        }*/
        logger.info("Configuration loaded: ");
        logger.info("\tencoding=" + encoding);

    }

}
