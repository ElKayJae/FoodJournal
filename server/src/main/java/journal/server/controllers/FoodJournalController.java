package journal.server.controllers;

import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
import journal.server.services.Utilities;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(path = "/api")
@RequiredArgsConstructor
public class FoodJournalController {
    
    private final FoodDataService foodDataService;
    private final JwtService jwtService;
    private final ImageService imageService;
    private final UserService userService;
    private final Utilities utilsService;

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
    public ResponseEntity<String> loadPage(@RequestHeader HttpHeaders header){
        String value = header.getFirst("Authorization").substring(7);
        String username = jwtService.extractUsername(value);
        System.out.printf("%s authenticated\n", username);
        return ResponseEntity.ok().build();
    }


    @ResponseBody
    @GetMapping (path= "/target")
    public ResponseEntity<String> getTargetCalorie(@RequestHeader HttpHeaders header){
        String value = header.getFirst("Authorization").substring(7);
        String username = jwtService.extractUsername(value);
        Optional<Integer> opt = userService.findTargetCalorieByEmail(username);
        System.out.println(opt.get());
        return ResponseEntity.ok().body(Json.createObjectBuilder().add("target", opt.get()).build().toString());
    }


    @ResponseBody
    @PutMapping (path= "/target")
    public ResponseEntity<String> updateTargetCalorie(@RequestHeader HttpHeaders header, @RequestParam Integer target ){
        String value = header.getFirst("Authorization").substring(7);
        String username = jwtService.extractUsername(value);
        boolean updated = userService.updateTargetCalorieByEmail(username, target);
        if (!updated) 
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Json.createObjectBuilder().add("error", "unable to update").build().toString());
        return ResponseEntity.ok().body(Json.createObjectBuilder().add("message", "updated").build().toString());
    }


    @ResponseBody
    @GetMapping (path= "/getdays")
    public ResponseEntity<String> getDays(@RequestHeader HttpHeaders header, @RequestParam String start, @RequestParam String end){
        String value = header.getFirst("Authorization").substring(7);
        String username = jwtService.extractUsername(value);
        Optional<JsonArray> opt = userService.findDaysByEmail(username, start, end);
        if (opt.isEmpty()){
            JsonObject error = Json.createObjectBuilder().add("error", "no data").build();
            return ResponseEntity.status(HttpStatus.OK).body(error.toString());
        }
        return ResponseEntity.ok().body(opt.get().toString());
    }


    @ResponseBody
    @GetMapping (path= "/searchday")
    public ResponseEntity<String> searchdayResponseEntity(@RequestHeader HttpHeaders header, @RequestParam String date){
        String value = header.getFirst("Authorization").substring(7);
        String username = jwtService.extractUsername(value);
        Optional<String> opt = userService.findDayByEmailAndDay(username, date);
        String dayId = "";
        if (opt.isPresent()) dayId = opt.get();
        JsonObject resp = Json.createObjectBuilder().add("day_id", dayId).build();
        return ResponseEntity.status(HttpStatus.OK).body(resp.toString());
    }
    

    @ResponseBody
    @GetMapping (path = "/getmeals")
    public ResponseEntity<String> getMealsByDayId(@RequestHeader HttpHeaders header, @RequestParam String day_id){
        JsonArray arr = userService.getMealsByDayId(day_id);
        return ResponseEntity.ok().body(arr.toString());
    }


    @ResponseBody
    @PostMapping (path = "/addmeal")
    public ResponseEntity<String> addMeal(@RequestHeader HttpHeaders header, @RequestBody Meal meal, @RequestParam String day_id){
        String value = header.getFirst("Authorization").substring(7);
        String username = jwtService.extractUsername(value);
        try {
            userService.insertMeal(meal, day_id, username);
        } catch (Exception e) {
            userService.deleteImage(meal.getMeal_id());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(Json.createObjectBuilder().add("day_id", day_id).build().toString());
    }


    @ResponseBody
    @DeleteMapping (path = "/deletemeal")
    public ResponseEntity<String> deleteMeal(@RequestParam String meal_id, @RequestParam Double calories, @RequestParam String day_id){
        userService.deleteMeal(meal_id, calories, day_id);
        return ResponseEntity.ok().body(Json.createObjectBuilder().add("meal_id", meal_id).build().toString());
    }


    @ResponseBody
    @DeleteMapping (path = "/deleteday")
    public ResponseEntity<String> deleteDay(@RequestParam String day_id){
        userService.deleteDay(day_id);
        return ResponseEntity.ok().body(Json.createObjectBuilder().add("message", "deleted").build().toString());
    }
    

    @ResponseBody
    @PostMapping (path = "/uploadimage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImage(@RequestHeader HttpHeaders header, @RequestPart(required = false) MultipartFile image){
        if (image != null){
            Optional<Meal> opt = imageService.uploadImage(image);
            if (opt.isEmpty()) 
                return ResponseEntity.internalServerError().body(Json.createObjectBuilder()
                        .add("error", "failed to upload image").build().toString());
    
            Meal meal = opt.get();
            return ResponseEntity.ok().body(Json.createObjectBuilder()
                .add("meal_id", meal.getMeal_id()).add("image_url", meal.getImageurl()).build().toString());
        }
        return ResponseEntity.ok().body(Json.createObjectBuilder()
        .add("meal_id", utilsService.generateUUID()).build().toString());
    }


}
