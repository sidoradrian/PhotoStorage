package com.nuadu.rest.service;

import com.nuadu.rest.model.Photo;
import com.nuadu.rest.model.builder.PhotoBuilder;
import com.nuadu.rest.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;

@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private UserService userService;

    public Photo storePhoto(String userId, MultipartFile file) {
        String fileName = file.getOriginalFilename();

        String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/photo/")
                .path(fileName)
                .toUriString();

                Photo photo = new PhotoBuilder()
                .name(fileName)
                .creationTimestamp(LocalDateTime.now())
                .url(fileUrl)
                .user(userService.findById(userId))
                .build();

        return photoRepository.save(photo);
    }

    public Page<Photo> findPaginated(String userId, Pageable pageable) {
        return photoRepository.findPaginated(userService.findById(userId), pageable);
    }

    public Photo findByName(String name, String userId) {
        return photoRepository.findFirstByNameAndUser(name, userService.findById(userId));
    }

    public void deleteById(String photoId) {
        photoRepository.deleteById(Long.parseLong(photoId));
    }
}
