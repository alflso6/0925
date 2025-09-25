package com.example.gagumoa_backend.model;

import lombok.Data;

@Data
public class Review {
    private String author;
    private double rating;
    private String comment;
    private String date;
}