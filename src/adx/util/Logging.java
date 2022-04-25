package adx.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * A simple logging class.
 */
public class Logging {

  public final static boolean LOGGING = true;

  public static void log(String message) {
    if (LOGGING) {
      System.out.println(message);
    }
  }

  public static void log(Object o) {
    if (LOGGING) {
      System.out.println(o);
    }
  }

  public static void log(String message, String filename) {
    try {
      new FileOutputStream(filename, true).close();
      Files.write(Paths.get(filename), message.getBytes(), StandardOpenOption.APPEND);
    } catch (IOException e) {
      Logging.log("ERROR WRITTING TO FILE -> " + filename);
    }
  }

}
