package com.mosaiker.heanservice.entity;

import com.alibaba.fastjson.JSONObject;
import java.util.List;
import javax.persistence.ElementCollection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Marked {
  Long uId;
  @ElementCollection
  List<String> marks;//list of marked hean card

}
