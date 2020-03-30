//package dailyhunt.internship.util.images;
//
//import dailyhunt.internship.entities.Image;
//import dailyhunt.internship.exceptions.BadRequestException;
//import dailyhunt.internship.services.interfaces.ImageService;
//import org.apache.tomcat.util.http.fileupload.IOUtils;
//import org.springframework.stereotype.Service;
//import org.springframework.util.DigestUtils;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.activation.MimetypesFileTypeMap;
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.*;
//import java.time.Instant;
//
//@Service
//public class ImageServiceImpl implements ImageService {
//    private static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads/";
//
//    public boolean isImage(File file) {
//        String mimeType = new MimetypesFileTypeMap().getContentType( file );
//        if(mimeType.substring(0,5).equalsIgnoreCase("image")){
//            return true;
//        }
//        return false;
//    }
//    public String saveImage(MultipartFile file, String fileMD5) throws IOException {
//        if (!file.isEmpty()) {
//            try {
//                byte[] bytes = file.getBytes();
//
//                // Creating the directory to store file
//                String rootPath = System.getProperty("user.dir");
//                File dir = new File(rootPath + File.separator + "tmpFiles");
//                if (!dir.exists())
//                    dir.mkdirs();
//                String filePath = dir.getAbsolutePath() + File.separator + fileMD5.substring(0, 2) + File.separator + fileMD5.substring(2, 4) + File.separator + fileMD5;
//                // Create the file on server
//                File serverFile = new File(filePath);
//                BufferedOutputStream stream = new BufferedOutputStream(
//                        new FileOutputStream(serverFile));
//                stream.write(bytes);
//                stream.close();
//                return filePath;
//
//
//            } catch (Exception e) {
//                throw new BadRequestException("Invalid File");
//
//            }
//        }
//            throw new BadRequestException("Invalid File");
//
//    }
//}
