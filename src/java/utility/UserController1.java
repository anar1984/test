package utility;

import module.cr.entity.EntityCrUserController;
import module.cr.entity.EntityCrUserControllerList;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utility.sqlgenerator.EntityManager;

/**
 *
 * @author candide
 */
public class UserController1 {

//    private String fullText;
    private File contentFile = null;

    public UserController1() throws IOException {
        GeneralProperties prop = new GeneralProperties();
        String file = prop.getWorkingDir() + "../page/page_index.html";
        this.contentFile = new File(file);
    }

    public String filterText(String userName) throws QException {
        Document doc=null;
        try {
            doc = Jsoup.parse(this.contentFile, "UTF-8");
        } catch (IOException ex) {
        }

        HashMap<String, String> permissions = null;
        try {
            permissions = this.getUserPermissions(userName);
        } catch (SQLException ex) {

        } catch (Exception ex) {

        }

        Element el = doc.getElementById("lang_id");
        el.html(SessionManager.getCurrentLang());

        try {
            QUtility.checkLangLabel(doc);
        } catch (IOException ex) {
        }
//        Elements elements = doc.getElementsByAttribute("component_id");
//        for (Element element : elements) {
//            String componentid = element.attr("component_id");
////            System.out.println("component_id->"+componentid);
//            //eger component id usercontroler-de yoxdursa o zaman silinecekdir. eger varsa o zaman permission type nezere alinacaqdir.
//            if (!permissions.containsKey(componentid)) {
//                element.remove();
//            } else {
//                String type = permissions.get(componentid);
//                if (type.equals("n")) {
//                    doc.getElementsByAttributeValue("component_id", componentid).stream().forEach((matchingComponent) -> {
//                        matchingComponent.remove();
//                    });
//                } else if (type.equals("d")) {
//                    doc.getElementsByAttributeValue("component_id", componentid).stream().forEach((matchingComponent) -> {
//                        matchingComponent.attr("disabled", "");
//                    });
//                }
//            }
//        }

        return doc.getElementsByTag("body").get(0).toString();
    }

    private HashMap<String, String> getUserPermissions(String username) throws SQLException, Exception {
        HashMap<String, String> permissions = new HashMap<>();
        EntityCrUserControllerList ent = new EntityCrUserControllerList();
        ent.setComponentType("MENULIST%IN%FORMCOMPONENT%IN%SRVFORMCOMPONENT");
        ent.setUsername(username);
        Carrier carrier = EntityManager.select(ent);
        int cnt = carrier.getTableRowCount(ent.toTableName());
        for (int i = 0; i < cnt; i++) {
            String compId = carrier.getValue(ent.toTableName(), i, EntityCrUserController.FK_COMPONENT_ID).toString();
            String perType = carrier.getValue(ent.toTableName(), i, EntityCrUserController.PERMISSION_TYPE).toString();
//            System.out.println("key->" + compId + " ;value->" + perType);
            permissions.put(compId, perType);
        }

        return permissions;
    }

    private String getPath() {
        String path = this.getClass().getClassLoader().getResource("").getPath();
        String fullPath = "";
        try {
            fullPath = URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(UserController1.class.getName()).log(Level.SEVERE, null, ex);
        }
        String pathArr[] = fullPath.split("/WEB-INF/classes/");
        fullPath = pathArr[0];

        String logDirPath = new File(fullPath).getPath() + File.separatorChar;

        return logDirPath;
    }

}
