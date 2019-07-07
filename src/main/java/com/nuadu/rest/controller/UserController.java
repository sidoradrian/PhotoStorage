package com.nuadu.rest.controller;

import static com.nuadu.rest.sort.SortStrategy.*;

import com.nuadu.rest.model.Photo;
import com.nuadu.rest.model.User;
import com.nuadu.rest.model.UserDto;
import com.nuadu.rest.service.FileStorageService;
import com.nuadu.rest.service.PhotoService;
import com.nuadu.rest.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

/**
 * Rest controller to perform basic user's operations. Each operation is secured by token. To generate
 * token send post request:
 * http://localhost:8080/oauth/token?grant_type=password&username=&password=
 *
 * @author Adrian Sidor
 */
@RestController
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private PhotoService photoService;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private UserService userService;

    /**
     * Gets all user's photo objects sorted by name or by creation timestamp.
     * http://localhost:8080/user/{userId}/photos?sortBy=&access_token=
     *
     * @param userId
     * @param sortBy sort strategy
     * @return photo page
     */
    @GetMapping(value = "user/{userId}/photos")
    public ResponseEntity<Page<Photo>> getAllUserPhotos(@PathVariable String userId,
                                                              @RequestParam(value = "sortBy") String sortBy,
                                                              Pageable pageable) {

        if (!StringUtils.equalsAnyIgnoreCase(sortBy, CREATION_TIMESTAMP.getLabel(), NAME.getLabel())) {
            throw new UnsupportedOperationException("Sorting by " + sortBy + " is not supported.");
        }

        Pageable sortedBy = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(sortBy));
        return ResponseEntity
                .ok()
                .body(photoService.findPaginated(userId, sortedBy));
    }

    /**
     * Gets user's photo object by name.
     * http://localhost:8080/user/{userId}/photo?photoName=&access_token=
     *
     * @param userId
     * @param name photo's name
     * @return photo object
     */
    @GetMapping(value = "user/{userId}/photo")
    public ResponseEntity<Photo> getPhotoByName(@PathVariable String userId, @RequestParam(value = "photoName") String name) {
        return ResponseEntity
                .ok()
                .body(photoService.findByName(name, userId));
    }

    /**
     * Deletes user's photo object by name.
     * http://localhost:8080/photo/{photoId}?access_token=
     *
     * @param photoId

     * @return id of removed photo
     */
    @DeleteMapping(value = "photo/{photoId}")
    public ResponseEntity<String> deletePhoto(@PathVariable String photoId) {
        photoService.deleteById(photoId);

        return ResponseEntity
                .ok()
                .body("Photo was removed " + photoId);
    }

    /**
     * Gets user's photo by name.
     * http://localhost:8080/photo/{photoName}?access_token=
     *
     * @param photoName
     * @param request

     * @return photo resource
     */
    @GetMapping("photo/{photoName:.+}")
    public ResponseEntity<Resource> getPhoto(@PathVariable String photoName, HttpServletRequest request) {
        Resource resource = fileStorageService.loadFileAsResource(photoName);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.warn("Could not determin file type.");
        }

        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


    /**
     * Creates new user.
     * http://localhost:8080/newUser
     *
     * @param userDto containing email and password
     * @return created user.
     */
    @PostMapping("newUser")
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDto userDto) {
            return ResponseEntity
                        .ok()
                        .body(userService.save(userDto));
    }


    /**
     * Updates user's email.
     * http://localhost:8080/user?access_token=
     *
     * @param user containing id and new email
     *
     * @return updated user.
     */
    @PutMapping("user")
    public ResponseEntity<User> updateEmail(@Valid @RequestBody User user) {

        return ResponseEntity
                .ok()
                .body(userService.updateUser(user));
    }

    /**
     * Gets user by id.
     * http://localhost:8080/user/{userId}?access_token=
     *
     * @param userId
     *
     * @return user.
     */
    @GetMapping("user/{userId}")
    public ResponseEntity<User> getUser(@PathVariable String userId) {

        return ResponseEntity
                .ok()
                .body(userService.findById(userId));
    }

    /**
     * Adds new photo.
     * http://localhost:8080/user/{userId}/photo?access_token=
     *
     * @param userId
     * @param file uploaded image
     * @return created photo.
     */
    @PostMapping(value = "user/{userId}/photo")
    public ResponseEntity<Photo> addPhoto(@PathVariable String userId, @RequestParam("file") MultipartFile file) {
        fileStorageService.storeFile(file);

        return ResponseEntity
                .ok()
                .body(photoService.storePhoto(userId, file));
    }
}
