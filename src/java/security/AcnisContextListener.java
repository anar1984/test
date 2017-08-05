/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utility.sqlgenerator.DBConnectionPool;

/**
 *
 * @author Azerbaycan
 */
@WebListener
public class AcnisContextListener implements ServletContextListener {

    private static final Logger LOG = LoggerFactory
            .getLogger(AcnisContextListener.class);

    private Driver driver = null;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
       
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DBConnectionPool.close();
        if (this.driver != null) {
            try {
                DriverManager.deregisterDriver(driver);
                LOG.info(String.format("deregistering jdbc driver: %s", driver));
            } catch (SQLException e) {
                LOG.warn(
                        String.format("Error deregistering driver %s", driver),
                        e);
            }
            this.driver = null;
        } else {
            LOG.warn("No driver to deregister");
        }

    }

}
