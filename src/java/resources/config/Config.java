/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utility.QException;

/**
 *
 * @author nikli
 */
public class Config {

    private static Logger logger = LogManager.getLogger();

    private static Map<String, String> configMap = new HashMap<>();

    private static String encoding = "UTF-8";
    private static String praatCommand;
    private static String uploadPath;
    private static String smsSenderUrl;
    private static String smsSenderUsername;
    private static String smsSenderPassword;
    private static String smsSenderSenderName;
    private static String selectEndLimitDefault;
    private static String sqlPoolDbNumber;

    public static String getProperty(String key) {
        return configMap.get(key);
    }

    public static boolean getPropertyBool(String key) {
        return Boolean.parseBoolean(configMap.get(key));
    }

    public static int getPropertyInt(String key) throws QException {
        try {
            return Integer.parseInt(configMap.get(key));
        } catch (NumberFormatException ex) {
            logger.error(ex.getMessage() + " for " + key + "=" + configMap.get(key), ex);
            throw new QException(ex.getMessage() + " for " + key + "=" + configMap.get(key), ex);
        }
    }

    //public static int BUFFER_SIZE = 1024;
    /**
     * @return the encoding
     */
    public static String getEncoding() {
        return encoding;
    }

    public static String getPraatCommand() {
        return praatCommand;
    }

    public static String getUploadPath() {
        return uploadPath;
    }

    /**
     * @return the smsSenderUrl
     */
    public static String getSmsSenderUrl() {
        return smsSenderUrl;
    }

    /**
     * @return the smsSenderUsername
     */
    public static String getSmsSenderUsername() {
        return smsSenderUsername;
    }

    /**
     * @return the smsSenderPassword
     */
    public static String getSmsSenderPassword() {
        return smsSenderPassword;
    }

    /**
     * @return the smsSenderSenderName
     */
    public static String getSmsSenderSenderName() {
        return smsSenderSenderName;
    }

    /**
     * @return the selectEndLimitDefault
     */
    public static String getSelectEndLimitDefault() {
        return selectEndLimitDefault;
    }

    /**
     * @return the sqlPoolDbNumber
     */
    public static String getSqlPoolDbNumber() {
        return sqlPoolDbNumber;
    }

    private Config() {

    }

    public static void loadConfig(Properties properties) {
        for (final String name : properties.stringPropertyNames()) {
            configMap.put(name, properties.getProperty(name));
        }

        encoding = properties.getProperty("encoding", "UTF-8");
        praatCommand = properties.getProperty("praat.command");
        uploadPath = properties.getProperty("upload.path");
        smsSenderUrl = properties.getProperty("sms.sender.url");
        smsSenderUsername = properties.getProperty("sms.sender.username");
        smsSenderPassword = properties.getProperty("sms.sender.password");
        smsSenderSenderName = properties.getProperty("sms.sender.sendername");
        selectEndLimitDefault = properties.getProperty("db.select.end-limit.default");
        sqlPoolDbNumber = properties.getProperty("db.sql-pool.db-number");

        logger.info("Configuration loaded: ");
        logger.info("\tcongig=" + configMap);
    }

}
