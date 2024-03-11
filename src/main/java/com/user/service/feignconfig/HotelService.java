package com.user.service.feignconfig;

import com.user.service.entity.Hotel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "HOTEL-Service")
public interface HotelService {
    @GetMapping("/hotel/{hotelId}")
    Hotel getHotel(@PathVariable String hotelId);
}
