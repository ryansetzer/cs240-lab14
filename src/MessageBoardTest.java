import static org.junit.jupiter.api.Assertions.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Tests for the MessageBoard
 * 
 * @author Nathan Sprague
 *
 */
@TestMethodOrder(OrderAnnotation.class)
class MessageBoardTest {

  @Test
  @Order(1)
  void testFunctionlityNoThreads() {
    MessageBoard board = new MessageBoard();

    int latestId;

    latestId = board.addMessage("Party planning!");
    assertEquals(0, latestId);

    assertEquals("Party planning!", board.getMessageText(0));

    latestId = board.addMessage("Wed. or Thurs?");

    assertEquals(1, latestId);

    assertEquals("Party planning!", board.getMessageText(0));
    assertEquals("Wed. or Thurs?", board.getMessageText(1));

    latestId = board.addReply(1, "Wed. is no good.");
    assertEquals(2, latestId);
    latestId = board.addReply(1, "Wed. works for me!");
    assertEquals(3, latestId);

    latestId = board.addReply(2, "Why do you hate Wednesdays?");
    assertEquals(4, latestId);
    latestId = board.addReply(4, "I don't hate Wednesdays!");
    assertEquals(5, latestId);

    latestId = board.addMessage("Wait! Why are we having a party");
    assertEquals(6, latestId);
    assertEquals(List.of(5), board.getReplies(4));
    assertEquals(List.of(2, 3), board.getReplies(1));
    assertEquals(List.of(), board.getReplies(6));
  }

  @Test
  @Order(2)
  void testFunctionlityWithThreads() {
    MessageBoard board = new MessageBoard();

    int latestId;

    latestId = board.addMessage("Party planning!");
    assertEquals(0, latestId);

    assertEquals("Party planning!", board.getMessageText(0));

    latestId = board.addMessage("Wed. or Thurs?");

    assertEquals(1, latestId);

    assertEquals("Party planning!", board.getMessageText(0));
    assertEquals("Wed. or Thurs?", board.getMessageText(1));

    latestId = board.addReply(1, "Wed. is no good.");
    assertEquals(2, latestId);
    latestId = board.addReply(1, "Wed. works for me!");
    assertEquals(3, latestId);

    latestId = board.addReply(2, "Why do you hate Wednesdays?");
    assertEquals(4, latestId);
    latestId = board.addReply(4, "I don't hate Wednesdays!");
    assertEquals(5, latestId);

    latestId = board.addMessage("Wait! Why are we having a party");
    assertEquals(6, latestId);
    assertEquals(List.of(5), board.getReplies(4));
    assertEquals(List.of(2, 3), board.getReplies(1));
    assertEquals(List.of(), board.getReplies(6));
    assertEquals(List.of(2, 4, 5), board.getThread(2));
    assertEquals(List.of(1, 2, 4, 5, 3), board.getThread(1));
  }
  @Order(3)
  void runTimings(int numMessages, double pctReplies, int numMessageLookups, int numRepliesLookups,
      int numThreadLookups) {
    MessageBoard board = new MessageBoard();
    Random gen = new Random(100101);
    long addTime;
    long lookupTextTime;
    long lookupReplyTime;
    long lookupThreadTime;
    String s = NumberFormat.getIntegerInstance().format(numMessages);
    System.out.printf("TIMING MESSAGE ADDING (%s messages)\n", s);

    long start = System.nanoTime();
    board.addMessage("Hello");
    for (int i = 1; i < numMessages; i++) {
      if (gen.nextDouble() < pctReplies) {
        int replyTo = gen.nextInt(i);
        board.addReply(replyTo, "MSG" + i);
      } else {
        board.addMessage("MSG" + i);
      }
    }
    addTime = System.nanoTime() - start;

    System.out.println("TIMING getMessageText...");
    start = System.nanoTime();
    for (int i = 1; i < numMessageLookups; i++) {
      board.getMessageText(gen.nextInt(numMessages));
    }
    lookupTextTime = System.nanoTime() - start;

    System.out.println("TIMING getReplies...");
    start = System.nanoTime();
    for (int i = 1; i < numRepliesLookups; i++) {
      board.getReplies(gen.nextInt(numMessages));
    }
    lookupReplyTime = System.nanoTime() - start;


    start = System.nanoTime();
    System.out.println("TIMING getThread...");
    for (int i = 1; i < numThreadLookups; i++) {
      board.getThread(gen.nextInt(numMessages));
    }
    lookupThreadTime = System.nanoTime() - start;

    long totalTime = addTime + lookupTextTime + lookupReplyTime + lookupThreadTime;


    System.out.printf("TOTAL TIME: %.2f seconds.\n", (totalTime) / 1000000000.0);
    System.out.printf("  Add time: %.2f seconds.\n", (addTime) / 1000000000.0);
    System.out.printf("  Text lookup time: %.2f seconds.\n", (lookupTextTime) / 1000000000.0);
    System.out.printf("  Reply lookup time: %.2f seconds.\n", (lookupReplyTime) / 1000000000.0);
    System.out.printf("  Thread lookup time: %.2f seconds.\n", (lookupThreadTime) / 1000000000.0);


  }

  @Test
  void testTiming() {
    runTimings(1000000, .75, 1000000, 500000, 500000);

    System.out.println("\nDOUBLING OPERATIONS AND REPEATING TIMING...\n");

    runTimings(2000000, .75, 2000000, 1000000, 1000000);
  }

}
