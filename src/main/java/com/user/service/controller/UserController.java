package com.user.service.controller;

import com.user.service.entity.User;
import com.user.service.exeptionResource.UserNotFound;
import com.user.service.service.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Optionals;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/save")
    public ResponseEntity<User> saveDetails(@RequestBody User user){
        User user1 = userService.saveUser(user);
        return new ResponseEntity<User>(user1,HttpStatus.CREATED);
    }
    @GetMapping("/all")
    public ResponseEntity<List<User>> getUsers(){
        List<User> alluser = userService.getAllUser();
        return  ResponseEntity.ok(alluser);
    }

    int retryCount = 1;
    @GetMapping("/{userId}")
    //@CircuitBreaker(name = "RatingHotelBreaker",fallbackMethod = "ratingHotelFallBack")
    //@Retry(name = "ratingHotelRetry",fallbackMethod = "ratingHotelFallBack")
    @RateLimiter(name = "ratingHotelRateLimiter",fallbackMethod = "ratingHotelFallBack")
    public ResponseEntity<User> getUserById(@PathVariable String userId) throws UserNotFound {
        log.info("retry count: {}",retryCount);
        retryCount++;
        User user2 = userService.getUser(userId);
        return ResponseEntity.ok(user2);
    }
    public ResponseEntity<?> ratingHotelFallBack(String userId,Exception e){

        log.info("This is fallbak method"+e.getMessage());
        return  new ResponseEntity<>("SomeThing Went Wrong !!!",HttpStatus.OK);

    }
    @DeleteMapping("/delete/{userId}")
    public void deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
    }
    @PutMapping("/update/{userId}")
    public User updateUser(@PathVariable String userId,@RequestBody User user) throws UserNotFound {
        //Optional<User> optional = userService.updateUser(userId,user);
        return  userService.updateUser(userId,user);
    }


}
