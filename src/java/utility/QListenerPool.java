/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lenovo
 */
public class QListenerPool extends Thread {

    String TIME = "time";
    String SEPERATOR = ",";

    @Override
    public void run() {
//        System.out.println("QListener is running");
    }

    public void executeListener() throws QException {
        ListenerConfigurationProperties prop;
        try {
            prop = new ListenerConfigurationProperties();
            String timeLine = prop.getProperty(TIME);
            String[] times = timeLine.trim().split(SEPERATOR);
        } catch (UnsupportedEncodingException ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }

    }

    
    
    //time formati hhmm formasinda string tipindendir
    public void selectListenesrByTime(String time) {
        Carrier carrier = new Carrier();
        executeSeriveListener(carrier);
        executeTemplateListener(carrier);
    }

    public void executeSeriveListener(Carrier carrier) {
//        System.out.println("executeServiceListener");
    }

    public void executeTemplateListener(Carrier carrier) {
//        System.out.println("executeTemplateListener");
    }
}
