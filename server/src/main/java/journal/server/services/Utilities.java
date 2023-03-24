package journal.server.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class Utilities {
    
    public String generateUUID(){
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
