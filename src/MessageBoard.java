import java.util.List;

public class MessageBoard {


  public MessageBoard() {
   
  }

  /**
   * Add a new message to the board.
   * 
   * @param text The text of the message
   * @return The message id.
   */
  public int addMessage(String text) {
    return -1;
  }

  /**
   * Add a reply to an existing message.
   * 
   * @param msgId The message to reply to
   * @param text The text of the message
   * @return The new message id
   */
  public int addReply(int msgId, String text) {
    return -1;
  }

  /**
   * Return the text for a message.
   */
  public String getMessageText(int msgId) {
    return null;
  }

  /**
   * Get the list of replies for a particular message (in order).
   * 
   * @param msgId The message
   * @return A List of message ids corresponding to replies
   */
  public List<Integer> getReplies(int msgId) {
    return null;
  }

  /**
   * A thread is composed of a message, all the replies to that message, 
   * all replies to the replies, etc. The returned list will be in the order 
   * the posts would appear on the board. I.e. replies appear after the message they 
   * are replying to.
   * 
   * @param msgId The start of the thread
   * @return A List of all messages in the thread
   */
  public List<Integer> getThread(int msgId) {
    return null;
  }

}
