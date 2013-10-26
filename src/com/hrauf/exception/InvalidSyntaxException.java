/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrauf.exception;

/**
 *
 * @author Hammad
 */
public class InvalidSyntaxException extends Exception {
      public InvalidSyntaxException() {
        super();
    }
    
    public InvalidSyntaxException(String mesg) {
        super(mesg);
    }
      
}
