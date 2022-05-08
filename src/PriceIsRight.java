import java.util.*;

public class PriceIsRight {

  private TreeMap<Double, String> priceList;


  public PriceIsRight() {
    this.priceList = new TreeMap<>();
  }

  public void addProduct(double price, String product) {
    priceList.put(price, product);
  }

  public String buy(double price) {
    Map.Entry<Double, String> item = priceList.floorEntry(price);
    if (item == null) {
      return null;
    }
    String name = item.getValue();
    priceList.remove(item.getKey());
    return name;
  }

}
