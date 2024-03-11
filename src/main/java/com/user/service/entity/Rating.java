package com.user.service.entity;

import jakarta.persistence.Transient;
import lombok.Data;

@Data
public class Rating {
    private String ratingId;
    private String userId;
    private String hotelId;
    private String rating;
    private String feedback;

    @Transient
    private Hotel hotel;
}
