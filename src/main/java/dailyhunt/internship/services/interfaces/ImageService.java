package dailyhunt.internship.services.interfaces;

import dailyhunt.internship.entities.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    String saveImage(String base_64_string) throws IOException;
}
