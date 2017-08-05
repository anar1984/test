package controllerpool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Messages {

    Message[] message = new Message[500];
    public int index = 0;
    
    public void add(Message msg) throws IOException {
        message[index] = msg;
        index++;
    }

    public boolean hasErrors() {
        boolean f = false;
        for (int i = 0; i < this.index; i++) {   
            if (this.message[i].hasError()) {
                f = true;
                break;
            }
        }
        return f;
    }

    public String getErrorText() throws IOException {
        String text = "";
        for (int i = 0; i < this.index; i++) {
            if (this.message[i].hasError()) {
                text += this.message[i].getMessageText() + "\n";
            }
        }
        return text;
    }

    /*public void Add(String code, String key, String value){
     //System.out.println(code + " " + key + " " + value);
     int cur_index = 0;
     boolean flag = false;
     //System.out.println("len " + index);
     //System.out.println("fora basla " + index);
     for(int i=0; i<index; i++){
     //System.out.println("len " + index);
     //System.out.println(i + "-->" + code + "-->" + message[i].getCode());
     //System.out.println(i);
     if(code.equals(message[i].getCode())){
     flag = true;
     cur_index = i;
     break;
     }
     }
     //System.out.println("for bittdi");
        
     if(flag == true){
     message[cur_index].setParam(key, value);
     }
     else{
     //System.out.println("code "+ code);
     //System.out.println(index);
     message[index] = new Message();
     message[index].setCode(code);
     message[index].setParam(key, value);
     index++;
     }
     }
    
     public String get(String code, String key){
     int cur_index = 0;
     boolean flag = false;
     for(int i=0; i<index; i++){
     if(code.equals(message[i].getCode())){
     //System.out.println("tapdim "+ i);
     flag = true;
     cur_index = i;
     break;
     }
     }
        
     if(flag == true){
     if(message[cur_index].getParam(key)== null){
     return "This key is not exist in this code";
     }
     else{
     return message[cur_index].getParam(key);
     }
     }
     else{
     return "This code is not exist";
     }
   
     }
    
     public void numberOfMesssages(){
     System.out.println(index);
     }*/
}
