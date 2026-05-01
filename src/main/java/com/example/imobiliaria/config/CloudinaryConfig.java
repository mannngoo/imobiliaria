package com.example.imobiliaria.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "Root",
                "api_key", "866683981376865",
                "api_secret", "wQtuXNwvJEBpPtwrf-J15YujOvM"
        ));
    }
}