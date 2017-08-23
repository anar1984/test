package utility.sqlgenerator;

import java.util.ArrayList;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utility.SessionManager;

public class QLogger {

    private static Logger sqlLogger = LogManager.getLogger("sqlLogger");
    private static Logger logger = LogManager.getLogger();

    public static void saveExceptions(String classname, String methodname, String log) {
        String username = SessionManager.getCurrentUsername();//SessionManager.getUserByThreadId(Thread.currentThread().getId());
        Date dt = new Date();
//        logger.error("-----------------------------------------------------------------------------------------");
//        logger.error("##");
//        logger.error(" -->" + dt + "::" + username + "::");
//        logger.error(" " + classname + ":: " + methodname + "::");
//        logger.error(log);
//        logger.error("-----------------------------------------------------------------------------------------");
    }

    public static void saveSMSLog(String[] numbers, String message, String output) {
        String username = SessionManager.getCurrentUsername();//SessionManager.getUserByThreadId(Thread.currentThread().getId());
        Date dt = new Date();
        String numbersLine = "";

        for (String number : numbers) {
            numbersLine = numbersLine + number + ",";
        }

//        logger.info("-----------------------------------------------------------------------------------------");
//        logger.info("##");
//        logger.info(" --&gt" + dt + "::" + username + "::");
//        logger.info(" numbers->" + numbersLine);
//        logger.info("::");
//        logger.info(" message->" + message + "::");
//        logger.info(" output-->" + output + ";");
//        logger.info("-----------------------------------------------------------------------------------------");
    }

    public static void saveLogToFile(String log) {
        saveLogToFile(log, new ArrayList());
    }

    public static void saveLogToFile(String log, ArrayList values) {
        String line = "";
        for (int idx = 1; idx <= values.size(); idx++) {
            line = line + idx + "->" + values.get(idx - 1) + ";";
        }
        saveLogToFile(log, line);
    }

    public static void saveLogToFile(String log, String values) {
        String username = SessionManager.getCurrentUsername();
        Date dt = new Date();
//        sqlLogger.debug("##");
//        sqlLogger.debug(" -->" + dt + "::" + username + "::");
//        sqlLogger.debug(" " + log + "::");
//        sqlLogger.debug(" " + values);
    }

}
