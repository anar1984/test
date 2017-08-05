/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auth;

import java.security.Key;
import org.jose4j.keys.AesKey;
import org.jose4j.lang.ByteUtil;

/**
 *
 * @author Azerbaycan
 */
public class SKey {
    private static Key key = new AesKey(ByteUtil.randomBytes(16));;
    
    private SKey() {
        key = new AesKey(ByteUtil.randomBytes(16));
    }

    public static Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        SKey.key = key;
    }
    
    public static SKey getInstance() {
        return KeyHolder.INSTANCE;
    }
    
    private static class KeyHolder {

        private static final SKey INSTANCE = new SKey();
    }
}
