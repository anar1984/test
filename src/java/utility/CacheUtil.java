/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.Key;

import module.cr.CrModel;
import org.apache.logging.log4j.LogManager;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.Status;
import org.ehcache.config.Configuration;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.xml.XmlConfiguration;
import resources.config.Config;

/**
 *
 * @author nikli
 */
public class CacheUtil {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger();
    public static CacheManager cacheManager;
    public static String CACHE_KEY_MODULE = "cache.key.module";
    public static String CACHE_KEY_ATTRIBUTE = "cache.key.attribute";
    public static String CACHE_KEY_LISTITEM = "cache.key.listitem";
    public static String CACHE_KEY_ENTITY_LABEL = "cache.key.entitylabel";
    public static String CACHE_KEY_SUBMODULE_ATTRIBUTE = "cache.key.submoduleattribute";
    public static String CACHE_KEY_SUBMODULE = "cache.key.submodule";
    
    private static Cache<String, Carrier> modelCache;
    
    private static Cache<String, Key> tokenKeyCache;
    
    

    public static void initCache(URL configUrl) {
        Configuration xmlConfig = new XmlConfiguration(configUrl);
        cacheManager = CacheManagerBuilder.newCacheManager(xmlConfig);
        cacheManager.init();
        
        try {
            modelCache = cacheManager.getCache("modelCache", String.class, Carrier.class);
            tokenKeyCache = cacheManager.getCache("tokenKeyCache", String.class, Key.class);
        } catch(Exception e) {
            logger.debug("getCache " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void closeCache() {
        if (cacheManager != null && cacheManager.getStatus() != Status.UNINITIALIZED) {
            cacheManager.close();
        }
    }

    public static Carrier getFromCache(String cacheKey) throws QException {
        Carrier carrier = new Carrier();
        if (modelCache.containsKey(cacheKey)
                && cacheManager.getStatus() == Status.AVAILABLE) {
            carrier = modelCache.get(cacheKey);
            logger.debug("getFromCache.cacheKey=" + cacheKey + " provided from cache");
        } else {
            try {
                String methodName = Config.getProperty(getPropKey(cacheKey));
                CrModel mdl = new CrModel();
                Method method = mdl.getClass().getMethod(methodName, Carrier.class);
                Object retObj = method.invoke(mdl, prepareParam(cacheKey));
                logger.debug("getFromCache.cacheKey=" + cacheKey + " provided from model");
                putCache(cacheKey, (Carrier) retObj);
                carrier = (Carrier) retObj;
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
    
    public static Key getKeyFromCache(String cacheKey) {
        if (tokenKeyCache.containsKey(cacheKey)
                && cacheManager.getStatus() == Status.AVAILABLE) {
            return tokenKeyCache.get(cacheKey);
        } else {
            return null;
        }
    }
    
    public static void putSessionCache(String cacheKey, Key val) {
        tokenKeyCache.put(cacheKey, val);
        logger.debug("putCache.cacheKey=" + cacheKey);
    }
    

    private static String getPropKey(String cacheKey) {
        int ix = cacheKey.indexOf("::");
        if (ix == -1) {
            return cacheKey;
        }
        return cacheKey.substring(0, ix);
    }

    private static Carrier prepareParam(String cacheKey) {
        int ix = cacheKey.indexOf("::");
        Carrier crParam = new Carrier();
        if (ix == -1) {
            return crParam;
        }
        String[] params = cacheKey.substring(ix + 2).split("&");
        for (String param : params) {
            String[] s = param.split("=");
            crParam.setValue(s[0], s[1]);
        }

        return crParam;

    }

    public static void putCache(String cacheKey, Carrier carrier) {
        modelCache.put(cacheKey, carrier);
        logger.debug("putCache.cacheKey=" + cacheKey);
    }

}
