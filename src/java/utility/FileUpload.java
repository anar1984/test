package utility;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import label.CoreLabel;
import org.apache.commons.codec.binary.Base64;
import resources.config.Config;
import utility.sqlgenerator.IdGenerator;

/**
 *
 * @author candide
 */
public class FileUpload {

    public synchronized String uploadImage(String base64String, String ext) throws QException {
//        System.out.println("1");
        String userId = "111111";
        try {
            userId = SessionManager.getCurrentUserId();
        } catch (Exception e) {

        }
//System.out.println("2");
        String id = IdGenerator.getId();
//        System.out.println("id = "+id);
//        System.out.println("userId = "+userId);
//        System.out.println("2 `1");
        long num = Long.parseLong(id) + Long.parseLong(userId) + 214521;
//        System.out.println("2 2");
        String hexId = QUtility.convertDecimalToHex(num);
        System.out.println("2 3");
        String fileName = "file_" + hexId + "." + ext.replaceAll("^\"|\"$", "");
//System.out.println("3");
//        System.out.println("ext -> " + ext);
        FileOutputStream imageOutFile;
//        System.out.println("4");
        try {
            // Decode String using Base64 Class
            byte[] imageByteArray = Base64.decodeBase64(base64String);
//System.out.println("5");
            // Write Image into File system - Make sure you update the path
            String fname = this.getUploadPath() + fileName;
//            System.out.println("file uploaded to ->" + fname);
            imageOutFile = new FileOutputStream(fname);
            imageOutFile.write(imageByteArray);
//System.out.println("return file");
            imageOutFile.close();
            return fileName;
        } catch (FileNotFoundException ex) {
//            System.out.println("6");
            Logger.getLogger(FileUpload.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileUpload.class.getName()).log(Level.SEVERE, null, ex);
//            System.out.println("7");
        }
//System.out.println("8");
        return QDate.getCurrentDate() + QDate.getCurrentTime() + "temp_file.png";
    }

    public String getUploadPath() {
        String file = "";
        try {
            GeneralProperties prop = new GeneralProperties();
            file = prop.coreFullPath() + Config.getUploadPath();
//            System.out.println("file uploaded------->" + file);

            return file;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(FileUpload.class.getName()).log(Level.SEVERE, null, ex);
        }
        return file;
    }

    public String getCurrentDateTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        String currentDT = sdf.format(cal.getTime());
        currentDT += QDate.getCurrentMillisecond();
        currentDT = currentDT.replaceAll("/", "");
        currentDT = currentDT.replaceAll(" ", "");
        currentDT = currentDT.replaceAll(":", "");

        return currentDT;
    }
}
