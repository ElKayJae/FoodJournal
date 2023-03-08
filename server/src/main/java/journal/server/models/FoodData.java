package journal.server.models;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;

public class FoodData {

    private static final Logger logger = LoggerFactory.getLogger(FoodData.class);

    private String name;
    private Integer weight;
    private BigDecimal calories;
    private BigDecimal protein;
    private BigDecimal carbs;
    private boolean checked=false;

    public boolean isChecked() {
        return checked;
    }
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    private static ArrayList<FoodData> foodList = new ArrayList<>();

    public static ArrayList<FoodData> getFoodList() {
        return foodList;
    }
    public static void setFoodList(ArrayList<FoodData> foodList) {
        FoodData.foodList = foodList;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getWeight() {
        return weight;
    }
    public void setWeight(Integer weight) {
        this.weight = weight;
    }
    public BigDecimal getCalories() {
        return calories;
    }
    public void setCalories(BigDecimal calories) {
        this.calories = calories;
    }
    public BigDecimal getProtein() {
        return protein;
    }
    public void setProtein(BigDecimal protein) {
        this.protein = protein;
    }
    public BigDecimal getCarbs() {
        return carbs;
    }
    public void setCarbs(BigDecimal carbs) {
        this.carbs = carbs;
    }

    public static FoodData createJson(JsonObject o) throws IOException {

        logger.info("createJson fooddata");
        FoodData foodData = new FoodData();
        String nameStr = o.getString("name");
        foodData.name = nameStr;
        logger.info(nameStr);
        JsonNumber weightNum = o.getJsonNumber("serving_size_g");
        foodData.weight = weightNum.intValue();
        logger.info(foodData.weight.toString());
        JsonNumber caloriesNum = o.getJsonNumber("calories");
        foodData.calories = caloriesNum.bigDecimalValue();
        JsonNumber proteinNum = o.getJsonNumber("protein_g");
        foodData.protein = proteinNum.bigDecimalValue();
        JsonNumber carbsNum = o.getJsonNumber("carbohydrates_total_g");
        foodData.carbs = carbsNum.bigDecimalValue();
        return foodData;
    }
    
    
}
