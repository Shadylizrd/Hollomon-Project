import java.io.IOException;
import java.io.BufferedReader;

// maybe this file isnt completely necessary
// makes it so lambda with ReaderTask can throw IOExceptions
@FunctionalInterface
interface ReaderTask {
    void execute(BufferedReader reader) throws IOException;
}
