package action;

import system.Beverage;

//Runnable class for checking and updating the ingredients in a thread safe manner.
public class BeverageManager implements Runnable {

    private Beverage beverage;

    BeverageManager(Beverage beverage) {
        this.beverage = beverage;
    }

    @Override
    public  void run() {
        manageBeverages();
    }

    private synchronized void manageBeverages() {
        IngredientManager ingredientManager = IngredientManager.getInstance();
        String deficientIngredient = ingredientManager.insufficientIngredient(beverage);
        if(deficientIngredient == null){
            System.out.println(beverage.getBeverageName()+" is ready");
            ingredientManager.updateStock(beverage);
        }
        else {
            System.out.println(beverage.getBeverageName()+" is not ready because "+ deficientIngredient+" is not available");
        }
    }

    @Override
    public String toString() {
        return "beverage=" + beverage.getBeverageName();
    }

}
