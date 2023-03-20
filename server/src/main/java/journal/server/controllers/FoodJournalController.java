package journal.server.controllers;

import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import journal.server.config.JwtService;
import journal.server.models.Meal;
import journal.server.services.FoodDataService;
import journal.server.services.ImageService;
import journal.server.services.UserService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(path = "/api")
@RequiredArgsConstructor
public class FoodJournalController {
    
    private final FoodDataService foodDataService;
    private final JwtService jwtService;
    private final ImageService imageService;
    private final UserService userService;

    @ResponseBody
    @GetMapping (path= "/search")
    public ResponseEntity<String> getFoodData(@RequestParam(required=true) String query){

        Optional<JsonArray> opt = foodDataService.getFoodDataList(query);
        JsonArray arr = opt.get();

        if (opt.isEmpty()) {
            JsonObject error = Json.createObjectBuilder().add("error", "failed to retrieve foods").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error.toString());
        }

        return ResponseEntity.ok(arr.toString());

    }

    @ResponseBody
    @GetMapping (path= "/loadpage")
    public ResponseEntity<String> test(@RequestHeader HttpHeaders header){
        String value = header.getFirst("Authorization").substring(7);
        String username = jwtService.extractUsername(value);
        System.out.printf("%s authenticated\n", username);
        return ResponseEntity.ok().build();
    }
    
    @ResponseBody
    @PostMapping (path = "/addmeal")
    public ResponseEntity<String> addMeal(@RequestHeader HttpHeaders header, @RequestBody Meal meal){
        String value = header.getFirst("Authorization").substring(7);
        System.out.println(meal.getCategory());
        System.out.println(meal.getTimestamp());
        System.out.println(meal.getMeal_id());
        System.out.println(meal.getImageurl());
        System.out.println("Test~~~~~~~~~~~~~~~~~~~~");
        userService.insertMeal(meal);
        
        return ResponseEntity.ok().build();
    }
    
    @ResponseBody
    @PostMapping (path = "/uploadimage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImage(@RequestHeader HttpHeaders header, @RequestPart MultipartFile image){

        Optional<Meal> opt = imageService.uploadImage(image);
        if (opt.isEmpty()) 
            return ResponseEntity.internalServerError().body(Json.createObjectBuilder()
                    .add("error", "failed to upload image").build().toString());

        Meal meal = opt.get();
        return ResponseEntity.ok().body(Json.createObjectBuilder()
            .add("meal_id", meal.getMeal_id()).add("image_url", meal.getImageurl()).build().toString());
    }
    
}
