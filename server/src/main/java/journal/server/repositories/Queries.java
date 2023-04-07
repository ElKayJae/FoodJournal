package journal.server.repositories;

public class Queries {
    public static final String SQL_FIND_BY_EMAIL = "select * from users where email=?";
    public static final String SQL_FIND_TARGET_CALORIE_BY_EMAIL = "select target from users where email=?";
    public static final String SQL_FIND_DAY_BY_DAY_ID = "select calories from days where day_id=?";
    public static final String SQL_REGISTER_USER = "insert into users(user_id, name, email, password, target, role) values (?,?,?,?,?,?)";
    public static final String SQL_INSERT_NEW_DAY = "insert into days(day_id, day, calories, user_id) values (?,?,?,?)";
    public static final String SQL_FIND_DAYS_BY_EMAIL = "select day_id, day, calories from days join users on days.user_id = users.user_id where users.email=? and day between ? and ?";
    public static final String SQL_FIND_DAY_BY_EMAIL_AND_DAY = "select day_id from days join users on days.user_id = users.user_id where users.email=? and day=?";
    public static final String SQL_UPDATE_DAY_CALORIES = "update days set calories=? where day_id=?";
}
