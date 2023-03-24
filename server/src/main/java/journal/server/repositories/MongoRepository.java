package journal.server.repositories;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import journal.server.models.Meal;

@Repository
public class MongoRepository {

    private final String C_MEALS = "meals";
    
    @Autowired
    MongoTemplate template;

    public void insertMeal(Meal meal, String dayid){
        Document toInsert = Meal.toDocument(meal);
        toInsert.put("day_id", dayid);
        template.insert(toInsert, C_MEALS);
    }
}
