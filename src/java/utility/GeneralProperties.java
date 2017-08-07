/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Properties;
import java.util.Set;
import label.CoreLabel;

public class GeneralProperties {

    private Properties prop = new Properties();
    private OutputStream output = null;
    private InputStream input = null;
    private String configFile = "";
    private String configFileName = "";
    private String workingDirectoryManual = "";

    public GeneralProperties() throws UnsupportedEncodingException {
        this.configFile = this.getWorkingDir();

    }

    public GeneralProperties(String configFileName) {
        this.configFileName = configFileName;
    }

    public GeneralProperties(String configFileName, String workingDirectory) {
        this.configFileName = configFileName;
        this.workingDirectoryManual = workingDirectory;
    }

    public String getWorkingDirectory() {
        return workingDirectoryManual;
    }

    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectoryManual = workingDirectory;
    }

    public String getConfigFileName() {
        return configFileName;
    }

    public void setConfigFileName(String configFileName) {
        this.configFileName = configFileName;
    }

    public String getCorePropertyValue(String key) {
        CoreProperties cp = new CoreProperties();
        return cp.getProperty(key);
    }

    private String getResourcePath() {
        return getCorePropertyValue(CoreLabel.RESOURCE_PATH);
    }

    public String getWorkingDir() throws UnsupportedEncodingException {

        String workingDir = "";
        if (getWorkingDirectory().trim().equals("")) {
//            workingDir = System.getProperty("user.dir");
            workingDir = this.coreFullPath();
            workingDir += getResourcePath();
        } else {
            workingDir = this.getWorkingDirectory();
        }

        return workingDir;
    }
    
    public String coreFullPath() throws UnsupportedEncodingException{
        CallDispatcher cd = new CallDispatcher();
        String path = this.getClass().getClassLoader().getResource("").getPath();
        String fullPath = URLDecoder.decode(path, "UTF-8");
        String pathArr[] = fullPath.split("/build/web/WEB-INF/classes/");
        fullPath = pathArr[0];
        return fullPath;
    }

    private String getConfigFile() throws UnsupportedEncodingException {
        return this.getWorkingDir() + this.configFileName;
    }

    public void store() throws IOException {
        // save properties to project root folder
        prop.store(this.output, null);
        if (output != null) {
            try {
                output.close();
            } catch (IOException e) {
            }
        }
    }

    public void setProperty(String key, String value) throws IOException {
        // set the properties value
        input = new FileInputStream(this.getConfigFile());
        prop.load(input);
        output = new FileOutputStream(this.getConfigFile());

        Set<Object> keys = prop.keySet();
        keys.stream().map((k) -> (String) k).forEach((key1) -> {
            this.prop.setProperty(key1, prop.getProperty(key1));
        });
        this.prop.setProperty(key, value);
        this.store();
        input.close();
        
    }

    public String getProperty(String key) {
        try {
            String filename = this.getConfigFile();
//            System.out.println("General properties File adlari--->"+filename);
            input = new FileInputStream(filename);
            prop.load(input);
            input.close();
        } catch (FileNotFoundException ex) {
//            throw new Exception(ex);
        } catch (IOException ex) {
        }
        return prop.getProperty(key);
    }

    public static void main(String[] args) throws IOException {
//        GeneralProperties cp = new GeneralProperties();
//        for (int i = 0; i < 6; i++) {
//            cp.setProperty("ad" + i, "ad" + i);
//        }
        Properties prop = new Properties();
        OutputStream output = null;
        InputStream input = null;

        try {
//            String workingDir = System.getProperty("user.dir");
            String workingDir = "";
            workingDir += "\\src\\java\\resources\\config\\config.properties";

            input = new FileInputStream(workingDir);
            prop.load(input);
            output = new FileOutputStream(workingDir);

            Set<Object> keys = prop.keySet();
            keys.stream().map((k) -> (String) k).forEach((key) -> {
                // System.out.println(prop.getProperty(key));
                prop.setProperty(key, prop.getProperty(key));
            });
            // set the properties value
            prop.setProperty("database", "localhost");
            prop.setProperty("dbuser", "mkyong");
            prop.setProperty("dbpassword", "password");

//            prop.put("database1", "localhost");
//            prop.put("dbuser1", "mkyong");
//            prop.put("dbpassword1", "password");
            // save properties to project root folder
            prop.store(output, "asdfasdfasdf asdf asdf asdf asdf");

        } catch (IOException io) {
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                }
            }
        }
    }

}
