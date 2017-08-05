/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import label.CoreLabel;
import utility.sqlgenerator.QLogger;

/**
 *
 * @author nikli
 */
public class SMSSender {

    

    private static String getInputText(String number[],String message,String from) throws QException{
        try {
            SMSConfigurationProperties prop = new SMSConfigurationProperties();
            
            String url1 = prop.getProperty(CoreLabel.SMS_SENDER_URL);
            String username = prop.getProperty(CoreLabel.SMS_SENDER_USERNAME);
            String password = prop.getProperty(CoreLabel.SMS_SENDER_PASSWORD);
//            System.out.println(url1+" --- "+username+" ---- "+password);
            String res = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
            res += "<SMS-InsRequest>";
            res += "<CLIENT user=\""+username+"\" pwd=\""+password+"\" from=\""+from+"\"/>";
            res += "<INSERTMSG text=\""+message+"\">";
            for (String number1 : number) {
                res += "<TO>" + number1 + "</TO>";
            }
            res += "</INSERTMSG>";
            res += "</SMS-InsRequest>";
//            System.out.println("content sms->"+res);
            return res;
        } catch (Exception ex) {
            throw new QException(new Object() {}.getClass().getEnclosingClass().getName(),
                    new Object(){}.getClass().getEnclosingMethod().getName(), ex);
        }
    }
    
    
    public static void send(String number[],String message) throws QException {
        try {
            SMSConfigurationProperties prop = new SMSConfigurationProperties();
            String sender = prop.getProperty(CoreLabel.SMS_SENDER_NAME_FAB);
//            System.out.println("sender - >"+sender);
            send(number,message,sender);
        } catch (UnsupportedEncodingException | QException ex) {
            throw new QException(new Object() {}.getClass().getEnclosingClass().getName(),
                    new Object(){}.getClass().getEnclosingMethod().getName(), ex);
        }
     }
    
     public static void send(String number,String message) throws QException {
        try {
            SMSConfigurationProperties prop = new SMSConfigurationProperties();
            String sender = prop.getProperty(CoreLabel.SMS_SENDER_NAME_FAB);
            send(number,message,sender);
        } catch (UnsupportedEncodingException ex) {
                        throw new QException(new Object() {}.getClass().getEnclosingClass().getName(),
                    new Object(){}.getClass().getEnclosingMethod().getName(), ex);
        }
     }
     
     public static void send(String number,String message,String from) throws QException {
         send(new String[]{number},message,from);
     }
     
    public static void send(String number[],String message,String from) throws QException {
        try  {
            SMSConfigurationProperties prop = new SMSConfigurationProperties();
            String url1 = prop.getProperty(CoreLabel.SMS_SENDER_URL);
//             System.out.println("url1 - >"+url1);
            HttpURLConnection conn = null;
            String output = "";
            
                
                URL url = new URL(url1);
                conn  = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/xml");
                
                String input = getInputText(number, message, from);
//                System.out.println("input xml"+input);
                OutputStream os = conn.getOutputStream();
                os.write(input.getBytes());
                os.flush();
//                System.out.println("respcode=" + conn.getResponseCode());
                
                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + conn.getResponseCode());
                }
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));
                
                
                System.out.println("Output from Server .... \n");
                String rline = "";
                while ((output = br.readLine()) != null) {
                    rline = rline + output;
                }
            
            QLogger.saveSMSLog(number, input, rline);
            
        } catch (Exception ex) {
            QLogger.saveSMSLog(number, message, ex.getMessage());
            throw new QException(new Object() {}.getClass().getEnclosingClass().getName(),
                    new Object(){}.getClass().getEnclosingMethod().getName(), ex);
        }
    }
    

    
         
            
         

   

}
