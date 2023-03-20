package journal.server.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import journal.server.models.User;
import static journal.server.repositories.Queries.*;

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

    public void registerUser(User user){
        String userId = UUID.randomUUID().toString().substring(0,8);
        template.update(SQL_REGISTER_USER, userId,  user.getName(), user.getEmail(), user.getPassword(), user.getRole().name());
    }


}
