package journal.server.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import journal.server.models.Meal;
import journal.server.repositories.ImageRepository;

@Service
public class ImageService {
    
    @Autowired
    ImageRepository imageRepository;

    public Optional<Meal> uploadImage(MultipartFile file){

        String mealId = UUID.randomUUID().toString().substring(0,8);
        Meal meal = new Meal();
        meal.setMeal_id(mealId);
        try {
            meal.setImageurl(imageRepository.upload(file, mealId));
        } catch (Exception e) {
            return Optional.empty();
        }

        return Optional.of(meal);
    }
}
