package com.mosaiker.heanservice.entity;

import com.alibaba.fastjson.JSONObject;
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

  public JSONObject ToJSONObject() {
    JSONObject ret = new JSONObject() {{
      put("cId", cId);
      put("hId", hId);
      put("date", date);
    }};
    return ret;
  }
}
