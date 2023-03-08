package journal.server.controllers;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import journal.server.services.FoodDataService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(path = "/api")
@RequiredArgsConstructor
public class FoodJournalController {
    
    private final FoodDataService foodDataService;

    @ResponseBody
    @GetMapping (path= "/search")
    public ResponseEntity<String> getFoodData(@RequestParam(required=true) String query){

        Optional<JsonArray> opt = foodDataService.getFoodList(query);
        JsonArray arr = opt.get();

        if (opt.isEmpty()) {
            JsonObject error = Json.createObjectBuilder().add("error", "failed to retrieve foods").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error.toString());
        }

        return ResponseEntity.ok(arr.toString());

    }

    @ResponseBody
    @GetMapping (path= "/loadpage")
    public ResponseEntity<String> test(){
        System.out.println("authenticated");
        return ResponseEntity.ok().build();
    }


}
