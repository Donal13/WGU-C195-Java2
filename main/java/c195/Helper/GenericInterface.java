package c195.Helper;

import java.io.IOException;

/**
 * This functional interface is designed for <b>lambda</b> expressions that perform actions based on a string input.
 * It includes a single abstract method, performAction, which can be implemented to perform any operation that uses a string parameter and throws an IOException if necessary.
 */
@FunctionalInterface
public interface GenericInterface {
    /**
     * Performs an action using the provided string.
     * The specific action is defined by the <b>lambda</b> expression or method reference that implements this interface.
     * @param string The string input to be used by the action.
     * @throws IOException If an I/O error occurs during the execution of the action.
     */
    void performAction(String string) throws IOException;

}
