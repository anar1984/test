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
import utility.sqlgenerator.IdGenerator;

/**
 *
 * @author candide
 */
public class FileUpload {

    public synchronized  String uploadImage(String base64String, String ext) throws QException {
        String userId= SessionManager.getCurrentUserId();
        String id = IdGenerator.getId();
        long num = Long.parseLong(id)+Long.parseLong(userId)+214521;
        String hexId = QUtility.convertDecimalToHex(num);
        String fileName = "file_" + hexId + "." + ext.replaceAll("^\"|\"$", "");

//        System.out.println("ext -> " + ext);
        //FileOutputStream imageOutFile;
        String fname = this.getUploadPath() + fileName;
        try(FileOutputStream imageOutFile = new FileOutputStream(fname)) {
            // Decode String using Base64 Class
            byte[] imageByteArray = Base64.decodeBase64(base64String);

            // Write Image into File system - Make sure you update the path
            //String fname = this.getUploadPath() + fileName;
            System.out.println("file uploaded to ->"+fname);
            //imageOutFile = new FileOutputStream(fname);
            imageOutFile.write(imageByteArray);

            //imageOutFile.close();
            return fileName;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileUpload.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileUpload.class.getName()).log(Level.SEVERE, null, ex);
        }

        return QDate.getCurrentDate()+QDate.getCurrentTime()+"temp_file.png";
    }

    public String getUploadPath() {
        String file = "";
        try {
            UploadConfigurationProperties prop = new UploadConfigurationProperties();
            file = prop.coreFullPath() + prop.getProperty(CoreLabel.UPLOAD_PATH);
            System.out.println("file uploaded------->"+file);
            
          
            return file;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(FileUpload.class.getName()).log(Level.SEVERE, null, ex);
        }
        return file;
    }

    public  String getCurrentDateTime() {
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
