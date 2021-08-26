package action;

import system.Beverage;
import java.util.HashMap;
import java.util.Map;

//Ingredient Manager, a singleton class acts as a core class for updating the ingredients stock in the machine.
//followed thread safety to synchronize between the threads as every thread is accessing the common space of ingredients.
public class IngredientManager {

    //IngredientStock is the actual mapper of the ingredients in the machine that we use for requesting beverages.
    private HashMap<String, Integer> ingredientsStock = new HashMap<>();

    private static IngredientManager instance;
    private IngredientManager() {
    }

    //initialize the singleton class
    public synchronized static IngredientManager getInstance() {
        if (instance == null) {
            instance = new IngredientManager();
        }
        return instance;
    }

    //update the stock in the ingredient stock
    public synchronized void updateStock(Beverage beverage) {
        Map<String, Integer> beverageIngredients = beverage.getBeverageIngredients();
        for(String ingredient: beverageIngredients.keySet()){
            int previousQuantity = ingredientsStock.getOrDefault(ingredient, 0);
            ingredientsStock.put(ingredient, previousQuantity - beverageIngredients.get(ingredient));
        }
    }

    public void addBeverageIngredients(Map<String, Integer> ingredients) {
        for (String ingredient : ingredients.keySet()) {
            addIngredient(ingredient, ingredients.get(ingredient));
        }
    }

    public void addIngredient(String ingredient, Integer quantity) {
            int previousQuantity = ingredientsStock.getOrDefault(ingredient, 0);
            ingredientsStock.put(ingredient, previousQuantity + quantity);
    }

    //check the adequate quantity of ingredients and send the missing ingredient.
    public synchronized String insufficientIngredient(Beverage beverage) {
        Map<String, Integer> beverageIngredients = beverage.getBeverageIngredients();
        for(String ingredient: beverageIngredients.keySet()) {
            if (beverageIngredients.get(ingredient) > ingredientsStock.getOrDefault(ingredient, 0)) {
                return ingredient;
            }
        }
        return null;
    }

    public void clear() {
        ingredientsStock.clear();
    }

    public Map<String, Integer> getIngredientsStock() {
        return ingredientsStock;
    }

}
