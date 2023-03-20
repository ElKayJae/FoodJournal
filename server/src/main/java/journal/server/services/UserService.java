package journal.server.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Optional<User> findUserByEmail(String email){
        return sqlRepository.findUserByEmail(email);
    }

    public void registerUser(User user){
        sqlRepository.registerUser(user);
    }

    public void insertMeal(Meal meal){
        //delete image if failed to insert into mongo
        try {
            mongoRepository.insertMeal(meal);
        } catch (Exception e) {
            imageRepository.deleteImage(meal.getMeal_id());
        }
    }
}
