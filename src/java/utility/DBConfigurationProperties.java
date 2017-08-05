/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.io.UnsupportedEncodingException;
/**
 *
 * @author user
 */
public class DBConfigurationProperties extends GeneralProperties{
    private final String configFileName = "dbconfig.properties";
    
    public DBConfigurationProperties() throws UnsupportedEncodingException{
       super();
       super.setConfigFileName(configFileName);
     }
    
}
