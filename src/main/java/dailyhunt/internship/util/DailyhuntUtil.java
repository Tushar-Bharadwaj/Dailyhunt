package dailyhunt.internship.util;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.regex.Pattern;

public class DailyhuntUtil {
    public static boolean isNullOrEmpty(String string) {
        return (string == null || string.trim().isEmpty());
    }

    public static boolean isNullOrEmpty(Collection obj) {
        return (obj == null || obj.isEmpty());
    }

    public static boolean isNullOrEmpty(Object obj) {
        return (obj == null || obj.toString().trim().isEmpty());
    }

    public static boolean emailValidator(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static byte[] convertToFormat(String imageBase64, String formatName) throws IOException {

        BufferedImage inputImage = ImageIO.read(new ByteArrayInputStream(javax.xml.bind.DatatypeConverter.parseBase64Binary(imageBase64)));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(inputImage, formatName, outputStream);

        byte[] bytes = outputStream.toByteArray();

        outputStream.close();

        return bytes;
    }
}

