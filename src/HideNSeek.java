import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class HideNSeek {

  private HashMap<Point, TreeSet<String>> locations;

  public HideNSeek() {
    locations = new HashMap<>();
  }

  public void hide(int x, int y, String item) {
    TreeSet<String> items = locations.get(new Point(x, y));
    // If location already exists
    if (items != null) {
      if (!items.contains(item)) {
        items.add(item);
      }
      // If location does not exist
    } else {
      Point point = new Point(x, y);
      TreeSet<String> placedItems = new TreeSet<>();
      placedItems.add(item);
      locations.put(point, placedItems);
    }
  }

  public boolean seek(int x, int y, String item) {
    return locations.get(new Point(x, y)) != null && locations.get(new Point(x, y)).contains(item);
  }


}
