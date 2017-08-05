/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utility.sqlgenerator;

import com.mchange.v2.c3p0.DataSources;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import utility.QException;

public class DBConnectionPool {

    private static final BasicDataSource ds = new BasicDataSource();

    public static Connection getConnection() throws QException {
        try {
            if (ds == null) {
                setupDataSource();
            }
            return ds.getConnection();
        } catch (Exception e) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(),
                    e);
        }
    }

    public static Connection getConnection(String drivername, String username,
            String pwd, String url, int initialSize, int maxIdle) throws QException {
//        System.out.println("ok");
        try {
            String s = "";
            setupDataSource(drivername, username, pwd, url, initialSize, maxIdle);
            return ds.getConnection();
        } catch (Exception e) {
            System.out.println("DB Connection problem");
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(),
                    e);
        }
    }

    public static DataSource setupDataSource() {
//		ds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
//		ds.setUsername("anar");
//		ds.setPassword("anar");
//		ds.setUrl("jdbc:oracle:thin:@172.16.2.68:1521:orcl");
//		ds.setInitialSize(10);
//		ds.setMaxIdle(100);
        return ds;
    }

    public static DataSource setupDataSource(String drivername, 
            String username, String pwd, String url, int initialSize, 
            int maxIdle) {
        ds.setDriverClassName(drivername);
        ds.setUsername(username);
        ds.setPassword(pwd);
        ds.setUrl(url);
        ds.setInitialSize(initialSize);
        ds.setMaxIdle(maxIdle);
       

        return ds;
    }
    
    public static void close() {
        try {
            ds.close();
            DataSources.destroy(ds);
        } catch (SQLException ex) {
            Logger.getLogger(DBConnectionPool.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
