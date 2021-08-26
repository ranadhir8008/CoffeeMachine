package action;

import com.fasterxml.jackson.databind.ObjectMapper;
import system.Beverage;
import system.MachineDetails;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.*;

/*
  CoffeeMachine gets equipped with provided json.
        --Updates the Ingredient Manager
        --Request and serve the beverages using multithreading and synchronizing the ingredients.

   ALGORITHM
        -- Using the multithreaded fixed number of threads and reusing them if needed, for requesting the beverages
           by checking and updating the sufficient ingredients in the machine.

 */
public class CoffeeMachine {

    private ThreadPoolExecutor executor;
    private MachineDetails machineDetails;
    private IngredientManager ingredientManager;

    //Initialize the MachineDetails pojo using the given json
    public void startMachine(String jsonInput) throws IOException, InterruptedException {
        this.machineDetails = new ObjectMapper().readValue(jsonInput, MachineDetails.class);
        this.updateIngredients();
    }

    //update the Ingredients Manager by filling the giving ingredients
    private void updateIngredients(){
        this.ingredientManager = IngredientManager.getInstance();
        Map<String, Integer> ingredients = machineDetails.getMachine().getItems_quantity();
        this.ingredientManager.addBeverageIngredients(ingredients);
    }

    //Initializes the threads for requesting the beverages
    public void serve() throws InterruptedException {
        int count = machineDetails.getMachine().getOutlets().getCount();
        executor = new ThreadPoolExecutor(count, count, 3000L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1000), new RejectedHandler());
        Map<String, Map<String, Integer>> beverages = machineDetails.getMachine().getBeverages();
        for (String beverageName : beverages.keySet()) {
            Beverage beverage = new Beverage(beverageName, beverages.get(beverageName));
            this.requestBeverage(beverage);
        }
        awaitTerminationAfterShutdown(executor);
    }

    //Request the beverage by executing runnable class BeverageManager
    private void requestBeverage(Beverage beverage) throws InterruptedException {
        BeverageManager brew = new BeverageManager(beverage);
        executor.execute(brew);
    }

    public void awaitTerminationAfterShutdown(ExecutorService threadPool) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public MachineDetails getMachineDetails() {
        return machineDetails;
    }
}