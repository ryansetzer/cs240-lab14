import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageBoard {

  private int messageCount = 0;
  private HashMap<Integer, String> messages;
  private HashMap<Integer, ArrayList<Integer>> replies;

  public MessageBoard() {
    this.messages = new HashMap<>();
    this.replies = new HashMap<>();
  }

  /**
   * Add a new message to the board.
   *
   * @param text The text of the message
   * @return The message id.
   */
  public int addMessage(String text) {
    int messageId = messageCount;
    messages.put(messageId, text);
    messageCount++;
    return messageCount - 1;
  }

  /**
   * Add a reply to an existing message.
   *
   * @param msgId The message to reply to
   * @param text  The text of the message
   * @return The new message id
   */
  public int addReply(int msgId, String text) {
    int messageId = addMessage(text);
    if (replies.containsKey(msgId)) {
      replies.get(msgId).add(messageId);
    } else {
      ArrayList<Integer> reply = new ArrayList<>();
      reply.add(messageId);
      replies.put(msgId, reply);
    }
    return messageId;
  }

  /**
   * Return the text for a message.
   */
  public String getMessageText(int msgId) {
    return messages.get(msgId);
  }

  /**
   * Get the list of replies for a particular message (in order).
   *
   * @param msgId The message
   * @return A List of message ids corresponding to replies
   */
  public List<Integer> getReplies(int msgId) {
    List<Integer> replyList = new ArrayList<>();
    if (replies.containsKey(msgId)) {
      for (Integer message : replies.get(msgId)) {
        replyList.add(message);
      }
    }
    return replyList;
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
    List<Integer> replyList = new ArrayList<>();
    replyList.add(msgId);
    if (replies.containsKey(msgId)) {
      for (Integer message : replies.get(msgId)) {
        replyList.addAll(getThread(message));
      }
    }
    return replyList;
  }
}
