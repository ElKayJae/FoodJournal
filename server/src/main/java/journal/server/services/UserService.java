package journal.server.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import journal.server.models.Day;
import journal.server.models.Meal;
import journal.server.models.User;
import journal.server.repositories.ImageRepository;
import journal.server.repositories.MongoRepository;
import journal.server.repositories.SQLRepository;

@Service
public class UserService {
    
    @Autowired
    SQLRepository sqlRepository;

    @Autowired
    MongoRepository mongoRepository;

    @Autowired
    ImageRepository imageRepository;


    public Optional<User> findUserByEmail(String email) {
        return sqlRepository.findUserByEmail(email);
    }


    public Optional<String> findDayByEmailAndDay(String email, String date) {
        return sqlRepository.findDayByEmailAndDay(email, date);
    }


    public Optional<Integer> findTargetCalorieByEmail(String email) {
        return sqlRepository.findTargetCalorieByEmail(email);
    }


    public void registerUser(User user) {
        sqlRepository.registerUser(user);
    }


    @Transactional
    public void insertMeal(Meal meal, String dayid, String email) throws Exception {
        if (dayid.equals("")){
            dayid = UUID.randomUUID().toString().substring(0,8);
            sqlRepository.insertNewDay(dayid, meal, email);
        } else {
            int target = sqlRepository.findTargetCalorieByEmail(email).get();
            Double intialCalories = sqlRepository.findCaloriesByDayId(dayid);
            Double newCalories = sqlRepository.addCaloriesToDay(dayid, meal, intialCalories);

            if (intialCalories < target && newCalories > target) System.out.println("exceeded calories");
        }
            mongoRepository.insertMeal(meal, dayid);
    }


    @Transactional
    public void deleteMeal(String meal_id, Double calories, String day_id){
        sqlRepository.removeCaloriesFromDay(day_id, calories);
        mongoRepository.deleteMeal(meal_id);
        imageRepository.deleteImage(meal_id);
    }


    public void deleteDay(String day_id){
        sqlRepository.deleteDay(day_id);
    }


    public void deleteImage(String id ){
        imageRepository.deleteImage(id);
    }


    public Optional<JsonArray> findDaysByEmail(String email, String startDate, String endDate) {
        Optional<List<Day>> opt = sqlRepository.findDaysByEmail(email, startDate, endDate);
        if (opt.isEmpty()) return Optional.empty();
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        List<Day> dayList = opt.get();
        for (Day day : dayList){
            arrBuilder.add(Day.toJson(day));
        }
        return Optional.of(arrBuilder.build());
    }


    public JsonArray getMealsByDayId(String day_id){
        return mongoRepository.getMealsByDayId(day_id);
    }

    
}
