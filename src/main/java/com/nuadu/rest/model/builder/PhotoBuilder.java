package com.nuadu.rest.model.builder;

import com.nuadu.rest.model.Photo;
import com.nuadu.rest.model.User;

import java.time.LocalDateTime;

public class PhotoBuilder {
    private String name;
    private String url;
    private LocalDateTime creationTimestamp;
    private User user;

    public PhotoBuilder name(String name) {
        this.name = name;
        return this;
    }

    public PhotoBuilder url(String url) {
        this.url = url;
        return this;
    }

    public PhotoBuilder creationTimestamp(LocalDateTime creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
        return this;
    }

    public PhotoBuilder user(User user) {
        this.user = user;
        return this;
    }

    public Photo build() {
        Photo photo = new Photo();
        photo.setName(name);
        photo.setUrl(url);
        photo.setCreationTimestamp(creationTimestamp);
        photo.setUser(user);

        return photo;
    }
}
