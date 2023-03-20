package journal.server.repositories;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Repository
public class ImageRepository {

    @Autowired
    private AmazonS3 s3Client;

    public String upload(MultipartFile file, String mealId) throws IOException {
            //user data
            Map<String, String> postData = new HashMap<>();
            postData.put("postid", mealId);
            postData.put("uploadTime", new Date().toString());
            postData.put("originalFilename", file.getOriginalFilename());
    
            //metadata of the file
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            metadata.setUserMetadata(postData);
            
            //create a put request
            PutObjectRequest putReq = new PutObjectRequest("vttpnus", "foodjournal/%s".formatted(mealId), file.getInputStream() ,metadata);
            
            //allow public access
            putReq.withCannedAcl(CannedAccessControlList.PublicRead);
            
            s3Client.putObject(putReq);
    
            String imageUrl = "https://vttpnus.sgp1.digitaloceanspaces.com/foodjournal/%s".formatted(mealId);
            
            return imageUrl;
    }

    public void deleteImage(String mealId){
        s3Client.deleteObject("vttpnus", "foodjournal/%s".formatted(mealId));
    }
    
}
