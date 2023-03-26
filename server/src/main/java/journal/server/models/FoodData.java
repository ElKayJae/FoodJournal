package journal.server.models;

import java.math.BigDecimal;
import java.util.List;

import org.bson.Document;
import org.bson.types.Decimal128;
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
    private Double calories;
    private Double protein;
    private Double carbohydrates;
    private Double fat;

    public static FoodData createFoodData(JsonObject o) {

        logger.info("createJson fooddata");
        FoodData foodData = new FoodData();
        foodData.setName(o.getString("name"));
        foodData.setWeight(o.getInt("serving_size_g"));
        foodData.setCalories(o.getJsonNumber("calories").doubleValue());
        foodData.setProtein(o.getJsonNumber("protein_g").doubleValue());
        foodData.setCarbohydrates(o.getJsonNumber("carbohydrates_total_g").doubleValue());
        foodData.setFat(o.getJsonNumber("fat_total_g").doubleValue());
        
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

    public static FoodData fromDocument(Document d){
        FoodData f = new FoodData();
        f.setCalories(d.getDouble("calories"));
        f.setCarbohydrates(d.getDouble("carbohydrates"));
        f.setFat(d.getDouble("fat"));
        f.setName(d.getString("name"));
        f.setProtein(d.getDouble("protein"));
        f.setWeight(d.getInteger("weight"));

        return f;
    }

    public static FoodData[] fromDocumentList(List<Document> docList){
        FoodData[] foodList = new FoodData[docList.size()];
        for (int i = 0; i < foodList.length; i++) {
            foodList[i] = FoodData.fromDocument(docList.get(i));
        }
        return foodList;
    }

    public static JsonObject toJson(FoodData foodData){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("name", foodData.getName());
        builder.add("calories", foodData.getCalories());
        builder.add("carbohydrates", foodData.getCarbohydrates());
        builder.add("fat", foodData.getFat());
        builder.add("protein", foodData.getProtein());
        builder.add("weight", foodData.getWeight());

        return builder.build();
    }
  
}
