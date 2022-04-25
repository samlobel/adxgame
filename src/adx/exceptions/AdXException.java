package adx.exceptions;

/**
 * A generic exception thrown while running the AdX game.
 * 
 * @author Enrique Areyan Viqueira
 */
@SuppressWarnings("serial")
public class AdXException extends Exception{

  public AdXException(String string) {
    super(string);
  }

}
