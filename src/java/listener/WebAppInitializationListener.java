/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listener;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ehcache.CacheManager;
import org.ehcache.config.Configuration;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.xml.XmlConfiguration;
import resources.config.Config;
import utility.CacheUtil;
import utility.CallDispatcher;
import utility.SessionManager;
import utility.sqlgenerator.DBConnectionPool;


/**
 * Web application lifecycle listener.
 *
 * @author nikli
 */
@WebListener()
public class WebAppInitializationListener implements ServletContextListener {

    private static final String CONF_DIR_NAME_OF_CATALINA = "conf";
    private static final String NAME_OF_DIR_IN_CONF = "apd";
    private static final String CONFIG_PROPERTIES_FILE = "config.properties";

    private final Logger logger = LogManager.getLogger();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //String configFolderPath = System.getProperty("catalina.base")
        //        + File.separator + CONF_DIR_NAME_OF_CATALINA
        //        + File.separator + NAME_OF_DIR_IN_CONF + File.separator;
        try {
            final String configFilePath = sce.getServletContext().getResource("/WEB-INF/conf/"+CONFIG_PROPERTIES_FILE)
                    .getPath();
            loadConfiguration(configFilePath);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getClass().getCanonicalName()
                    + " occurred while getting path", ex);
        }
        
        
        try {
            
            final URL configUrl = sce.getServletContext().getResource("/WEB-INF/conf/ehcache.xml");
            CacheUtil.initCache(configUrl);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getClass().getCanonicalName()
                    + " occurred while ehcache initialization", ex);
        }
        
        logger.info("Application initialized.");
          
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DBConnectionPool.close();
        CacheUtil.closeCache();
        logger.info("Application destroyed.");
    }

    private void loadConfiguration(String configFilePath) {
        Properties properties = new Properties();
        //String configFilePath = configFolderPath + CONFIG_PROPERTIES_FILE;
        try (InputStreamReader isr = new InputStreamReader(
                new FileInputStream(configFilePath), Charset.forName(Config.getEncoding()))) {
            properties.load(isr);
            Config.loadConfig(properties);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getClass().getCanonicalName()
                    + " occurred while reading configuration file in path: "
                    + configFilePath, e);
        }
    }
}
