package controllerpool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Message_az {
    public void OneLineOfMessages(){
        try {
            // C:\Users\Rovshan\Desktop\\Message_az.txt
            // C:\\Users\\student\\Desktop\\Message_az.txt
            /*BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\Users\\student\\Desktop\\\\Message_az.txt"));
            bw.write("100::@param1 parametrinde @param2 Integer olmalidir");                                          bw.newLine();
            bw.write("101::@param1 parametrinde @param2 String olmalidir");                                           bw.newLine();
            bw.write("102::@param1 parametrinde @param2 kicik beraber olmalidir @param3");                            bw.newLine();
            bw.write("103::@param1 parametrinde @param2 boyuk beraber olmalidir @param3");                            bw.newLine();
            bw.write("104::@param1 parametrinde @param2 @param3 ve @param4 arasinda olmalidir");                      bw.newLine();
            bw.write("105::@param1 parametrinde @param2 sozunun uzunlugu kicik beraber olmalidir @param3");           bw.newLine();
            bw.write("106::@param1 parametrinde @param2 sozunun uzunlugu boyuk beraber olmalidir @param3");           bw.newLine();
            bw.write("107::@param1 parametrinde @param2 sozunun uzunlugu beraber olmalidir @param3");                 bw.newLine();
            bw.write("108::@param1 parametrinde @param2 sozunun uzunlugu @param3 ve @param4 arasinda olmalidir");     bw.newLine();
            bw.close();*/
            String s = "";
            BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Rovshan\\Desktop\\Message_az.txt"));
//            while((s = br.readLine()) != null){
////                System.out.println(s);
//            }
            
            br.close();
        } catch (FileNotFoundException e) {
            
        } catch (IOException e) {
            
        }
    }
    
    public static void main (String [] args){
        Message_az aa = new Message_az();
        aa.OneLineOfMessages();
    }
}
