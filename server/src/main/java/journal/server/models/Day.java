package journal.server.models;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Day {
    private Meal[] mealList;
    private String dayId;
    private Date date;
    private BigDecimal calories;

    public static Day createDay(SqlRowSet rs){
        Day day = new Day();
        day.setDayId(rs.getString("day_id"));
        day.setCalories(rs.getBigDecimal("calories"));
        day.setDate(rs.getDate("day"));

        return day;
    }

    public static JsonObject toJson(Day day){
        return Json.createObjectBuilder()
            .add("day_id", day.dayId)
            .add("calories", day.calories)
            .add("date", day.date.toString())
            .build();
    }

}
