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
public class ListenerConfigurationProperties extends GeneralProperties{
    private final String configFileName = "listenerconfig.properties";
    
    public ListenerConfigurationProperties() throws UnsupportedEncodingException{
       super();
       super.setConfigFileName(configFileName);
    }
    
}
