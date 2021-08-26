package system;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class Machine {

    private Outlets outlets;

    @JsonProperty("total_items_quantity")
    private Map<String, Integer> items_quantity;

    @JsonProperty("beverages")
    private Map<String, Map<String, Integer>> beverages;

    public Outlets getOutlets() {
        return outlets;
    }

    public Map<String, Integer> getItems_quantity() {
        return items_quantity;
    }

    public Map<String, Map<String, Integer>> getBeverages() {
        return beverages;
    }


}
