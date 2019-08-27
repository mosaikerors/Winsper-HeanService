package com.mosaiker.heanservice.entity;

import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Marked {
  @GeneratedValue
  @Id
  private String markId;
  private Long uId;
  @ElementCollection
  private List<String> marks;//list of marked hean card

  public Marked(Long uId) {
    this.uId = uId;
    this.marks = new ArrayList<>();
  }
}
