package com.durp.Model.Exceptions;

/**
 * The username or password or email was invalid.
 */
public class InvalidUserException extends Exception {

    public UserError userError;
    public InvalidUserException(String message){
        super(message);
        userError.errorReason = message;
    }

}
