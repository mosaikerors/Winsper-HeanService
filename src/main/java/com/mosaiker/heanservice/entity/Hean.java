package com.mosaiker.heanservice.entity;
import com.alibaba.fastjson.JSONObject;
import java.util.Date;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hean {
  @Id
  private String hId;
  private Long uId;
  private Date createdTime; // 上传时间
  private String text;
  private double longtitude;//精度
  private double latitude;//纬度
  private double height;//海拔
  @ElementCollection
  private List<String> pics;
  public Hean(Long uId,Date createdTime,String text,double longtitude,double latitude,double height,List<String> pics){
    this.uId=uId;
    this.createdTime=createdTime;
    this.text=text;
    this.longtitude=longtitude;
    this.latitude=latitude;
    this.height=height;
    this.pics=pics;
  }
  public JSONObject ToJSONObject(){
    JSONObject result = new JSONObject(255,true);
    result.put("hId",this.hId);
    result.put("uId",this.uId);
    result.put("createdTime",this.createdTime);
    result.put("text",this.text);
    result.put("longtitude",this.longtitude);
    result.put("latitude",this.latitude);
    result.put("height",this.height);
    result.put("pics",this.pics);
    return result;
  }
}
