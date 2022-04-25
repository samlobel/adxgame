package adx.messages;
/**
 * A message that signals the server that an agent wants to connect. The message
 * is also used by the server to communicate to the agent that a connection has
 * been either refused (if credentials are unknown) or accepted (if credentials
 * are OK, OR the agent is already connected).
 * All setters of this class implement singleton.
 * 
 * @author Enrique Areyan Viqueira
 *
 */
public class ConnectServerMessage {

  /**
   * The agent name as recognized by the server.
   */
  private String agentName;
  
  /**
   * The agent password as recognized by the server.
   */
  private String agentPassword;
  
  /**
   * Stores the response from the server.
   */
  private String serverResponse;
  
  /**
   * Stores a numerical code with the server response.
   */
  private int statusCode = -1;

  /**
   * Constructor.
   */
  public ConnectServerMessage() {
    super();
  }

  /**
   * Setter. Implements singleton.
   * 
   * @param agentName
   * @throws Exception
   */
  public void setAgentName(String agentName) throws Exception {
    if (this.agentName == null) {
      this.agentName = agentName;
    } else {
      throw new Exception("Agent Name can only be set once in JoinGame");
    }
  }

  /**
   * Setter. Implements singleton.
   * 
   * @param agentPassword
   * @throws Exception
   */
  public void setAgentPassword(String agentPassword) throws Exception {
    if (this.agentPassword == null) {
      this.agentPassword = agentPassword;
    } else {
      throw new Exception("Agent Password can only be set once in JoinGame");
    }
  }

  /**
   * Setter. Implements singleton.
   * 
   * @param serverResponse
   * @throws Exception
   */
  public void setServerResponse(String serverResponse) throws Exception {
    if (this.serverResponse == null) {
      this.serverResponse = serverResponse;
    } else {
      throw new Exception("Server Response can only be set once");
    }
  }

  /**
   * Setter. Implements singleton.
   * 
   * @param statusCode
   * @throws Exception
   */
  public void setStatusCode(int statusCode) throws Exception {
    if (this.statusCode == -1) {
      this.statusCode = statusCode;
    } else {
      throw new Exception("Status Code can only be set once");
    }
  }

  /**
   * Getter. 
   * 
   * @return the agents name.
   */
  public String getAgentName() {
    return this.agentName;
  }

  /**
   * Getter.
   * 
   * @return the agent password.
   */
  public String getAgentPassword() {
    return this.agentPassword;
  }

  /**
   * Getter. 
   * 
   * @return the response from the server.
   */
  public String getServerResponse() {
    return this.serverResponse;
  }

  /**
   * Getter.
   * 
   * @return the response numerical code from the server.
   */
  public int getStatusCode() {
    return this.statusCode;
  }

}
