package journal.server.services;

import java.io.StringReader;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import journal.server.models.FoodData;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FoodDataService {
    private static final Logger logger = LoggerFactory.getLogger(FoodDataService.class);

    @Value ("${calorie.ninja.apikey}")
    String apiKey;


    private static final String URL = "https://api.calorieninjas.com/v1/nutrition";

    public Optional<JsonArray> getFoodDataList(String q){

        String requestUrl = UriComponentsBuilder.fromUriString(URL)
                            .queryParam("query", q)
                            .toUriString()
                            .replace("%20", " ");


        logger.info(requestUrl);
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = null;

        try{
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Api-Key", apiKey);
            HttpEntity request = new HttpEntity<>(headers);
            resp = template.exchange(requestUrl, 
                                    HttpMethod.GET, 
                                    request, 
                                    String.class,
                                    1);
                                    

            logger.info("response body >>>>>>>>> " + resp.getBody());
            JsonReader reader = Json.createReader(new StringReader(resp.getBody()));
            JsonObject o = reader.readObject();
            JsonArray arr = o.getJsonArray("items");
            JsonArrayBuilder builder = Json.createArrayBuilder();
            for (JsonValue v : arr){
                builder.add(
                    FoodData.toJsonObject(
                        FoodData.createFoodData((JsonObject) v)
                    )
                );
            }
            return Optional.of(builder.build());
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        logger.info("response empty");
        return Optional.empty();

    }
}
