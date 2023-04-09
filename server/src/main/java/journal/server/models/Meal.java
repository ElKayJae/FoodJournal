package journal.server.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Meal {
    private String meal_id;
    private FoodData[] foodlist;
    private Date timestamp;
    private Double calories;
    private String category;
    private String imageurl;


    public static Document toDocument(Meal meal){
        Document d = new Document();
        d.put("meal_id", meal.getMeal_id());
        d.put("timestamp", meal.getTimestamp());
        d.put("category", meal.getCategory());
        d.put("calories",meal.calculateCalories());
        if (meal.getImageurl() != null ){
            d.put("imageurl", meal.getImageurl());
        }
        List<Document> documentList = new ArrayList<>();
        FoodData[] mealList = meal.getFoodlist();
        for (FoodData data: mealList){
            documentList.add(FoodData.toDocument(data));
        }
        d.put("foodlist", documentList);

        return d;
    }


    public static Meal createMealFromDocument(Document d){
        Meal meal = new Meal();
        meal.setMeal_id(d.getString("meal_id"));
        meal.setCategory(d.getString("category"));
        if (null != d.getString("imageurl"))
            meal.setImageurl(d.getString("imageurl"));
        meal.setTimestamp(d.getDate("timestamp"));
        meal.setCalories(d.getDouble("calories"));
        List<Document> foodListDoc = d.getList("foodlist", Document.class);
        meal.setFoodlist(FoodData.fromDocumentList(foodListDoc));

        return meal;
    }

    public static JsonObject toJson(Meal meal){
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        FoodData[] foodList = meal.getFoodlist();
        for (FoodData d: foodList){
            arrayBuilder.add(FoodData.toJson(d));
        }

        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("meal_id", meal.getMeal_id());
        builder.add("category", meal.getCategory());
        if (null != meal.getImageurl())
            builder.add("imageurl", meal.getImageurl());
        builder.add("calories", meal.getCalories());
        builder.add("foodlist", arrayBuilder.build());
        builder.add("timestamp", meal.getTimestamp().toString());

        return builder.build();
    }

    
    public Double calculateCalories(){
        Double calories = 0d;
        for (FoodData food: this.foodlist)
            calories += food.getCalories();
        return calories;
    }
}
