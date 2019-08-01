package com.mosaiker.heanservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Contribution {
    @Id
    Long cId;
    String hId;
    Long date;

    public Contribution(String hId) {
        this.hId = hId;
        this.date = new Date().getTime();
    }
}
