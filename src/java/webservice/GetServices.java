/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import utility.CallDispatcher;
import utility.Carrier;
//import smssender.Config;
//import smssender.SMSSender;

/**
 * REST Web Service
 *
 * @author nikli
 */
@Path("get")
public class GetServices {

    @Context
    private UriInfo context;

//    SMSSender smsSender = new SMSSender();

    /**
     * Creates a new instance of AndroidWS
     */
    public GetServices() {
        //Config.loadConfig();
    }

    /**
     * Retrieves representation of an instance of com.andr.GetServices
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Path(value = "testService")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response doGetRequest() {
        return Response.status(Response.Status.OK).entity("asldfkj aslkdfj ;aslkfj;alskdf j").build();
    }

    @GET
    @Path(value = "sendsms")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response sendSms() {

        return Response.status(Response.Status.OK).entity("SMS gonderildi").build();
    }

    @GET
    @Path(value = "payment")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response payment() {
        
        return Response.status(Response.Status.OK).entity("sorgu gonderildi").build();
    }
    
    @GET
    @Path(value = "sms/{msg}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response sms(@PathParam("msg") String msg) {

        return Response.status(Response.Status.OK).entity(msg + " gonderildi").build();
    }

    @POST
    @Path(value = "postsms")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response postsms(String msg) {
        return Response.status(Response.Status.OK).entity(msg +" gonderildi").build();
    }

    /**
     * PUT method for updating or creating an instance of GetServices
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }

    @GET
    @Path("test/{msg}")
    @Produces("application/json")
    @Consumes("application/json")
    public String test(@PathParam("msg") String msg) {
        return "Salam " + msg;
//        return null;
    }
    private final ExecutorService executorService = java.util.concurrent.Executors.newCachedThreadPool();
    
    @GET
    @Path(value = "srv/{servicename}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public void callDispatcher(@Suspended
    final AsyncResponse asyncResponse, @PathParam(value = "servicename")
    final String servicename, final String json) {
        Future<?> submit = executorService.submit(() -> {
            try {
                asyncResponse.resume(doCallDispatcher(servicename, json));
            } catch (IllegalAccessException ex) {
                Logger.getLogger(GetServices.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchMethodException ex) {
                Logger.getLogger(GetServices.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(GetServices.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(GetServices.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(GetServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private Response doCallDispatcher(@PathParam("servicename") String servicename, String json) throws InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException, Exception {
//        System.out.println(servicename);
//        System.out.println(json);
        
        Carrier c = new Carrier();
        c.setServiceName(servicename);
        c.fromJson(json);
        return CallDispatcher.callService(c);
    }
}
