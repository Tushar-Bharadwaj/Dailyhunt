package dailyhunt.internship.endpoints;

import dailyhunt.internship.services.interfaces.ImageService;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(ImageEndpoint.BASE_URL)
public class ImageEndpoint {

    static final String BASE_URL = "/api/v1/image";

    private ImageService imageService;

    public ImageEndpoint(ImageService imageService){
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> uploadImage(@Valid @RequestBody String base_64_string) throws IOException {
        return ResponseEntity.ok().body(imageService.saveImage(base_64_string));
    }
}
