/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smssender;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author nikli
 */
public class Config {
    
    public static String WS_URL;
    public static String USERNAME;
    public static String PASSWORD;
    
    public static void loadConfig() {
        Properties prop = new Properties();
	InputStream input = null;

	try {

		input = new FileInputStream("smssender/config.properties");

		// load a properties file
		prop.load(input);

		WS_URL = prop.getProperty("ws.url");
                USERNAME = prop.getProperty("username");
                PASSWORD = prop.getProperty("password");

	} catch (IOException ex) {
		ex.printStackTrace();
	} finally {
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
    }
    
}
