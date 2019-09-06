package com.mosaiker.heanservice.entity;

import com.alibaba.fastjson.JSONObject;
import javax.persistence.GeneratedValue;
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
  @GeneratedValue
  private Long cId;
  private String hId;
  private Long uId;
  private Long date;
  private String reason;
  private int status;  //0-未审核，1-审核通过，2-审核不通过

  public Contribution(String hId, Long uId, String reason) {
    this.hId = hId;
    this.uId = uId;
    this.date = new Date().getTime();
    this.reason = reason;
    this.status = 0;
  }

  public JSONObject ToJSONObject() {
    return new JSONObject() {{
      put("cId", cId);
      put("hId", hId);
      put("uId", uId);
      put("date", date);
      put("reason", reason);
      put("status", status);
    }};
  }

  public String getHId() {
    return hId;
  }
}
