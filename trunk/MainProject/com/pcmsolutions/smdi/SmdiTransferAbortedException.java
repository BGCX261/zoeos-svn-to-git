package com.pcmsolutions.smdi;

/**
 * Created by IntelliJ IDEA.
 * User: pmeehan
 * Date: 09-Sep-2003
 * Time: 21:38:02
 * To change this template use Options | File Templates.
 */
public class SmdiTransferAbortedException extends Exception {
    public SmdiTransferAbortedException(String message) {
        super(message);
    }

    public SmdiTransferAbortedException() {
        super("SMDI transfer was aborted");
    }
}
