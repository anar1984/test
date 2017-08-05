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
public class WebServiceCallConfigurationProperties extends GeneralProperties{
    private final String configFileName = "webservicecallconfig.properties";
    
    public WebServiceCallConfigurationProperties() throws UnsupportedEncodingException{
       super();
       super.setConfigFileName(configFileName);
    }
    
}
