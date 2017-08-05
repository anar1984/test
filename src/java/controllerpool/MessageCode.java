package controllerpool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MessageCode {
    public void codes (){
        try {
            // C:\Users\Rovshan\Desktop\\Message_az.txt
            // C:\\Users\\student\\Desktop\\Message_az.txt
            /*BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\Users\\student\\Desktop\\\\Message_code.txt"));
            bw.write("100:isInteger");          bw.newLine();
            bw.write("101:isString");           bw.newLine();
            bw.write("102:isLessThan");         bw.newLine();
            bw.write("103:isLargerThan");       bw.newLine();
            bw.write("104:isBetween");          bw.newLine();
            bw.write("105:lengthLessThan");     bw.newLine();
            bw.write("106:lengthBiggerThan");   bw.newLine();
            bw.write("107:lengthEqualTo");      bw.newLine();
            bw.write("108:lengthBetween");      bw.newLine();
            bw.close();*/
            String s = "";
            BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Rovshan\\Desktop\\MessageCode.txt"));
//            while((s = br.readLine()) != null){
//                System.out.println(s);
//            }
            
            br.close();
        } catch (FileNotFoundException e) {
            
        } catch (IOException e) {
            
        }
    }
    
    public static void main(String [] args){
        MessageCode aa = new MessageCode();
        aa.codes();
    }
}

