/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

import module.cr.CrModel;
import org.apache.logging.log4j.LogManager;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.Status;
import org.ehcache.config.Configuration;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.xml.XmlConfiguration;

/**
 *
 * @author nikli
 */
public class CacheUtil {

    private static org.apache.logging.log4j.Logger logger = LogManager.getLogger();
    public static CacheManager cacheManager;
    public static String CACHE_KEY_MODULE = "cache.key.module";
    public static String CACHE_KEY_ATTRIBUTE = "cache.key.attribute";
    public static String CACHE_KEY_LISTITEM = "cache.key.listitem";
    

    public static void initCache(URL configUrl) {
        Configuration xmlConfig = new XmlConfiguration(configUrl);
        CacheUtil.cacheManager = CacheManagerBuilder.newCacheManager(xmlConfig);
        CacheUtil.cacheManager.init();
    }
    
    public static void closeCache() {
        if (cacheManager != null && cacheManager.getStatus()!=Status.UNINITIALIZED) {
            cacheManager.close();
        }
    }

    public static Carrier getFromCache(String cacheKey) throws QException {
        Cache<String, Carrier> serviceCache = cacheManager
                .getCache("modelCache", String.class, Carrier.class);

        Carrier carrier = null;

        if (cacheManager.getStatus() == Status.AVAILABLE
                && serviceCache.containsKey(cacheKey)) {
            carrier = serviceCache.get(cacheKey);
            logger.debug("getFromCache.cacheKey="+cacheKey+" provided from cache");
        } else {
            try {
                CommonConfigurationProperties prop = new CommonConfigurationProperties();
                String methodName = prop.getProperty(getPropKey(cacheKey));
                CrModel mdl = new CrModel();
                Method method = mdl.getClass().getMethod(methodName, Carrier.class);
                Object retObj = method.invoke(mdl, prepareParam(cacheKey));
                logger.debug("getFromCache.cacheKey="+cacheKey+" provided from model");
                putCache(cacheKey, (Carrier) retObj);

            } catch (UnsupportedEncodingException ex) {
                logger.error("getFromCache.cacheKey=" + cacheKey, ex);
                throw new QException(ex);
            } catch (NoSuchMethodException ex) {
                logger.error("getFromCache.cacheKey=" + cacheKey, ex);
                throw new QException(ex);
            } catch (SecurityException ex) {
                logger.error("getFromCache.cacheKey=" + cacheKey, ex);
                throw new QException(ex);
            } catch (IllegalAccessException ex) {
                logger.error("getFromCache.cacheKey=" + cacheKey, ex);
                throw new QException(ex);
            } catch (IllegalArgumentException ex) {
                logger.error("getFromCache.cacheKey=" + cacheKey, ex);
                throw new QException(ex);
            } catch (InvocationTargetException ex) {
                logger.error("getFromCache.cacheKey=" + cacheKey, ex);
                throw new QException(ex);
            }

        }

        return carrier;
    }
    
    private static String getPropKey(String cacheKey) {
        int ix = cacheKey.indexOf("::");
        if (ix==-1) {
            return cacheKey;
        }
        return cacheKey.substring(0, ix);
    }
    
    private static Carrier prepareParam(String cacheKey) {
        int ix = cacheKey.indexOf("::");
        Carrier crParam = new Carrier();
        if (ix==-1) {
            return crParam;
        }
        String[] params = cacheKey.substring(ix+2).split("&");
        for (String param:params) {
            String[] s = param.split("=");
            crParam.setValue(s[0], s[1]);
        }
        
        return crParam;
        
    }

    public static void putCache(String cacheKey, Carrier carrier) {
        Cache<String, Carrier> serviceCache = cacheManager
                .getCache("modelCache", String.class, Carrier.class);
        serviceCache.put(cacheKey, carrier);
        logger.debug("putCache.cacheKey="+cacheKey);

    }

}
