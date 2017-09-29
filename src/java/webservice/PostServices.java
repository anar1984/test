/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice;

import auth.SessionHandler;
import controllerpool.CoreUserController;
import module.cr.entity.EntityCrUser;
import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.SQLException;
import utility.CallDispatcher;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.ws.rs.GET;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import label.CoreLabel;
import org.glassfish.jersey.CommonProperties;
import module.cr.CrModel;
import org.apache.commons.lang.ArrayUtils;
import org.jose4j.lang.JoseException;
import resources.config.Config;
import utility.Carrier;
import utility.FileUpload;
import utility.QDate;
import utility.QException;
import utility.SessionManager;
import utility.UserController;
import utility.sqlgenerator.DBConnection;
import utility.sqlgenerator.EntityManager;
import utility.sqlgenerator.QLogger;
//import smssender.Config;
//import smssender.SMSSender;

/**
 * REST Web Service
 *
 * @author nikli
 */
@Path("post")
public class PostServices {

    @Context
    private UriInfo context;
    private ExecutorService executorService = java.util.concurrent.Executors.newCachedThreadPool();
//    SMSSender smsSender = new SMSSender();

    /**
     * Creates a new instance of AndroidWS
     */
    public PostServices() {
        //Config.loadConfig();
    }

    @POST
    @Path(value = "login")
    @Produces(value = MediaType.APPLICATION_JSON)
    public void doPostRequestForLogin(@Suspended final AsyncResponse asyncResponse, @Context final HttpHeaders headers, final String jsonData) {
        executorService.submit(() -> {
            asyncResponse.resume(doDoPostRequestForLogin(headers, jsonData));
        });
    }

    @POST
    @Path("upload")
    @Produces(MediaType.APPLICATION_JSON)
    public Response doPostRequestUpload(@Context HttpHeaders headers, String jsonData) throws Exception {
        System.out.println("geldi");
        Carrier carrier = new Carrier();
//        System.out.println("uploaded file json->" + jsonData);
        carrier.fromJson(jsonData);

        Cookie cookie = headers.getCookies().get("apdtok");
        String cs = cookie.getValue();
        EntityCrUser user = null;

        user = SessionHandler.getTokenFromCookie(cs);

        SessionManager.setUserName(Thread.currentThread().getId(), user.getUsername());
        SessionManager.setLang(Thread.currentThread().getId(), user.selectLang());
        SessionManager.setDomain(Thread.currentThread().getId(), user.selectDomain());
        SessionManager.setUserId(Thread.currentThread().getId(), user.getId());

        String file_type = carrier.getValue("file_type").toString();
        String file_extension = carrier.getValue("file_extension").toString();
        String fileName = "";
        String msg = "";
        String type = "";

        if (file_type.trim().toLowerCase().equals("image")) {
            type = "image";
        } else if (file_type.trim().toLowerCase().equals("video")) {
            type = "video";
        } else if (file_type.trim().toLowerCase().equals("audio")) {
            type = "audio";
        }

        if (type.trim().length() > 0) {
            String img_type[] = Config.getProperty("upload.type." + type).split(",");
            boolean f = false;
            for (String t : img_type) {
                if (file_extension.trim().toLowerCase().equals(t.trim().toLowerCase())) {
                    f = true;
                }
            }
            if (f) {
                FileUpload fileUpload = new FileUpload();
                fileName = fileUpload.uploadImage(carrier.getValue("file_base_64").toString(),
                        file_extension.trim().toLowerCase());
            } else {
                for (String t : img_type) {
                    msg += t + ", ";
                }
                msg = msg.substring(0, msg.length() - 2);
            }
        } else {
            msg = "Incorrent File Type";
        }

        Carrier c = new Carrier();

        c.setValue("msg", msg);
        c.setValue("uploaded_file_name", fileName);

        return Response.status(Response.Status.OK)
                .entity(c.getJson()).build();
    }

    /**
     * PUT method for updating or creating an instance of GetServices
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    private Response doDoPostRequestForLogin(@Context HttpHeaders headers, String jsonData) {
        Connection conn = null;
        try {
            System.out.println("json->" + jsonData);
            conn = new DBConnection().getConnection();
            conn.setAutoCommit(false);

            SessionManager.setConnection(Thread.currentThread().getId(), conn);

            String token;
            Carrier carrier = new Carrier();
            carrier.fromJson(jsonData);
            String usename = carrier.getValue("username").toString();
            String password = carrier.getValue("password").toString();
            String domain = carrier.getValue("domain").toString();
            String lang = carrier.getValue("lang").toString();
            lang = SessionHandler.isLangAvailable(lang) ? lang : "ENG";
            EntityCrUser user = SessionHandler.checkLogin(usename, password, domain);
            user.setLang(lang);
            user.setDomain(user.selectDbname());

            token = SessionHandler.encryptUser(user);
            String fullname = "";
            String id = "";
//            if (!usename.trim().equals("")) {
//                EntityCrUser ent = new EntityCrUser();
//                ent.setDeepWhere(false);
//                ent.setUsername(usename);
//                EntityManager.select(ent);
//                fullname = "Admin Admin";
//            }
            System.out.println("ok4");
            SessionManager.setUserName(Thread.currentThread().getId(), user.getUsername());
            SessionManager.setLang(Thread.currentThread().getId(), lang);
            SessionManager.setUserId(Thread.currentThread().getId(), user.getId());

            String entity = "{\"fullname\":\"" + fullname + "\"}";
            conn.commit();
            //conn.close();
            return Response.status(Response.Status.OK).cookie(new NewCookie("apdtok", token, "/", "", "comment", 10000000, false)).entity(entity).build();
        } catch (Exception ex) {
            DBConnection.rollbackConnection(conn);
            System.err.println("Username Or Password is incorrect!!");
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } finally {
            DBConnection.closeConnection(conn);
            SessionManager.cleanSessionThread();
        }
    }

    @POST
    @Compress
    @Path(value = "cb/{servicename}/{key}/{value}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public void callServiceAsCombobox(@Context HttpHeaders headers, @Suspended
            final AsyncResponse asyncResponse,
            @PathParam(value = "servicename")
            final String servicename, @PathParam(value = "key")
            final String key,
            @PathParam(value = "value")
            final String value, final String json) {
        executorService.submit(() -> {
            String jsonNew = "";
            jsonNew += "{\"b\": {\n";
            jsonNew += "\"includedFields\":\"" + key + "," + value + "\",\n";
            jsonNew += "\"asc\":\"" + value + "\"\n";
            jsonNew += "}}";
//                System.out.println("servicename=" + servicename);
//                System.out.println("json of combo=" + jsonNew);
            asyncResponse.resume(doCallDispatcher(headers, servicename, jsonNew));
        });
    }

    @POST
    @Compress
    @Path(value = "li/{code}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public void callListItem(@Context HttpHeaders headers, @Suspended
            final AsyncResponse asyncResponse,
            @PathParam(value = "code")
            final String itemCode, final String json) {
        executorService.submit(() -> {
            System.out.println("json->" + json);

            Carrier carrier = new Carrier();
            carrier.fromJson(json);
            carrier.setValue("itemCode", itemCode);
            carrier.setValue("asc", "itemValue");

            String jsonNew = "";
            try {
                jsonNew = carrier.getJson();
            } catch (QException ex) {

            }

//            jsonNew += "{\"b\": {";
//            jsonNew += "\"itemCode\":\"" + itemCode + "\",";
//            jsonNew += "\"asc\":\"itemValue\"";
//            jsonNew += "}}";
            String servicename = "serviceCrGetListItemByCode";
            /*try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(PostServices.class.getName()).log(Level.SEVERE, null, ex);
            }*/

            asyncResponse.resume(doCallDispatcher(headers, servicename, jsonNew));
        });
    }

    @POST
    @Compress
    @Path(value = "nali/{code}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public void callNaListItem(@Context HttpHeaders headers, @Suspended
            final AsyncResponse asyncResponse,
            @PathParam(value = "code")
            final String itemCode, final String json) {

        executorService.submit(() -> {
            /*Connection conn = null;
            Response res = null;
            try {

                conn = new DBConnection().getConnection();
                conn.setAutoCommit(false);
                SessionManager.setConnection(Thread.currentThread().getId(), conn);

                long serviceTime = System.currentTimeMillis();
                System.out.println("json->" + json);

                Carrier carrier = new Carrier();
                carrier.fromJson(json);
                carrier.setValue("itemCode", itemCode);
                carrier.setValue("asc", "itemValue");

                carrier = CrModel.getListItemList(carrier);

                String entity = carrier.getJson();
                res = Response.status(Response.Status.OK).entity(entity).build();

            } catch (QException ex) {
                res = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                DBConnection.rollbackConnection(conn);
                //QLogger.saveExceptions(servicename, json, ex.getMessage());
                res = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            } finally {
                DBConnection.closeConnection(conn);
            }

            asyncResponse.resume(res);*/

            System.out.println("json->" + json);

            Carrier carrier = new Carrier();
            carrier.fromJson(json);
            carrier.setValue("itemCode", itemCode);
            carrier.setValue("asc", "itemValue");

            String jsonNew = "";
            try {
                jsonNew = carrier.getJson();
            } catch (QException ex) {

            }

            String servicename = "serviceCrGetListItemList";

            asyncResponse.resume(doCallDispatcherNoToken(headers, servicename, jsonNew));

        });
    }

    @POST
    @Path(value = "srv/{servicename}")
    @Compress
    @Produces(value = MediaType.APPLICATION_JSON)
    public void callDispatcher(@Context HttpHeaders headers, @Suspended
            final AsyncResponse asyncResponse, @PathParam(value = "servicename")
            final String servicename, final String json) {
        executorService.submit(() -> {
            asyncResponse.resume(doCallDispatcher(headers, servicename, json));
        });
    }

    @POST
    @Path(value = "nasrv/{servicename}")
    @Compress
    @Produces(value = MediaType.APPLICATION_JSON)
    public void callNaDispatcher(@Context HttpHeaders headers, @Suspended
            final AsyncResponse asyncResponse, @PathParam(value = "servicename")
            final String servicename, final String json) {
        executorService.submit(() -> {
            asyncResponse.resume(doCallDispatcherNoToken(headers, servicename, json));

            // asyncResponse.resume(doCallDispatcher(headers, servicename, json));
        });
    }

    @GET
    @Path(value = "signup/activate/{activationId}")
    @Compress
    @Produces(value = MediaType.APPLICATION_JSON)
    public void activateCompany(@Context HttpHeaders headers, @Suspended
            final AsyncResponse asyncResponse, @PathParam(value = "activationId")
            final String activationId, final String json) {
        executorService.submit(() -> {

            Carrier carrier = new Carrier();
            carrier.fromJson(json);
            carrier.setValue("activationId", activationId);

            String jsonNew = "";
            try {
                jsonNew = carrier.getJson();
            } catch (QException ex) {

            }

            asyncResponse.resume(doCallDispatcherNoToken(headers, "serviceCrActivateCompany", jsonNew));

            // asyncResponse.resume(doCallDispatcher(headers, servicename, json));
        });
    }

    @GET
    @Path(value = "signup/resend/{userId}")
    @Compress
    @Produces(value = MediaType.APPLICATION_JSON)
    public void resendEmail(@Context HttpHeaders headers, @Suspended
            final AsyncResponse asyncResponse, @PathParam(value = "userId")
            final String userId, final String json) {
        executorService.submit(() -> {

            Carrier carrier = new Carrier();
            carrier.fromJson(json);
            carrier.setValue("userId", userId);

            String jsonNew = "";
            try {
                jsonNew = carrier.getJson();
            } catch (QException ex) {

            }

            asyncResponse.resume(doCallDispatcherNoToken(headers, "serviceCrResendEmail", jsonNew));

            // asyncResponse.resume(doCallDispatcher(headers, servicename, json));
        });
    }

    @POST
    @Compress
    @Path(value = "li/{code}/{sortbykey}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public void callListItemBySort(@Context HttpHeaders headers, @Suspended
            final AsyncResponse asyncResponse,
            @PathParam(value = "code")
            final String itemCode, @PathParam(value = "sortbykey")
            final String sortbykey, final String json) {
        executorService.submit(() -> {
            String jsonNew = "";
            jsonNew += "{\"b\": {";
            jsonNew += "\"itemCode\":\"" + itemCode + "\",";
            jsonNew += "\"asc\":\"itemKey\"";
            jsonNew += "}}";

            String servicename = "serviceCrGetListItemList";

            asyncResponse.resume(doCallDispatcher(headers, servicename, jsonNew));
        });
    }

    private Response doCallDispatcher(@Context HttpHeaders headers,
            @PathParam("servicename") String servicename, String json) {
        Connection conn = null;
        try {

            conn = new DBConnection().getConnection();
            conn.setAutoCommit(false);
            SessionManager.setConnection(Thread.currentThread().getId(), conn);

            long serviceTime = System.currentTimeMillis();
            System.out.println("json srv->" + json);
            Cookie cookie = headers.getCookies().get("apdtok");
            String cs = cookie.getValue();
            EntityCrUser user = null;

            user = SessionHandler.getTokenFromCookie(cs);

            SessionManager.setUserName(Thread.currentThread().getId(), user.getUsername());
            SessionManager.setLang(Thread.currentThread().getId(), user.selectLang());
            SessionManager.setDomain(Thread.currentThread().getId(), user.selectDomain());
            SessionManager.setUserId(Thread.currentThread().getId(), user.getId());

//        SessionManager.setUserName(Thread.currentThread().getId(),"admin1");
            //if (!SessionManager.hasAccessToService(servicename)) {
            //return Response.status(Response.Status.FORBIDDEN).build();
            //}
            System.out.println("Start-" + SessionManager.getCurrentUsername() + ":" + QDate.getCurrentDate() + ":" + QDate.getCurrentTime() + ":" + servicename);

            System.out.println("json->" + json);
            Carrier c = new Carrier();
            c.setServiceName(servicename);
//            System.out.println("json->" + json);
            c.fromJson(json);
            Response res = CallDispatcher.callService(c);
            System.out.println(servicename + " | - Service Executing Time: " + (System.currentTimeMillis() - serviceTime));
            conn.commit();
            //conn.close();

            return res;
        } catch (JoseException ex) {
            DBConnection.rollbackConnection(conn);
            QLogger.saveExceptions(servicename, "loginException", ex.getMessage());
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception ex) {
            DBConnection.rollbackConnection(conn);
            QLogger.saveExceptions(servicename, json, ex.getMessage());
            return Response.status(Response.Status.NOT_FOUND).build();
        } finally {
            DBConnection.closeConnection(conn);
            SessionManager.cleanSessionThread();
        }

    }

    private Response doCallDispatcherNoToken(@Context HttpHeaders headers,
            @PathParam("servicename") String servicename, String json) {
        String srv[] = new String[]{"serviceCrSignupPersonal",
            "serviceCrSignupCompany",
            "serviceCrSignupCompany",
            "serviceCrActivateCompany",
            "serviceCrGetMessageText",
            "serviceCrGetModuleList4ComboNali"};

        if (!ArrayUtils.contains(srv, servicename)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Connection conn = null;
        try {
            conn = new DBConnection().getConnection();
            conn.setAutoCommit(false);
            SessionManager.setConnection(Thread.currentThread().getId(), conn);

            long serviceTime = System.currentTimeMillis();
            System.out.println("json srv->" + json);
            System.out.println("Start-" + SessionManager.getCurrentUsername()
                    + ":" + QDate.getCurrentDate() + ":" + QDate.getCurrentTime() + ":" + servicename);

            System.out.println("json->" + json);
            Carrier c = new Carrier();
            c.setServiceName(servicename);
//            System.out.println("json->" + json);
            c.fromJson(json);
            Response res = CallDispatcher.callService(c);
            System.out.println(servicename + " | - Service Executing Time: " + (System.currentTimeMillis() - serviceTime));
            conn.commit();
            //conn.close();

            if (servicename.equals("serviceCrSignupPersonal")
                    || servicename.equals("serviceCrSignupCompany")) {
                return Response.temporaryRedirect(new URI("/apd/activation.html?id=" + c.getValue(EntityCrUser.ID).toString())).build();
            }

            if (servicename.equals("serviceCrActivateCompany")) {
                return Response.temporaryRedirect(new URI("/apd/activated.html")).build();
            }
            return res;
        } catch (JoseException ex) {
            DBConnection.rollbackConnection(conn);
            QLogger.saveExceptions(servicename, "loginException", ex.getMessage());
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception ex) {
            DBConnection.rollbackConnection(conn);
            QLogger.saveExceptions(servicename, json, ex.getMessage());
            return Response.status(Response.Status.NOT_FOUND).build();
        } finally {
            DBConnection.closeConnection(conn);
            SessionManager.cleanSessionThread();
        }
    }

    @GET
    @Path(value = "getContent")
    @Compress
    @Produces(value = MediaType.TEXT_HTML)
    public Response getContent(@Context HttpHeaders headers) {
        Connection conn = null;
        try {
            conn = new DBConnection().getConnection();
            conn.setAutoCommit(false);
            SessionManager.setConnection(Thread.currentThread().getId(), conn);

            Cookie cookie = headers.getCookies().get("apdtok");
            String cs = cookie.getValue();
            EntityCrUser user = null;
            long startTime = System.currentTimeMillis();
            user = SessionHandler.getTokenFromCookie(cs);
            SessionManager.setUserName(Thread.currentThread().getId(), user.getUsername());
            SessionManager.setLang(Thread.currentThread().getId(), user.selectLang());
            SessionManager.setDomain(Thread.currentThread().getId(), user.selectDomain());
            SessionManager.setUserId(Thread.currentThread().getId(), user.getId());

            System.out.println("Getting User: " + (System.currentTimeMillis() - startTime));

//         EntityCrUser user = new EntityCrUser();
//         user.setUsername("admin1");
            startTime = System.currentTimeMillis();
            UserController uc = new UserController();
            String content = uc.filterText(user.getUsername());
            System.out.println("Filter HTML: " + (System.currentTimeMillis() - startTime));
            conn.commit();
            //conn.close();
            return Response.status(Response.Status.OK).entity(content).build();
        } catch (QException ex) {
            DBConnection.rollbackConnection(conn);
            QLogger.saveExceptions("getContent", "createConnection", ex.getMessage());
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (SQLException | IOException ex) {
            DBConnection.rollbackConnection(conn);
            QLogger.saveExceptions("getContent", "getContent", ex.getMessage());
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (JoseException ex) {
            DBConnection.rollbackConnection(conn);
            QLogger.saveExceptions("getContent", "getToken", ex.getMessage());
            return Response.status(Response.Status.NOT_FOUND).build();
        } finally {
            DBConnection.closeConnection(conn);
            SessionManager.cleanSessionThread();
        }
    }

    @POST
    @Path(value = "signup/{type}")
    @Compress
    @Produces(value = MediaType.TEXT_HTML)
    public Response signup(@Context HttpHeaders headers, @PathParam(value = "type")
            final String type, final String json) throws QException {

        Connection conn = null;
        try {

            conn = new DBConnection().getConnection();
            conn.setAutoCommit(false);
            SessionManager.setConnection(Thread.currentThread().getId(), conn);

//            System.out.println("json>>>" + json);
            Carrier carrier = new Carrier();
            carrier.fromJson(json);
            String lang = carrier.getValue("lang").toString();
            lang = SessionHandler.isLangAvailable(lang) ? lang : "ENG";
            SessionManager.setLang(Thread.currentThread().getId(), lang);

            long startTime = System.currentTimeMillis();
            UserController uc;
            if ("company".equals(type)) {
                uc = new UserController("page_signup_company.html");
            } else if ("personal".equals(type)) {
                uc = new UserController("page_signup_personal.html");
            } else {
                conn.commit();
                return Response.status(Response.Status.BAD_REQUEST).entity("").build();

            }
            String content = uc.filterText("__singup__");
            System.out.println("Filter HTML: " + (System.currentTimeMillis() - startTime));
            conn.commit();

            return Response.status(Response.Status.OK).entity(content).build();
        } catch (QException ex) {
            DBConnection.rollbackConnection(conn);
            QLogger.saveExceptions("signup", "createConnection", ex.getMessage());
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (SQLException | IOException ex) {
            DBConnection.rollbackConnection(conn);
            QLogger.saveExceptions("signup", "getContent", ex.getMessage());
            return Response.status(Response.Status.NOT_FOUND).build();
        } finally {
            DBConnection.closeConnection(conn);
            SessionManager.cleanSessionThread();
        }
    }

}
