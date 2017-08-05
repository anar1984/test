package utility.sqlgenerator;

import java.sql.*;
import java.io.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import label.CoreLabel;
import org.apache.commons.dbcp.BasicDataSource;
import utility.*;
//import org.apache.commons.dbcp.BasicDataSource;

public class DBConnection {

    private Connection conn = null;
//    private Statement stmt = null;
    private ResultSet rs = null;
    DBConfigurationProperties prop = null;

    private String userName = "";
    private String password = "";
    private String url = "";
    private String driver = "";
    private String initialSize = "";
    private String maxIdle = "";

    private String dbname = "";
    private String shemaname = "";

    private String INITIAL_SIZE = "initialSize";
    private String MAX_IDLE = "maxIdle";

    public DBConnection() throws QException {

        try {
            prop = new DBConfigurationProperties();
        } catch (UnsupportedEncodingException ex) {

        }
        try {  
            setConnection(CoreLabel.DB_PRIMARY);
            int size = Integer.valueOf(this.initialSize);
            int midSize = Integer.valueOf(this.maxIdle);
            conn = DBConnectionPool.getConnection(this.driver, this.userName,
                    this.password, this.url, size, midSize);
        } catch (Exception e) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(),
                    e);
        }
//        Class.forName(this.driver);
//        conn = DriverManager.getConnection(this.url, this.userName, this.password);
//        stmt = conn.createStatement();
//        System.out.println("--db connection " + (System.currentTimeMillis()-int1));
    }

    public DBConnection(String dbTypeNumber) throws QException {
//                    long int1 = System.currentTimeMillis();

        try {
            prop = new DBConfigurationProperties();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            setConnection(dbTypeNumber);
            conn = DBConnectionPool.getConnection(this.driver, this.userName,
                    this.password, this.url,
                    Integer.valueOf(this.initialSize),
                    Integer.valueOf(this.maxIdle));
        } catch (QException e) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(),
                    e);
        }
//        Class.forName(this.driver);
//        conn = DriverManager.getConnection(this.url, this.userName, this.password);

//        stmt = conn.createStatement();
    }

    private void setConnection(String dbTypeNumber) throws QException {
        try {
            if (dbTypeNumber.equals(CoreLabel.DB_PRIMARY)) {
                setPrimaryConnection();
            }

        } catch (Exception e) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(),
                    e);
        }
    }

    private void setPrimaryConnection() throws QException {
        this.setUserName(this.prop.getProperty(CoreLabel.USERNAME_PRIMARY));
        this.setPassword(this.prop.getProperty(CoreLabel.PASSWORD_PRIMARY));
        this.setUrl(this.prop.getProperty(CoreLabel.URL_PRIMARY));
        this.setDriver(this.prop.getProperty(CoreLabel.DRIVER_PRIMARY));
        this.setInitialSize(this.prop.getProperty(INITIAL_SIZE));
        this.setMaxIdle(this.prop.getProperty(MAX_IDLE));
    }

    public String getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(String initialSize) {
        this.initialSize = initialSize;
    }

    public String getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(String maxIdle) {
        this.maxIdle = maxIdle;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public void setDatabasaName(String arg) {
        this.dbname = arg;
    }

    public String getDatabaseName() {
        return this.dbname;
    }

    public void setShemaName(String arg) {
        this.shemaname = arg;
    }

    public String getShemaName() {
        return this.shemaname;
    }

//    public Statement getStatement() {
//        return this.stmt;
//    }
    public Connection getConnection() {
        return this.conn;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static void connectionCreate() throws QException {
        try {

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/test?"
                    + "user=test_user&password=test1234");
            conn.setAutoCommit(true);
            Statement stmt = conn.createStatement();

            //    ResultSet rset = stmt.executeQuery("INSERT INTO STUDENT_INFO(SURNAME,BIRTH_DAY,MODIFICATION_DATE,INSERT_DATE,BIRTH_PLACE,NAME,ID) VALUES ('lomush','19770506','20120606','20120202','Turkey','Kenan','11111')");
            // stmt.execute("commit");
            //    System.out.println("user :"+user+", parol:"+parol);
            ResultSet rs = stmt.executeQuery("select * from us_users");
            //stmt.execute("commit");
            //Carrier qc= convertResultSetToCarrier(rs,"table");

            //for (int i=0;i<)
            //System.out.println(qc);
//            while (rs.next()) {
//                System.out.println("sezon: " + rs.getString(1).toString());
//                System.out.println("qCiymetler: " + rs.getString(2).toString());
//            }
            //stmt.close();
//            System.out.println("Ok.");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
//            throw new QException();
            System.err.println(e.getMessage());
        }
    }

    public static void execInsertSql(String arg) throws QException {
        try {
//            Class.forName("com.mysql.jdbc.Driver").newInstance();
//            conn = DriverManager.getConnection("jdbc:mysql://localhost/test?"
//                    + "user=test_user&password=test1234");
//            /* Class.forName("oracle.jdbc.driver.OracleDriver");
//             String url = "jdbc:oracle:thin:@//172.16.2.155:1521/tes2";
//             DBConnection conn =  DriverManager.getConnection(url,"arustamov","qafqaz");*/
//            conn.setAutoCommit(false);
//            Statement stmt = conn.createStatement();
//            SaveToFileLog.saveLogToFile(arg);
//            stmt.execute(arg);
//            stmt.execute("commit");
//            stmt.close();
//            System.out.println("Ok.");
        } catch (Exception e) {
            throw new QException(e);
        }

    }

    public static Carrier execSelectSql(String arg, String tableName) throws QException {
        Carrier qc1 = new Carrier();
        try {
//            Class.forName("com.mysql.jdbc.Driver").newInstance();
//            DBConnection conn = DriverManager.getConnection("jdbc:mysql://localhost/test?"
//                    + "user=test_user&password=test1234");
//            /* Class.forName("oracle.jdbc.driver.OracleDriver");
//             String url = "jdbc:oracle:thin:@//172.16.2.155:1521/tes2";
//             DBConnection conn =  DriverManager.getConnection(url,"arustamov","qafqaz");*/
//            conn.setAutoCommit(false);
//            Statement stmt = conn.createStatement();
//            SaveToFileLog.saveLogToFile(arg);
//            ResultSet rs = stmt.executeQuery(arg);
//            //stmt.execute("commit");
//            //	   
//            qc1 = convertResultSetToCarrier(rs, tableName);
//            stmt.execute("commit");
//            stmt.close();
        } catch (Exception e) {
            throw new QException(e);
        }
        return qc1;
    }

    private static String[] getColumnNames(ResultSet rs) throws QException {
        String[] cc;
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            String[] columnNames = new String[columnCount];
            // The column count starts from 1
            for (int i = 1; i < columnCount + 1; i++) {
                columnNames[i - 1] = rsmd.getColumnName(i);

                // Do stuff with name
            }
            cc = columnNames;
        } catch (Exception e) {
            throw new QException(e);
        }

        return cc;
    }

    private static String getTableName(ResultSet rs) throws QException {
        String cc = "";
        try {
            rs.next();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            String[] columnNames = new String[columnCount];
            // PGResultSetMetaData meta = (PGResultSetMetaData)rs.getMetaData(); 
            //String tableName = meta.getBaseTableName(1);

            // The column count starts from 1
            // for (int i = 1; i < columnCount + 1; i++ ) {
            cc = rsmd.getTableName(1) + rsmd.getTableName(2);
            // Do stuff with name
            //}
            //cc=columnNames;
        } catch (Exception e) {
            throw new QException(e);
        }

        return "table";
    }

    private static Carrier convertResultSetToCarrier(ResultSet rs, String tableName) throws QException {
        String[] colNames = getColumnNames(rs);
        //String 	tableName = getTableName(rs);
        Carrier qCarry1 = new Carrier();
        try {
            int row = 0;
            while (rs.next()) {
                for (int i = 0; i <= colNames.length - 1; i++) {
                    String vl = rs.getString(colNames[i]);
                    if (vl == null) {
                        vl = " ";
                    }
                    String fieldName = convertTableFieldNameToEntityfieldName(colNames[i]);

                    qCarry1.setValue(tableName, row, fieldName, vl);
                    //qCarry1.setValue(tableName, row, i, vl);
                }
                row++;
            }
        } catch (Exception e) {
            throw new QException(e);
        }
        return qCarry1;
    }

    private static Carrier convertResultSetToCarrierBoth(ResultSet rs, String tableName) throws QException {
        String[] colNames = getColumnNames(rs);
        //String 	tableName = getTableName(rs);
        Carrier qCarry1 = new Carrier();
        try {
            int row = 0;
            while (rs.next()) {
                for (int i = 0; i <= colNames.length - 1; i++) {
                    qCarry1.setValue(tableName, row, i, rs.getString(colNames[i]));
                    qCarry1.setValue(tableName, row, convertTableFieldNameToEntityfieldName(colNames[i]), rs.getString(colNames[i]));
                }
                row++;
            }
        } catch (Exception e) {
            throw new QException(e);
        }
        return qCarry1;
    }

    static String convertTableFieldNameToEntityfieldName(String arg) {
        String UNDERSCORE = "_";
        String st[] = arg.split(UNDERSCORE);
        String res = st[0].toLowerCase();
        for (int i = 1; i <= st.length - 1; i++) {
            res = res + st[i].substring(0, 1).toUpperCase() + st[i].substring(1, st[i].length()).toLowerCase();
        }
        return res;
    }

    public static void main(String[] arg) throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/apdvoice?"
                + "user=root&password=");

        conn.setAutoCommit(false);
        Statement stmt = conn.createStatement();
        stmt.execute("SELECT * FROM CR_LIST_ITEM");

        System.out.println("Ok for my sql");

        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUsername("root");
        ds.setPassword("");
        ds.setUrl("jdbc:mysql://localhost/apdvoice");
        ds.setMaxIdle(200);
        ds.setMinIdle(50);

        conn = ds.getConnection();
        stmt = conn.createStatement();
        stmt.execute("SELECT * FROM CR_LIST_ITEM");

        System.out.println("Ok for my sql 2");

        conn = new DBConnection().getConnection();
        stmt = conn.createStatement();
        stmt.execute("SELECT * FROM CR_LIST_ITEM");

        System.out.println("Ok for   mssql");
    }
    
    
    public static void closeConnection(Connection conn) {
        if(conn!=null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                QLogger.saveExceptions("closeConnection", "closeConnection", ex.getMessage());
                
            }
        }
    }
    
    public static void rollbackConnection(Connection conn) {
        if(conn!=null) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                QLogger.saveExceptions("rollbackConnection", "rollbackConnection", ex.getMessage());
                
            }
        }
    }
}
