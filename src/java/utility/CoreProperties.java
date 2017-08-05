/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class CoreProperties {

    private final String filename = "core.properties";
    Properties prop;

    public CoreProperties() {
        prop = new Properties();
    }

    public String getProperty(String key) {
        try {
            String res = "";
            String fname = coreFullPath() + "/" + filename;
            try (InputStream input = new FileInputStream(fname)) {
                prop.load(input);
                res = prop.getProperty(key);
            } catch (IOException ex) {
                Logger.getLogger(CoreProperties.class.getName()).log(Level.SEVERE, null, ex);
            }
            return res;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CoreProperties.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    private String coreFullPath() throws UnsupportedEncodingException {
//        String path = this.getClass().getClassLoader().getResource("../../").getPath();
        String path = this.getClass().getClassLoader().getResource("").getPath();
        String fullPath = URLDecoder.decode(path, "UTF-8");
        String pathArr[] = fullPath.split("/build/web/WEB-INF/classes/");
        fullPath = pathArr[0];
//         System.out.println("key point->"+fullPath);
        return fullPath;
//        return path;
    }

}
