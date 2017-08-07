package utility.sqlgenerator;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import label.LabelSqlQueryIds;
import utility.Carrier;
import utility.CoreEntity;
import utility.DBConfigurationProperties;
import rp.module.entity.EntityCrSqlPool;
import utility.QDate;
import utility.QException;

public class IdGenerator {

    private static final String ID_GENERATOR_TYPE = "idGeneratorType";
    private static final String SQL_TYPE_LOGICAL_ID = "lastId";
    
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String nextRandomSessionId() {
        return new BigInteger(130, RANDOM).toString(32);
    }
    
    public static String nextDbId() {
        return new BigInteger(32, RANDOM).toString(32);
    }

    public static String getId(CoreEntity entity, String databaseNumber) throws QException {
        String res = "";
        String idType = getIdGeneratorType(databaseNumber);
//        if (idType.equals("1")) {
//            res = getIdType1();
//        }
//        if (idType.equals("2")) {
//            res = getIdType2(entity, databaseNumber);
//        }
        res = getIdType1();
        return res;
    }

    public static String getId() throws QException {
        try {
            return getIdType1();
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    private static String getIdGeneratorType(String databaseNumber) {
        DBConfigurationProperties prop = null;
        try {
            prop = new DBConfigurationProperties();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(IdGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        String propKeyName = ID_GENERATOR_TYPE + capitalizeFirstLetter(databaseNumber);
        String keyName = prop.getProperty(propKeyName);
//                                            System.out.println("  getId  ----------- ok "+propKeyName+ " " +keyName);

        return keyName;
    }

    static String capitalizeFirstLetter(String arg) {
        arg = arg.substring(0, 1).toUpperCase() + arg.substring(1, arg.length()).toLowerCase();
        return arg;
    }

    public static String getIdType1() {
//        System.out.println("getIdType1-----ok");

//        Calendar now = Calendar.getInstance();
//        String year = String.valueOf(now.get(Calendar.YEAR));
//        String month = String.valueOf(now.get(Calendar.MONTH));
//        String day = String.valueOf(now.get(Calendar.DAY_OF_MONTH));
//        String hr = String.valueOf(now.get(Calendar.HOUR));
//        String min = String.valueOf(now.get(Calendar.MINUTE));
//        String sec = String.valueOf(now.get(Calendar.SECOND));
//
//        if (month.length() <= 1) {
//            month = "0" + month;
//        }
//        if (day.length() <= 1) {
//            day = "0" + day;
//        }
//        if (hr.length() <= 1) {
//            hr = "0" + hr;
//        }
//        if (min.length() <= 1) {
//            min = "0" + min;
//        }
//        if (sec.length() <= 1) {
//            sec = "0" + sec;
//        }
//
//        int randomNum = 10000 + (int) (Math.random() * 99999);
//        String id = year + month + day + hr + min + sec + String.valueOf(randomNum);
        String id = QDate.getCurrentDate() + QDate.getCurrentTime() + QDate.getCurrentMillisecond();
        return id;
    }

//        public static void main(String[] arg){
//            System.out.println(getId());
//        }
    private static String getIdType2(CoreEntity entity, String databaseNumber) throws Exception {

        String res = "";
        EntityCrSqlPool sqlEnt = new EntityCrSqlPool();

        String tableName = entity.toDBTableName();
//        System.out.println(tableName);
        Carrier carrier1 = EntityManager.selectBySqlId(LabelSqlQueryIds.GET_LAST_ID, new String[]{tableName}, databaseNumber);
//        System.out.println(carrier1.toXML());
        String id = carrier1.getValue(sqlEnt.toTableName(), 0, "id").toString();
        int newId = Integer.valueOf(id) + 1;

        return String.valueOf(newId);
    }
    
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println( new BigInteger(32, RANDOM).toString(32));
        }
        
    }

}
