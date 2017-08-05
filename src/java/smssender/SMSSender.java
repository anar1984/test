/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smssender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author nikli
 */
public class SMSSender {

    

    public String cancel(int messageId) {
        //{"Credential":{"Username":"String","Password":"String"},"MessageId":0}
       String input = "{\"Credential\":{\"Username\":\""+Config.USERNAME+"\",\"Password\":\""+Config.PASSWORD+"\"},\"MessageId\":"+messageId+"}"; 
       return processRequest("Cancel", "POST", input); 
    }
    
    public String getBalance() {
        return null;
    }
    
    public String getBalanceLog(String begin, String end) {
        return null;
    }
    
    public String getSettings() {
        return null;
    }
    
    public String login() {
        return null;
    }
    
    public String query() {
        return null;
    }
    
    public String QueryMulti() {
        return null;
    }
    
    
    public String QueryQueued() {
        return null;
    }
    
    public String QueryStats() {
        return null;
    }
    
    public String Receive() {
        return null;
    }
    
    public String Reschedule() {
        return null;
    }
    
    public String Submit() {
        return null;
    }
    
    public String SubmitData() {
        return null;
    }
    
    public String SubmitDataMulti() {
        return null;
    }    
    
     public String SubmitMulti() {
        return null;
    }            
            
         

    private String processRequest(String action, String requestMethod, String input) {
        HttpURLConnection conn = null;
        String output = "";
        try  {

            URL url = new URL(Config.WS_URL+action);
            conn  = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("Content-Type", "application/json");

            //String input = "{\"Credential\":{\"Username\":\"testsms\",\"Password\":\"testtest\"},\"Header\":{\"From\":\"VIP SMOKE\"},\"Message\":\"bomba oglan\",\"To\":[\"994705254039\"],\"DataCoding\":\"Default\"}";
                //String input = "Credential={Username:testsms,Password:testtest}&Header={From:testsms}&Message=test&To=[994705254039]&DataCoding=Default";

                //{"Credential":{"Username":"testsms","Password":"test"},"Header":{"From":"","ScheduledDeliveryTime":"","ValidityPeriod":0,"Route":0},"Message":"bomba oglan","To":["994705254039"],"DataCoding":"Default"}
            //,\"ScheduledDeliveryTime\":\"2016-08-27T16:49:00.0000000\",\"ValidityPeriod\":1440,\"Route\":0
            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();
//            System.out.println("respcode=" + conn.getResponseCode());

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
             throw new RuntimeException("Failed : HTTP error code : "
             + conn.getResponseCode());
             }
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            
//            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
//                System.out.println(output);
            }
            
            


        } catch (MalformedURLException e) {

            e.printStackTrace();
            output = e.getMessage();

        } catch (IOException e) {

            e.printStackTrace();
            output = e.getMessage();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return output;
    }

}
