import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;



/**
 * Correctness and performance tests for HideNSeek.
 * 
 * @author Nathan Sprague
 *
 */

@TestMethodOrder(OrderAnnotation.class)
class HideNSeekTest {
  
  /**
   * Generate random pairs with no duplicates.
   */
  public List<List<Integer>> randomPairs(int howMany, List<List<Integer>> avoid) {
    List<List<Integer>> result = new ArrayList<>();
    Random gen = new Random(100101);
    HashSet<List<Integer>> seen = new HashSet<>();
    HashSet<List<Integer>> avoidSet = new HashSet<>(avoid);

    while (result.size() < howMany) {
      List<Integer> xy = List.of(gen.nextInt(10), gen.nextInt());

      if (!seen.contains(xy) && !avoidSet.contains(xy)) {
        seen.add(xy);
        result.add(xy);
      }
    }
    return result;
  }


  @Test
  @Order(1)
  void testCorrectness() {
    HideNSeek hs = new HideNSeek();

    assertFalse(hs.seek(1, 2, "monkey"));

    hs.hide(1, 2, "keys");
    assertTrue(hs.seek(1, 2, "keys"));
    assertFalse(hs.seek(1, 2, "monkey"));
    assertFalse(hs.seek(2, 1, "keys"));

    hs.hide(1, 2, "glasses");
    assertTrue(hs.seek(1, 2, "keys"));
    assertTrue(hs.seek(1, 2, "glasses"));
    assertFalse(hs.seek(1, 2, "monkey"));

    hs.hide(10000, 10000, "keys");
    assertTrue(hs.seek(10000, 10000, "keys"));
    assertTrue(hs.seek(1, 2, "keys"));
    assertFalse(hs.seek(10000, 10000, "monkey"));
    assertFalse(hs.seek(-10, 20, "keys"));
  }


  private void runSpeedTest(int numInPositions, int numOutPositions, int numDups) {
    long randomAddTime, dupAddTime, randomLookupTime, dupLookupTime;

    System.out.println("GENERATING RANDOM DATA...");

    // A bunch of random pairs, not including (0, 0)
    List<List<Integer>> inPairs = randomPairs(numInPositions, List.of(List.of(0, 0)));
    List<List<Integer>> inPairsPlus = new ArrayList<>(inPairs);
    inPairsPlus.add(List.of(0, 0));
    List<List<Integer>> outPairs = randomPairs(numOutPositions, inPairsPlus);


    HideNSeek hs = new HideNSeek();

    System.out.println("TIMING RANDOM HIDES...");
    long start = System.nanoTime();
    for (List<Integer> pair : inPairs) {
      hs.hide(pair.get(0), pair.get(1), "A");
      hs.hide(pair.get(0), pair.get(1), "B");
    }
    randomAddTime = System.nanoTime() - start;

    System.out.println("TIMING DUPLICATE HIDES...");
    start = System.nanoTime();
    for (int i = 0; i < numDups; i++) {
      hs.hide(0, 0, "item" + i);
    }
    dupAddTime = System.nanoTime() - start;

    System.out.println("TIMING RANDOM LOOKUPS...");
    start = System.nanoTime();
    for (List<Integer> pair : inPairs) {
      hs.seek(pair.get(0), pair.get(1), "B");
      hs.seek(pair.get(0), pair.get(1), "Z");
    }
    for (List<Integer> pair : outPairs) {
      hs.seek(pair.get(0), pair.get(1), "B");
    }
    randomLookupTime = System.nanoTime() - start;


    System.out.println("TIMING DUPLICATE LOOKUPS...");
    start = System.nanoTime();
    for (int i = 0; i < numDups; i++) {
      hs.seek(0, 0, "item" + i);
      hs.seek(0, 0, "Zitem" + i);
    }
    dupLookupTime = System.nanoTime() - start;

    System.out.printf("TOTAL TIME: %.2f seconds.\n",
        (randomAddTime + dupAddTime + randomLookupTime + dupLookupTime) / 1000000000.0);

    System.out.printf("Hide: %8d operations performed in %4.2f seconds.\n",
        numInPositions * 2 + numDups, (randomAddTime + dupAddTime) / 1000000000.0);

    System.out.printf("Seek: %8d operations performed in %4.2f seconds.\n",
        3 * numInPositions + 2 * numDups, (randomLookupTime + dupLookupTime) / 1000000000.0);

  }

  @Test
  @Order(2)
  void testSpeed() {
    runSpeedTest(800000, 800000, 800000);
    System.out.println("\nDOUBLING OPERATIONS AND REPEATING TIMING...\n");
    runSpeedTest(1600000, 1600000, 1600000);

  }

}
