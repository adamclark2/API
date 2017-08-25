package com.durp.Model.Managers;

/**
 * Manages API Keys
 *
 */
public class APIKeyManager {

    /**
     * Verify an APIKey is valid
     *
     * TODO implement this, currently always returns true except when null
     *
     * @author Adam
     * @param key String representing the key
     */
    public static boolean verifyKey(String key){
        if(key == null){
            return false;
        }

        return true;
    }

    /**
     * Get a JSON message describing the error
     *
     * TODO Implement getErrorMessage();
     */
    public static String getErrorMessage(){
        return "APIKEY OOPS!";
    }
}
