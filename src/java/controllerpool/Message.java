package controllerpool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Message {

    private String code;
    private Map<String, String> param = new HashMap<>();

    public Message() {
    }

    public boolean hasError() {
        if (this.code.equals("200")) {
            return false;
        }
        return true;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setParam(String key, String value) {
        this.param.put(key, value);
    }

    public String getParam(String key) {
        return this.param.get(key);
    }

    public String getMessageText() throws IOException  {
        String s = "",  ans = "" , soz = "";
        // C:\Users\Rovshan\Desktop\\Message_az.txt
        // C:\\Users\\student\\Desktop\\Message_az.txt
        //System.out.println(this.code);
        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Rovshan\\Desktop\\\\Message_az.txt"));
        Boolean f = false;
        while ((s = br.readLine()) != null) {
            int cnt = 0;
            String temp = null;
            for (String split: s.split("::", 2)){
                cnt++;
                if(cnt == 1){
                    temp = split;
                }
                else if(cnt == 2){
                   soz = split;
                }
            }
                
            if (this.code.equals(temp)) {
                f = true;
                StringTokenizer s1 = new StringTokenizer(soz);
                cnt = 0;
                while(s1.hasMoreTokens()){
                    String cur_string = s1.nextToken();
                    String cur = "";
                    if(cur_string.charAt(0) == '@'){
                        cnt = cur_string.charAt(6) - 48;
                        cur = "param" + cnt;
                        ans += this.getParam(cur) + " ";
                    }
                    else{
                        ans += cur_string + " ";
                    }
                }
                break;
            }
        }
        
        if(!f){
            ans = this.code + " deye bir kod movcud deyil";
            //System.out.println("Bele bir kod movcud deyil");
        }
        return ans;
    }
}
