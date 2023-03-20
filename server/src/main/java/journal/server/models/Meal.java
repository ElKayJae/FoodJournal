package journal.server.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;

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
    private String category;
    private String imageurl;

    public static Document toDocument(Meal meal){
        Document d = new Document();
        d.put("meal_id", meal.getMeal_id());
        d.put("timestamp", meal.getTimestamp());
        d.put("category", meal.getCategory());
        d.put("imageurl", meal.getImageurl());
        List<Document> documentList = new ArrayList<>();
        FoodData[] mealList = meal.getFoodlist();
        for (FoodData data: mealList){
            documentList.add(FoodData.toDocument(data));
        }
        d.put("foodlist", documentList);

        return d;
    }

}
