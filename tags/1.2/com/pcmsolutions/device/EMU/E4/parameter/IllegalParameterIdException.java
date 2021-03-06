/*
 * IllegalParameterId.java
 *
 * Created on January 2, 2003, 7:41 PM
 */

package com.pcmsolutions.device.EMU.E4.parameter;

/**
 *
 * @author  pmeehan
 */
public class IllegalParameterIdException extends java.lang.Exception {

    /**
     * Creates a new instance of <code>IllegalParameterId</code> without detail message.
     */
    public IllegalParameterIdException() {
    }


    /**
     * Constructs an instance of <code>IllegalParameterId</code> with the specified detail message.
     * @param msg the detail message.
     */
    public IllegalParameterIdException(String msg) {
        super(msg);
    }
}
