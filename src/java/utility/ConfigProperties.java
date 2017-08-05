/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.io.UnsupportedEncodingException;
/**
 *S
 * @author user
 */
public class ConfigProperties extends GeneralProperties{
    private final String configFileName = "config.properties";
    
    public ConfigProperties() throws UnsupportedEncodingException{
       super();
       super.setConfigFileName(configFileName);
    }
    
}
