/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.io.UnsupportedEncodingException;
import utility.GeneralProperties;
/**
 *
 * @author user
 */
public class TGConfigurationProperties extends GeneralProperties{
    private String configFileName = "tgconfig.properties";
    
    public TGConfigurationProperties() throws UnsupportedEncodingException{
       super();
       super.setConfigFileName(configFileName);
    }
    
}
