package utility.sqlgenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import label.CoreLabel;
import utility.ConfigProperties;
import utility.GeneralProperties;
import utility.LogConfigurationProperties;
import utility.SessionManager;

public class QLogger {

    public static void main(String arg[]) {
    }

    public static void saveLogToFile(String log) {
        saveLogToFile(log, new ArrayList());
    }

    public static void saveExceptions(String classname, String methodname, String log) {
        // Create file 
        try {
            LogConfigurationProperties prop = new LogConfigurationProperties();
            //String file = prop.coreFullPath() + prop.getProperty(CoreLabel.LOG_EXCEPTION_PATH);
            String file = prop.coreFullPath() + "/" + prop.getProperty(CoreLabel.LOG_EXCEPTION_PATH);
//            System.out.println("exception log folder path---->"+file);
            file += "Exception" + ".txt";
            try (FileWriter fstream = new FileWriter(file, true);) {
                String stLog = "";
                String username = SessionManager.getCurrentUsername();//SessionManager.getUserByThreadId(Thread.currentThread().getId());
                Date dt = new Date();
                stLog = "----------------------------------------------------------------------------------------- \n";
                stLog = stLog + "## \n -->" + dt + "::" + username + "::  \n " + classname + ":: " + methodname + ":: \n" + log + ";\n";
                stLog = stLog + "-----------------------------------------------------------------------------------------";

                // Create file 
                //LogConfigurationProperties prop = new LogConfigurationProperties();
                //String file = prop.coreFullPath() + prop.getProperty(CoreLabel.LOG_EXCEPTION_PATH);
                //String file = prop.coreFullPath()+"/"+prop.getProperty(CoreLabel.LOG_EXCEPTION_PATH);
//            System.out.println("exception log folder path---->"+file);
                //file +="Exception" + ".txt";
//            System.out.println("exception log--->"+file);
                //FileWriter fstream = new FileWriter(file, true);
                try (BufferedWriter out = new BufferedWriter(fstream)) {
                    out.append(stLog);
                    out.newLine();
                    //Close the output stream
                }
            }
            //fstream.close();
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void saveSMSLog(String[] numbers, String message, String output) {
        try {
            LogConfigurationProperties prop = new LogConfigurationProperties();
            String file = prop.coreFullPath() + prop.getProperty(CoreLabel.LOG_SMS_PATH);
            file += "SMSLog" + ".txt";
            try (FileWriter fstream = new FileWriter(file, true)) {
                String stLog = "";
                String username = SessionManager.getCurrentUsername();//SessionManager.getUserByThreadId(Thread.currentThread().getId());
                Date dt = new Date();
                String numbersLine = "";

                for (String number : numbers) {
                    numbersLine = numbersLine + number + ",";
                }

                stLog = "----------------------------------------------------------------------------------------- \n";
                stLog = stLog + "## \n -->" + dt + "::" + username + "::  \n numbers->" + numbersLine
                        + "::  \n message->" + message + ":: \n output->" + output + ";\n";
                stLog = stLog + "-----------------------------------------------------------------------------------------";

                // Create file 
                //LogConfigurationProperties prop = new LogConfigurationProperties();
                //String file = prop.coreFullPath() + prop.getProperty(CoreLabel.LOG_SMS_PATH);
                //file += "SMSLog" + ".txt";
//            System.out.println("sms log file-->"+file);
                //FileWriter fstream = new FileWriter(file, true);
                try (BufferedWriter out = new BufferedWriter(fstream)) {
                    out.append(stLog);
                    out.newLine();
                    //Close the output stream
                }
            }
            //fstream.close();
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void saveLogToFile(String log, ArrayList values) {
        String line = "";
        for (int idx = 1; idx <= values.size(); idx++) {
            line = line + idx + "->" + values.get(idx - 1) + ";";
        }
        saveLogToFile(log, line);
    }

    public static void saveLogToFile(String log, String values) {
        try {
            LogConfigurationProperties prop = new LogConfigurationProperties();
            String file = prop.coreFullPath() + prop.getProperty(CoreLabel.LOG_PATH);
            file += "SQLLog" + ".txt";
            File file1 = new File(file);
            try (FileWriter fstream = new FileWriter(file1, true)) {
                String stLog;
                String username = SessionManager.getCurrentUsername();//SessionManager.getUserByThreadId(Thread.currentThread().getId());
                Date dt = new Date();
                stLog = "## \n -->" + dt + "::" + username + "::  \n " + log + "::  \n " + values;
                // Create file 

//            System.out.println("SQL Log-->"+file1.getPath());
                //FileWriter fstream = new FileWriter(file1, true);
                try (BufferedWriter out = new BufferedWriter(fstream)) {
                    out.append(stLog);
                    out.newLine();
                }
            }
            //fstream.close();
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }

//    private static boolean isFileSizeExceded(String fileName) {
//        File file = new File(fileName);
//        double bytes = file.length();
//        double kilobytes = (bytes / 1024);
//        double megabytes = (kilobytes / 1024);
//        double gigabytes = (megabytes / 1024);
//
//        return megabytes >=5;
//    }
}
