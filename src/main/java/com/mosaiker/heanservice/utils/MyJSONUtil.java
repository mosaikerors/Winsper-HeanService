package com.mosaiker.heanservice.utils;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class MyJSONUtil {
  public static <T> T initSelfFromJSONObject(Class<T> clazz,
      JSONObject parameterJSON) {
    try {
      Field[] fields = clazz.getDeclaredFields();
      T t = clazz.newInstance();
      for (Field f : fields) {
        f.setAccessible(true);
        f.set(t, getValueOfTypeTFromJSONObject(f.getType(), f.getName(),parameterJSON ));
      }
      return t;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  private static <T> T getValueOfTypeTFromJSONObject(Class<T>clazz,String key,JSONObject jo) throws Exception{
    if (clazz.equals(Integer.class)) {
      return (T) jo.getInteger(key);
    }else if(clazz.equals(Float.class)){
      return (T) jo.getFloat(key);
    }else if(clazz.equals(Double.class)){
      return(T)jo.getDouble(key);
    }else if(clazz.equals(String.class)) {
      return (T)jo.getString(key);
    }else if(clazz.equals(BigDecimal.class)){
      return (T)jo.getBigDecimal(key);
    }else if(clazz.equals(Boolean.class)){
      return (T)jo.getBoolean(key);
    }else if(clazz.equals(Date.class)){
      return (T)jo.getDate(key);
    }else if(clazz.equals(Long.class)){
      return (T)jo.getLong(key);
    }else if(clazz.equals(JSONArray.class)){
      return (T)jo.getJSONArray(key);
    }else if(clazz.equals(JSONObject.class)){
      return (T)jo.getJSONObject(key);
    }else if(clazz.equals(Timestamp.class)){
      return (T)jo.getTimestamp(key);
    }else {
      throw new Exception("cannot get any object from fastjson.JSONObject of Type:"+clazz.getSimpleName());
    }
  }

  /**
   * @function 比较两个JSONObject的key-value对的个数,内容是否一致---忽略顺序
   * @param json1
   * @param json2
   * @return
   */
  public static boolean compareTwoJSONObject(JSONObject json1,JSONObject json2) {
    if (json1==null&&json2==null) {
      return true;
    }else if (json1==null && json2!=null) {
      return false;
    }else if(json1!=null&&json2==null) {
      return false;
    }else if (json1.size()!=json2.size()) {
      int size1 = json1.size();
      int size2=json2.size();
      return false;
    }
    for(String key:json1.keySet()) {
      if ("poiid".equals(key)) {
        System.err.println();
      }
      if(!json2.containsKey(key)) {
        return false;
      }else if(json1.get(key)==null&&json2.get(key)!=null) {
        return false;
      }
      try {
        JSONObject sonJSON1 = json1.getJSONObject(key);
        JSONObject sonJSON2 = json2.getJSONObject(key);
        if(!compareTwoJSONObject(sonJSON1, sonJSON2)) {
          return false;
        }
      }catch (Exception e) {
        try {
          JSONArray sonArray1 = json1.getJSONArray(key);
          JSONArray sonArray2 = json2.getJSONArray(key);
          if(!compareTwoJSONArray(sonArray1, sonArray2)) {
            return false;
          }
        } catch (Exception e1) {
          try {
            Object o1=json1.get(key);
            Object o2=json2.get(key);
            if (!o1.equals(o2)) {
              return false;
            }
          } catch (Exception e2) {
            return false;
          }
        }
      }
    }

    return true;
  }

  /**
   * @function 比较两个JSONArray元素个数,内容是否一致---忽略顺序
   * @param array1
   * @param array2
   * @return	一致返回true,不一致返回false
   */
  public static boolean compareTwoJSONArray(JSONArray array1,JSONArray array2) {
    if (array1==null&&array2==null) {
      return true;
    }else if (array1==null && array2!=null) {
      return false;
    }else if(array1!=null&&array2==null) {
      return false;
    }else if (array1.size()!=array2.size()) {
      return false;
    }
    for(int index=0;index<array1.size();index++) {
      //array1的第index个元素还是JSONArray,则遍历array2的所有元素,递归比较...
      try {
        JSONArray sonArray1 = array1.getJSONArray(index);
        boolean flag = false;
        for(int index2=0;index2<array2.size();index2++) {
          JSONArray sonArray2 = array2.getJSONArray(index2);
          if(compareTwoJSONArray(sonArray1,sonArray2)) {
            flag = true;
          }
        }
        if (!flag) {
          return false;
        }
      } catch (Exception e) {
        //array1的第index个元素是JSONObject,则遍历array2的所有元素,递归比较...
        try {
          JSONObject sonJSON1 = array1.getJSONObject(index);
          boolean flag = false;
          for(int index2=0;index2<array2.size();index2++) {
            JSONObject sonJSON2 = array2.getJSONObject(index2);
            if(compareTwoJSONObject(sonJSON1,sonJSON2)) {
              flag = true;
            }
          }
          if (!flag) {
            return false;
          }
        } catch (Exception e1) {
          //array1的第index个元素非JSONArray&&非JSONObject,则遍历array2的所有元素,递归比较...
          try {
            Object o1 = array1.get(index);
            boolean flag = false;
            for(int index2=0;index2<array2.size();index2++) {
              Object o2 = array2.get(index2);
              if((o1==null&&o2==null)||o1.equals(o2)) {
                flag = true;
              }
            }
            if (!flag) {
              return false;
            }
          }catch (Exception e2) {
            array1.equals(array2);
            return false;
          }
        }
      }
    }
    return true;
  }
  @Test
  public void fff() {
    JSONObject jo1 = new JSONObject();
    jo1.put("abc", "abc");
    jo1.put("cba", "cba");
    jo1.put("jo", jo1.toString());

    JSONObject jo2 = new JSONObject();
    jo2.put("cba", "cba");
    jo2.put("abc", "abc");
    jo2.put("jo", jo2.toString());

    System.err.println(jo1.equals(jo2));
    System.err.println(compareTwoJSONObject(jo1, jo2));
  }

  @Test
  public void f() {
    JSONObject aJsonObject = new JSONObject();
    JSONObject bJsonObject = new JSONObject();

    JSONObject cJsonObject = new JSONObject();
    cJsonObject.put("c", "c");
    cJsonObject.put("d", "d");

    JSONArray eArray = new JSONArray();
    eArray.add("e");
    eArray.add("f");
    eArray.add(1);

    aJsonObject.put("a", "a");
    aJsonObject.put("c", cJsonObject);
    aJsonObject.put("b", "b");
    aJsonObject.put("e", eArray);

    bJsonObject.put("b", "b");
    bJsonObject.put("a", "a");

    JSONObject dJsonObject = new JSONObject();
    dJsonObject.put("d", "d");
    dJsonObject.put("c", "c");
    bJsonObject.put("c",dJsonObject);

    JSONArray fArray = new JSONArray();
    fArray.add("f");
    fArray.add("e");
    fArray.add(1);
    bJsonObject.put("e", fArray);

    System.err.println(compareTwoJSONObject(aJsonObject, bJsonObject));
  }
}
