package com.damasahhre.hooftrim.database.models;

import androidx.room.Embedded;

public class FarmWithCowCount {

    @Embedded
    Farm farm;

    Integer count;

}
