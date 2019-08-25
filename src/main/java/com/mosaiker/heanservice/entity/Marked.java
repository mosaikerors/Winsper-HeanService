package com.mosaiker.heanservice.entity;

import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Marked {
  @Id
  Long uId;
  @ElementCollection
  List<String> marks;//list of marked hean card

  public Marked(Long uId) {
    this.uId = uId;
    this.marks = new ArrayList<>();
  }
}
