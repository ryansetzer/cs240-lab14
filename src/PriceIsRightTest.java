import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

/**
 * Correctness and performance tests for PriceIsRight.
 * 
 * @author Nathan Sprague
 *
 */
@TestMethodOrder(OrderAnnotation.class)
class PriceIsRightTest {

  /**
   * Generate a random list of doubles guaranteeing no repeats.
   * 
   * @param howMany how many to create
   * @param avoid don't include any from this list
   * @return howMany doubles
   */
  public List<Double> randomDoubles(int howMany, List<Double> avoid) {
    List<Double> result = new ArrayList<>();
    Random gen = new Random(100101);
    HashSet<Double> seen = new HashSet<>();
    HashSet<Double> avoidSet = new HashSet<>(avoid);

    while (result.size() < howMany) {
      double price = gen.nextDouble();

      if (!seen.contains(price) && !avoidSet.contains(price)) {
        seen.add(price);
        result.add(price);
      }
    }
    return result;
  }



  @Test
  @Order(1)
  void testFunctionality() {
    PriceIsRight pricer = new PriceIsRight();

    assertEquals(null, pricer.buy(50.0));

    pricer.addProduct(5000.0, "vacation");
    pricer.addProduct(20.0, "blender");
    pricer.addProduct(40.0, "vacuum");
    pricer.addProduct(60.0, "watch");

    assertEquals("vacuum", pricer.buy(50.0));
    assertEquals("blender", pricer.buy(50.0));
    assertEquals(null, pricer.buy(50.0));

    pricer.addProduct(25000.0, "car");
    assertEquals("vacation", pricer.buy(10000.0));
    assertEquals("watch", pricer.buy(10000.0));
    assertEquals(null, pricer.buy(10000.0));

  }

  private void timePerformance(int fillAmount, int lookupAmount) {
    long addTime;
    long buyTime;

    PriceIsRight pricer = new PriceIsRight();

    System.out.println("GENERATING RANDOM DATA...");

    // A bunch of random doubles, not including (0, 0)
    List<Double> initialPrices = randomDoubles(fillAmount, List.of());
    List<Double> dynamicPrices = randomDoubles(lookupAmount, initialPrices);
    List<Double> searchPrices = randomDoubles(lookupAmount, List.of());

    System.out.println("TIMING LONG SEQUENCE OF ADDS...");
    long start = System.nanoTime();
    for (int i = 0; i < initialPrices.size(); i++) {
      pricer.addProduct(initialPrices.get(i), "Product_" + i);
    }
    addTime = System.nanoTime() - start;

    System.out.println("TIMING ALTERNATING ADDS AND LOOKUPS...");
    start = System.nanoTime();
    for (int i = 0; i < lookupAmount; i++) {
      pricer.addProduct(dynamicPrices.get(i), "Product_" + i);
      pricer.buy(searchPrices.get(i));
    }
    buyTime = System.nanoTime() - start;

    System.out.printf("\nTOTAL TIME: %.2f seconds.\n", (addTime + buyTime) / 1000000000.0);

  }

  @Test
  @Order(2)
  void testPerformance() {

    timePerformance(400000, 400000);

    System.out.println("\nDOUBLING THE NUMBER OF OPERATIONS...\n");

    timePerformance(800000, 800000);

  }

}
