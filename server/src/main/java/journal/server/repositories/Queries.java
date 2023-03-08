package journal.server.repositories;

public class Queries {
    public static final String SQL_FIND_BY_EMAIL = "select * from users where email=?";
    public static final String SQL_REGISTER_USER = "insert into users(user_id, name, email, password) values (?,?,?,?)";
}
