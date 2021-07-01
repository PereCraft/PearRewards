/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pearrewards.exception;

/**
 *
 * @author antonio
 */
public class PearRewardsException extends Exception {

    /**
     * Creates a new instance of <code>PearRewardsException</code> without
     * detail message.
     */
    public PearRewardsException() {
    }

    /**
     * Constructs an instance of <code>PearRewardsException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public PearRewardsException(String msg) {
        super(msg);
    }
}
