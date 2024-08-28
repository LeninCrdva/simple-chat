package tec.edu.azuay.chat.config.image;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageConfig {

    @Value("${cloudinary.url}")
    private String CLOUDINARY_URL;

    @Bean
    public Cloudinary cloudinary() throws Exception {
        Cloudinary cloud = new Cloudinary(CLOUDINARY_URL);

        cloud.api().createFolder("simple-chat", ObjectUtils.emptyMap());

        return cloud;
    }
}
