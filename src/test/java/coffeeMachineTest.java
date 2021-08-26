import action.IngredientManager;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import action.CoffeeMachine;
import system.Beverage;

import java.io.File;

public class coffeeMachineTest {
    IngredientManager ingredientManager = null;
    CoffeeMachine coffeeMachine = null;
    @Before
    public void setUp() {
        coffeeMachine = new CoffeeMachine();
        ingredientManager = ingredientManager.getInstance();
    }

    @After
    public void down() {
        ingredientManager.getInstance().clear();
        coffeeMachine = null;
        System.out.println();
    }

    //Test with medium quantity ingredients
    @Test
    public void testValid() throws Exception {
        final String filePath = "machine.json";
        File file = new File(CoffeeMachine.class.getClassLoader().getResource(filePath).getFile());
        String jsonInput = FileUtils.readFileToString(file, "UTF-8");
        coffeeMachine.startMachine(jsonInput);

        Assert.assertEquals(500,(int)coffeeMachine.getMachineDetails().getMachine().getItems_quantity().get("hot_water"));
        Assert.assertEquals(500,(int)ingredientManager.getIngredientsStock().get("hot_water"));

        Assert.assertNull(ingredientManager.insufficientIngredient(getBeverage(coffeeMachine, "hot_tea")));
        Assert.assertEquals("green_mixture",ingredientManager.insufficientIngredient(getBeverage(coffeeMachine, "green_tea")));

        coffeeMachine.serve();

        Assert.assertNotEquals(500,(int)ingredientManager.getIngredientsStock().get("hot_water"));
        Assert.assertEquals(4,coffeeMachine.getMachineDetails().getMachine().getOutlets().getCount());
    }

    //Test with many outlets
    @Test
    public void testHighOutlets() throws Exception {
        final String filePath = "machine2.json";
        File file = new File(CoffeeMachine.class.getClassLoader().getResource(filePath).getFile());
        String jsonInput = FileUtils.readFileToString(file, "UTF-8");
        coffeeMachine.startMachine(jsonInput);

        Assert.assertEquals(100,(int)coffeeMachine.getMachineDetails().getMachine().getItems_quantity().get("sugar_syrup"));
        Assert.assertEquals(100,(int)ingredientManager.getIngredientsStock().get("sugar_syrup"));

        Assert.assertNull(ingredientManager.insufficientIngredient(getBeverage(coffeeMachine, "black_tea")));
        Assert.assertEquals("hot_milk",ingredientManager.insufficientIngredient(getBeverage(coffeeMachine, "hot_coffee")));

        coffeeMachine.serve();

        Assert.assertNotEquals(100,(int)ingredientManager.getIngredientsStock().get("sugar_syrup"));
        Assert.assertEquals(7,coffeeMachine.getMachineDetails().getMachine().getOutlets().getCount());
    }

    //Test with high quantity ingredients
    @Test
    public void testHighQuantity() throws Exception {
        final String filePath = "machine3.json";
        File file = new File(CoffeeMachine.class.getClassLoader().getResource(filePath).getFile());
        String jsonInput = FileUtils.readFileToString(file, "UTF-8");
        coffeeMachine.startMachine(jsonInput);

        Assert.assertEquals(10000,(int)coffeeMachine.getMachineDetails().getMachine().getItems_quantity().get("tea_leaves_syrup"));
        Assert.assertEquals(10000,(int)ingredientManager.getIngredientsStock().get("tea_leaves_syrup"));

        Assert.assertNull(ingredientManager.insufficientIngredient(getBeverage(coffeeMachine, "hot_tea")));
        Assert.assertNull(ingredientManager.insufficientIngredient(getBeverage(coffeeMachine, "green_tea")));

        coffeeMachine.serve();

        Assert.assertNotEquals(10000,(int)ingredientManager.getIngredientsStock().get("tea_leaves_syrup"));
        Assert.assertEquals(5,coffeeMachine.getMachineDetails().getMachine().getOutlets().getCount());
    }

    //Test with no ingredients
    @Test
    public void testNoQuantity() throws Exception {
        final String filePath = "machine4.json";
        File file = new File(CoffeeMachine.class.getClassLoader().getResource(filePath).getFile());
        String jsonInput = FileUtils.readFileToString(file, "UTF-8");
        coffeeMachine.startMachine(jsonInput);

        Assert.assertNull(coffeeMachine.getMachineDetails().getMachine().getItems_quantity().get("hot_milk"));
        Assert.assertNull(ingredientManager.getIngredientsStock().get("hot_milk"));

        Assert.assertEquals("hot_water",ingredientManager.insufficientIngredient(getBeverage(coffeeMachine, "hot_coffee")));
        Assert.assertEquals("hot_water",ingredientManager.insufficientIngredient(getBeverage(coffeeMachine, "black_tea")));

        coffeeMachine.serve();

        Assert.assertNull(ingredientManager.getIngredientsStock().get("hot_milk"));
        Assert.assertEquals(2,coffeeMachine.getMachineDetails().getMachine().getOutlets().getCount());
    }

    //Test with medium quantity ingredients
    //Test for adding ingredient and serving again
    @Test
    public void testByAddingIngredients() throws Exception {
        final String filePath = "machine5.json";
        File file = new File(CoffeeMachine.class.getClassLoader().getResource(filePath).getFile());
        String jsonInput = FileUtils.readFileToString(file, "UTF-8");
        coffeeMachine.startMachine(jsonInput);

        Assert.assertEquals(3,coffeeMachine.getMachineDetails().getMachine().getOutlets().getCount());

        Assert.assertEquals(100,(int)coffeeMachine.getMachineDetails().getMachine().getItems_quantity().get("ginger_syrup"));
        Assert.assertEquals(100,(int)ingredientManager.getIngredientsStock().get("ginger_syrup"));

        Assert.assertNull(ingredientManager.insufficientIngredient(getBeverage(coffeeMachine, "hot_coffee")));
        Assert.assertEquals("ginger_syrup", ingredientManager.insufficientIngredient(getBeverage(coffeeMachine, "black_tea")));

        coffeeMachine.serve();

        Assert.assertNotEquals(100, (int)ingredientManager.getIngredientsStock().get("ginger_syrup"));
        Assert.assertEquals(3,coffeeMachine.getMachineDetails().getMachine().getOutlets().getCount());

        ingredientManager.addIngredient("ginger_syrup", ingredientManager.getIngredientsStock().getOrDefault("ginger_syrup",0)+1000);

        Assert.assertNull(ingredientManager.insufficientIngredient(getBeverage(coffeeMachine, "black_tea")));
        System.out.println();
        coffeeMachine.serve();
    }


    private Beverage getBeverage(CoffeeMachine coffeeMachine, String beverageName) {
        return new Beverage(beverageName, coffeeMachine.getMachineDetails().getMachine().getBeverages().get(beverageName));
    }
}