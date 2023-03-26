package journal.server.repositories;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import journal.server.models.Meal;

@Repository
public class MongoRepository {

    private final String C_MEALS = "meals";
    
    @Autowired
    MongoTemplate template;

    public void insertMeal(Meal meal, String dayid) {
        Document toInsert = Meal.toDocument(meal);
        toInsert.put("day_id", dayid);
        template.insert(toInsert, C_MEALS);
    }

    public JsonArray getMealsByDayId(String dayid) {
        Criteria c = Criteria.where("day_id").is(dayid);
        Query q = Query.query(c);
        List<Document> docList = template.find(q, Document.class, C_MEALS);
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        for (Document d : docList){
            Meal m = Meal.createMealFromDocument(d);
            arrBuilder.add(Meal.toJson(m));
        }
        return arrBuilder.build();
    }
}
