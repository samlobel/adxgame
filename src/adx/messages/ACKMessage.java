package adx.messages;

/**
 * A simple message to acknowledge reception
 * of other messages.
 * 
 * @author Enrique Areyan Viqueira
 */
public class ACKMessage {

  /**
   * Code of the message.
   */
  private final boolean code;

  /**
   * Brief message.
   */
  private final String message;

  /**
   * Constructor.
   */
  public ACKMessage() {
    super();
    this.code = true;
    this.message = "";
  }

  /**
   * Constructor.
   * 
   * @param code
   * @param message
   */
  public ACKMessage(boolean code, String message) {
    super();
    this.code = code;
    this.message = message;
  }

  /**
   * Getter.
   * 
   * @return the code of the message.
   */
  public boolean getCode() {
    return this.code;
  }

  /**
   * Getter.
   * 
   * @return an optional description. 
   */
  public String getMessage() {
    return this.message;
  }

}
