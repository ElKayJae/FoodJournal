package journal.server.models;

import java.math.BigDecimal;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.json.Json;
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
public class FoodData {

    private static final Logger logger = LoggerFactory.getLogger(FoodData.class);

    private String name;
    private Integer weight;
    private BigDecimal calories;
    private BigDecimal protein;
    private BigDecimal carbohydrates;
    private BigDecimal fat;

    public static FoodData createFoodData(JsonObject o) {

        logger.info("createJson fooddata");
        FoodData foodData = new FoodData();
        foodData.setName(o.getString("name"));
        foodData.setWeight(o.getInt("serving_size_g"));
        foodData.setCalories(o.getJsonNumber("calories").bigDecimalValue());
        foodData.setProtein(o.getJsonNumber("protein_g").bigDecimalValue());
        foodData.setCarbohydrates(o.getJsonNumber("carbohydrates_total_g").bigDecimalValue());
        foodData.setFat(o.getJsonNumber("fat_total_g").bigDecimalValue());
        
        return foodData;
    }
    
    public static JsonObject toJsonObject(FoodData data) {

        JsonObjectBuilder builder = Json.createObjectBuilder()
            .add("name", data.getName())
            .add("weight", data.getWeight())
            .add("calories", data.getCalories())
            .add("protein", data.getProtein())
            .add("carbohydrates", data.getCarbohydrates())
            .add("fat", data.getFat());

        return builder.build();
    }
    
    public static Document toDocument(FoodData data){

        Document d = new Document();
        d.put("name", data.getName());
        d.put("weight", data.getWeight());
        d.put("calories", data.getCalories());
        d.put("protein", data.getProtein());
        d.put("carbohydrates", data.getCarbohydrates());
        d.put("fat", data.getFat());

        return d;
    }
    
}
