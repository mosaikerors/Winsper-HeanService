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
  Long uId;
  Long date;
  String reason;

  public Contribution(String hId, Long uId, String reason) {
    this.hId = hId;
    this.uId = uId;
    this.date = new Date().getTime();
    this.reason = reason;
  }

  public JSONObject ToJSONObject() {
    JSONObject ret = new JSONObject() {{
      put("cId", cId);
      put("hId", hId);
      put("uId", uId);
      put("date", date);
      put("reason", reason);
    }};
    return ret;
  }
}
