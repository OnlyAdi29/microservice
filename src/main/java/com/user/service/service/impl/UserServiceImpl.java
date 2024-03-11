package com.user.service.service.impl;

import com.user.service.entity.Hotel;
import com.user.service.entity.Rating;
import com.user.service.entity.User;
import com.user.service.exeptionResource.UserNotFound;
import com.user.service.feignconfig.HotelService;
import com.user.service.repository.UserRepository;
import com.user.service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private HotelService hotelService;

    //private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Override
    public User saveUser(User user) {
        String randomId = UUID.randomUUID().toString();
        user.setUserId(randomId);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(String userId) throws UserNotFound {
        User us = userRepository.findById(userId).orElseThrow(()->new UserNotFound("User is not available") );
        Rating[] forObject = restTemplate.getForObject("http://RATING-SERVICE/ratings/user/"+us.getUserId(), Rating[].class);
        List<Rating> ratings = Arrays.stream(forObject).toList();

        List<Rating> ratingList = ratings.stream().map(rating -> {
             //ResponseEntity<Hotel> hotelrating = restTemplate.getForEntity("http://HOTEL-SERVICE/hotel/"+rating.getHotelId(), Hotel.class);
             Hotel hotel = hotelService.getHotel(rating.getHotelId());
            // log.info("status code: {} ",hotelrating.getStatusCode());

             rating.setHotel(hotel);
             return rating;
        }).collect(Collectors.toList());
        //log.info("{}",forObject);
        us.setRating(ratings);

        return us;
    }

    @Override
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public User updateUser(String userId,User user) throws UserNotFound {

        Optional<User> u1 = userRepository.findById(userId);
        if(!u1.isPresent()){
            throw new UserNotFound("cannot find user");
        }else {
            User u = u1.get();
            u.setName(user.getName());
            u.setEmail(user.getEmail());
            u.setAbout(user.getAbout());

            return userRepository.save(u);
        }
    }
}
