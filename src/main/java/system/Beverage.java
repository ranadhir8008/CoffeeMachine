package system;

import java.util.Map;

public class Beverage {

    private String beverageName;

    private Map<String, Integer> beverageIngredients;

    public Beverage(String name, Map<String, Integer> beverageIngredients) {
        this.beverageName = name;
        this.beverageIngredients = beverageIngredients;
    }

    public Map<String, Integer> getBeverageIngredients() {
        return beverageIngredients;
    }

    public String getBeverageName() {
        return beverageName;
    }

}
