package main;

import java.io.IOException;
import java.io.BufferedReader;

/**
 * Functional interface that allows an abstract method to be defined which can take
 * @param reader to use within the method and
 * @throws IOException in case of server output stream closing prematurely.
 */
@FunctionalInterface
public interface ReaderTask {
    void execute(BufferedReader reader) throws IOException;
}
