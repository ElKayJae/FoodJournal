package journal.server.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import journal.server.models.Day;
import journal.server.models.Meal;
import journal.server.models.User;
import static journal.server.repositories.Queries.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class SQLRepository {

    @Autowired
    JdbcTemplate template;

    public Optional<User> findUserByEmail(String email){
        SqlRowSet rs = template.queryForRowSet(SQL_FIND_BY_EMAIL, email);
        if (!rs.next()) return Optional.empty();
        User user = User.createUser(rs);

        return Optional.of(user);
    }

    
    public Optional<Integer> findTargetCalorieByEmail(String email){
        SqlRowSet rs = template.queryForRowSet(SQL_FIND_TARGET_CALORIE_BY_EMAIL, email);
        if (!rs.next()) return Optional.empty();
        int target =rs.getInt("target");

        return Optional.of(target);
    }

    public boolean updateTargetCalorieByEmail(String email, int target){
        int rows = template.update(SQL_UPDATE_TARGET_CALORIES, target, email);
        return rows > 0;
    }


    public void registerUser(User user){
        String userId = UUID.randomUUID().toString().substring(0,8);
        template.update(SQL_REGISTER_USER, userId,  user.getName(), user.getEmail(), user.getPassword(), user.getTarget(), user.getRole().name());
    }


    public void insertNewDay(String dayid, Meal meal, String email, String dayString){
        Optional<User> opt = findUserByEmail(email);
        template.update(SQL_INSERT_NEW_DAY, dayid, dayString, meal.calculateCalories(), opt.get().getUserId());
    }


    public Double findCaloriesByDayId(String dayId){
        SqlRowSet rs = template.queryForRowSet(SQL_FIND_DAY_BY_DAY_ID, dayId);
        rs.next();
        return rs.getDouble("calories");
    }


    public Double addCaloriesToDay(String dayId, Meal meal, Double intialCalories){
        Double newCalories = meal.calculateCalories() + intialCalories;
        template.update(SQL_UPDATE_DAY_CALORIES, newCalories, dayId);
        return newCalories;
    }


    public void removeCaloriesFromDay(String dayId, Double calories){
        template.update(SQL_UPDATE_DAY_CALORIES_REMOVE_MEAL, calories, dayId);
    }


    public void deleteDay(String dayId){
        template.update(SQL_DELETE_DAY_BY_DAY_ID, dayId);
    }


    public Optional<List<Day>> findDaysByEmail(String email, String startDate, String endDate){
        System.out.println(startDate + " " + endDate);
        SqlRowSet rs = template.queryForRowSet(SQL_FIND_DAYS_BY_EMAIL, email, startDate, endDate);
        if (!rs.next()) return Optional.empty();
        List<Day> dayList = new LinkedList<>();
        Day day = Day.createDay(rs);
        dayList.add(day);
        while (rs.next()){
            Day newDay = Day.createDay(rs);
            dayList.add(newDay);
        }
        return Optional.of(dayList);
    }


    public Optional<String> findDayByEmailAndDay(String email, String date){
        SqlRowSet rs = template.queryForRowSet(SQL_FIND_DAY_BY_EMAIL_AND_DAY, email, date);
        if (!rs.next()) return Optional.empty();
        String day_id = rs.getString("day_id");

        return Optional.of(day_id);
    }


}
