package dailyhunt.internship.services;

import dailyhunt.internship.clientmodels.request.MultiPartResource;
import dailyhunt.internship.clientmodels.response.ImageDTO;
import dailyhunt.internship.services.interfaces.ImageService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class ImageServiceImpl implements ImageService {


    @Override
    public String saveImage(String base_64_string) throws IOException{

        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl = "https://dailyhunt-image-service.herokuapp.com/uploadFile";
        ImageDTO imageDTO = ImageDTO.builder().base64(base_64_string).build();
        String result = restTemplate.postForObject(fooResourceUrl, imageDTO, String.class);
        return result;
    }

}
