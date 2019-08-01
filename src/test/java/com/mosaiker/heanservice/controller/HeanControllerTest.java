package com.mosaiker.heanservice.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertTrue;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mosaiker.heanservice.entity.Hean;
import com.mosaiker.heanservice.entity.Picture;
import com.mosaiker.heanservice.repository.HeanRepository;
import com.mosaiker.heanservice.service.HeanService;
import com.mosaiker.heanservice.service.PictureService;
import com.mosaiker.heanservice.service.UserInfoService;
import com.mosaiker.heanservice.utils.Geohash;
import com.mosaiker.heanservice.utils.MyJSONUtil;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.bson.types.Binary;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

/**
 * HeanController Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>���� 31, 2019</pre>
 */
public class HeanControllerTest {


  @Mock
  private PictureService pictureService;
  @Mock
  private UserInfoService userInfoService;
  @Mock
  private HeanRepository heanRepository;
  @Mock
  private HeanService heanService;
  @InjectMocks
  private HeanController heanController;
  private Hean hean1 = new Hean(1L, new Date(), "hean1", 0.01, 0.01, 0.01, new ArrayList<String>());
  private Hean hean2 = new Hean(1L, new Date(), "hean2", 1.01, 1.01, 1.01, new ArrayList<String>());
  private Hean hean22 = new Hean(22L, new Date(), "hean2", 1.01, 1.01, 1.01,
      new ArrayList<String>());

  @Before
  public void before() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @After
  public void after() throws Exception {
  }

  /**
   * Method: findAllPoint(@RequestParam Double longitude, @RequestParam Double latitude,
   *
   * @RequestParam String follower, @RequestParam String time, @RequestHeader Long uId)
   */
  @Test
  public void testFindAllPoint() throws Exception {
    List<Hean> heanList = new ArrayList<>(Arrays.asList(hean1, hean2));
    ArrayList<String> follower = new ArrayList<>(Arrays.asList("me", "mutual", "all"));
    ArrayList<String> time = new ArrayList<>(Arrays.asList("day", "week", "month", "year"));
    when(userInfoService.getFollowings(anyLong())).thenReturn(new JSONObject() {{
      put("following", new ArrayList<Long>() {{
        add(1L);
      }});
      put("follower", new ArrayList<Long>() {{
        add(1L);
      }});
    }});
    when(userInfoService.getFollowers(anyLong())).thenReturn(new JSONObject() {{
      put("following", new ArrayList<Long>() {{
        add(1L);
      }});
      put("follower", new ArrayList<Long>() {{
        add(1L);
      }});
    }});
    when(heanRepository.findAllByCreatedTimeAfter(anyObject())).thenReturn(heanList);
    when(heanService.findAllHeans(anyDouble(), anyDouble())).thenReturn(heanList);
    when(heanService.findHeansByUId(1L)).thenReturn(heanList);
    JSONObject expect = new JSONObject(true);
    expect.put("heans", new JSONArray() {{
      add(hean1.ToJSONPoint());
      add(hean2.ToJSONPoint());
    }});
    expect.put("rescode", 0);
    for (String folset : follower) {
      for (String timeset : time) {
        assertTrue(MyJSONUtil
            .compareTwoJSONObject(heanController.findAllPoint(0.01, 0.01, folset, timeset, 1L),
                expect));
      }
    }
  }

  /**
   * Method: findOneCardHean(@RequestParam String hId, @RequestHeader("uId") Long uId)
   */
  @Test
  public void testFindOneCardHean() throws Exception {
    when(heanService.findHeanByHId("1")).thenReturn(hean1);
    when(heanService.findHeanByHId("22")).thenReturn(hean22);
    when(userInfoService.getSimpleInfo(1L)).thenReturn(new JSONObject() {{
      put("isHeanPublic", true);
    }});
    when(userInfoService.getSimpleInfo(22L)).thenReturn(new JSONObject() {{
      put("isHeanPublic", false);
    }});

    JSONObject ret_ok1 = new JSONObject() {{
      put("heanCard", hean1.ToCard(1L));
      put("rescode", 0);
    }};
    JSONObject ret_ok2 = new JSONObject() {{
      put("heanCard", hean1.ToCard(22L));
      put("rescode", 0);
    }};
    JSONObject ret_fail = new JSONObject() {{
      put("rescode", 3);
    }};

    assertTrue(MyJSONUtil.compareTwoJSONObject(ret_ok1, heanController.findOneCardHean("1", 1L)));
    assertTrue(MyJSONUtil.compareTwoJSONObject(ret_ok2, heanController.findOneCardHean("1", 22L)));
    assertTrue(MyJSONUtil.compareTwoJSONObject(ret_fail, heanController.findOneCardHean("22", 1L)));
  }

  /**
   * Method: findOneDetailedHean(@RequestParam String hId, @RequestParam Double longitude,
   *
   * @RequestParam Double latitude, @RequestHeader("uId") Long uId)
   */
  @Test
  public void testFindOneDetailedHean() throws Exception {
    Long loc_ok = new Geohash().encode(0.01, 0.01);
    Long loc_fail = new Geohash().encode(50.0, 50.0);

    when(heanRepository.findAllByGeoStrBetween(loc_ok - 10 * 10, loc_ok + 10 * 10))
        .thenReturn(new ArrayList<Hean>() {{
          add(hean1);
          add(hean2);
        }});
    when(heanRepository.findAllByGeoStrBetween(loc_fail - 10 * 10, loc_fail + 10 * 10))
        .thenReturn(new ArrayList<Hean>());
    when(userInfoService.getSimpleInfo(1L)).thenReturn(new JSONObject() {{
      put("avatarUrl", "pic");
      put("username", "testname");
    }});
    when(heanService.allComments("1")).thenReturn(new JSONArray());
    when(heanService.findHeanByHId("1")).thenReturn(hean1);

    JSONObject expect_fail = new JSONObject() {{
      put("rescode", 3);
    }};
    JSONObject dest_detail = hean1.ToDetail(1L);
    dest_detail.put("avatar", "pic");
    dest_detail.put("username", "testname");
    dest_detail.put("comments", new JSONArray());
    JSONObject expect_ok = new JSONObject() {{
      put("rescode", 0);
      put("hean", dest_detail);
    }};

    assertTrue(MyJSONUtil
        .compareTwoJSONObject(expect_ok, heanController.findOneDetailedHean("1", 0.01, 0.01, 1L)));
    assertTrue(MyJSONUtil.compareTwoJSONObject(expect_fail,
        heanController.findOneDetailedHean("1", 50.0, 50.0, 1L)));

  }

  /**
   * Method: findAllByUId(@PathVariable Long owner, @RequestHeader("uId") Long viewer)
   */
  @Test
  public void testFindAllByUId() throws Exception {
    when(userInfoService.getSimpleInfo(1L)).thenReturn(new JSONObject() {{
      put("isHeanPublic", true);
    }});
    when(userInfoService.getSimpleInfo(22L)).thenReturn(new JSONObject() {{
      put("isHeanPublic", false);
    }});
    when(userInfoService.getSimpleInfo(3L)).thenReturn(new JSONObject() {{
      put("isHeanPublic", true);
    }});

    List<Hean> heanList = new ArrayList<Hean>() {{
      add(hean1);
      add(hean2);
    }};
    when(heanService.findHeansByUId(1L)).thenReturn(heanList);
    when(heanService.findHeansByUId(3L)).thenReturn(null);

    JSONObject ret_ok1 = new JSONObject() {{
      put("heanCards", new JSONArray() {{
        add(hean1.ToCard(1L));
        add(hean2.ToCard(1L));
      }});
      put("rescode", 0);
    }};
    JSONObject ret_ok2 = new JSONObject() {{
      put("heanCards", new JSONArray() {{
        add(hean1.ToCard(22L));
        add(hean2.ToCard(22L));
      }});
      put("rescode", 0);
    }};
    JSONObject ret_null = new JSONObject() {{
      put("rescode", 4);
    }};
    JSONObject ret_limit = new JSONObject() {{
      put("rescode", 3);
    }};

    assertTrue(MyJSONUtil.compareTwoJSONObject(ret_ok1, heanController.findAllByUId(1L, 1L)));
    assertTrue(MyJSONUtil.compareTwoJSONObject(ret_ok2, heanController.findAllByUId(1L, 22L)));
    assertTrue(MyJSONUtil.compareTwoJSONObject(ret_null, heanController.findAllByUId(3L, 1L)));
    assertTrue(MyJSONUtil.compareTwoJSONObject(ret_limit, heanController.findAllByUId(22L, 1L)));
  }

  /**
   * Method: findStaredByUId(@RequestParam Long owner, @RequestHeader("uId") Long viewer)
   */
  @Test
  public void testFindStaredByUId() throws Exception {
    when(userInfoService.getSimpleInfo(1L)).thenReturn(new JSONObject() {{
      put("isCollectionPublic", true);
    }});
    when(userInfoService.getSimpleInfo(22L)).thenReturn(new JSONObject() {{
      put("isCollectionPublic", false);
    }});
    when(userInfoService.getSimpleInfo(3L)).thenReturn(new JSONObject() {{
      put("isCollectionPublic", true);
    }});

    List<Hean> heanList = new ArrayList<Hean>() {{
      add(hean1);
      add(hean2);
    }};
    when(heanService.findAllMarkedByUId(1L)).thenReturn(heanList);
    when(heanService.findAllMarkedByUId(3L)).thenReturn(null);

    JSONObject ret_ok1 = new JSONObject() {{
      put("heanCards", new JSONArray() {{
        add(hean1.ToCard(1L));
        add(hean2.ToCard(1L));
      }});
      put("rescode", 0);
    }};
    JSONObject ret_ok2 = new JSONObject() {{
      put("heanCards", new JSONArray() {{
        add(hean1.ToCard(22L));
        add(hean2.ToCard(22L));
      }});
      put("rescode", 0);
    }};
    JSONObject ret_null = new JSONObject() {{
      put("rescode", 4);
    }};
    JSONObject ret_limit = new JSONObject() {{
      put("rescode", 3);
    }};

    assertTrue(MyJSONUtil.compareTwoJSONObject(ret_ok1, heanController.findStaredByUId(1L, 1L)));
    assertTrue(MyJSONUtil.compareTwoJSONObject(ret_ok2, heanController.findStaredByUId(1L, 22L)));
    assertTrue(MyJSONUtil.compareTwoJSONObject(ret_null, heanController.findStaredByUId(3L, 1L)));
    assertTrue(MyJSONUtil.compareTwoJSONObject(ret_limit, heanController.findStaredByUId(22L, 1L)));
  }

  /**
   * Method: deleteHean(@RequestParam String hId, @RequestHeader("uId") Long uId)
   */
  @Test
  public void testDeleteHean() throws Exception {
    when(heanService.findHeanByHId("1")).thenReturn(hean1);
    when(heanService.findHeanByHId("2")).thenReturn(hean2);
    when(heanService.findHeanByHId("22")).thenReturn(hean22);
    when(heanService.deleteByHId("1")).thenReturn(true);
    when(heanService.deleteByHId("2")).thenReturn(false);

    JSONObject ret_ok1 = new JSONObject() {{
      put("rescode", 0);
    }};
    JSONObject ret_notexist = new JSONObject() {{
      put("rescode", 1);
    }};
    JSONObject ret_noauth = new JSONObject() {{
      put("rescode", 3);
    }};

    assertTrue(MyJSONUtil.compareTwoJSONObject(ret_ok1, heanController.deleteHean("1", 1L)));
    assertTrue(MyJSONUtil.compareTwoJSONObject(ret_noauth, heanController.deleteHean("22", 3L)));
    assertTrue(MyJSONUtil.compareTwoJSONObject(ret_notexist, heanController.deleteHean("2", 1L)));


  }

  /**
   * Method: uploadHean(@RequestParam(value = "pictures") MultipartFile[] files, @RequestParam(value
   * = "uId") Long uId, @RequestParam(value = "text") String text, @RequestParam(value = "location")
   * String location)
   */
  @Test
  public void testUploadHean() throws Exception {
    JSONObject mockParam = new JSONObject();
    String baseUrl = "http://47.103.0.246:7190/hean/pictures/get/";
    //text 与 pics同时为空的情况
    MultipartFile[] pic1 = new MultipartFile[]{};
    JSONObject result1 = heanController.uploadHean(null, 10000L, "", "0.1,0.1,0.1");
    JSONObject result2 = heanController.uploadHean(null, 10000L, null, "0.1,0.1,0.1");
    JSONObject result3 = heanController.uploadHean(pic1, 10000L, null, "0.1,0.1,0.1");
    JSONObject result4 = heanController.uploadHean(pic1, 10000L, "", "0.1,0.1,0.1");
    String expected1 = "{\"rescode\":4}";
    assertEquals(expected1, result1.toJSONString());
    assertEquals(expected1, result2.toJSONString());
    assertEquals(expected1, result3.toJSONString());
    assertEquals(expected1, result4.toJSONString());

    //图片数量大于4
    String filePath4 = this.getClass().getResource("").getPath() + "HeanControllertesta.class";
    File file4 = new File(filePath4);
    FileInputStream fileInputStream41 = new FileInputStream(file4);
    FileInputStream fileInputStream42 = new FileInputStream(file4);
    MultipartFile picPart1 = new MockMultipartFile("pic1", "Mockpic1", "image/jpg",
        fileInputStream41);
    MultipartFile picPart2 = new MockMultipartFile("pic2", "Mockpic2", "image/jpeg",
        fileInputStream42);
    MultipartFile[] pic4 = new MultipartFile[]{picPart1, picPart2, picPart1, picPart2, picPart1};
    JSONObject result41 = heanController.uploadHean(pic4, 10000L, "hello", "0.1,0.1");
    String expected41 = "{\"rescode\":1}";
    assertEquals(expected41, result41.toJSONString());

    //经纬高格式不对的情况
    String filePath = this.getClass().getResource("").getPath() + "HeanControllertesta.class";
    File file = new File(filePath);
    FileInputStream fileInputStream1 = new FileInputStream(file);
    FileInputStream fileInputStream2 = new FileInputStream(file);
    MultipartFile[] pic2 = new MultipartFile[]{picPart1, picPart2};
    JSONObject result5 = heanController.uploadHean(pic2, 10000L, "hello", "0.1,0.1");
    String expected2 = "{\"rescode\":5}";
    assertEquals(expected2, result5.toJSONString());

    /*upload picture*/

    when(pictureService.uploadPicture(anyObject(), anyObject())).thenReturn("ok");
    when(pictureService.uploadPicture(anyObject(), anyObject())).thenReturn("ok");
    JSONObject result6 = heanController.uploadHean(pic2, 10000L, "hello", "0.1,0.1,0.1");
    String expected3 = "{\"rescode\":0}";
    assertEquals(expected3, result6.toJSONString());

    /*upload exception*/
    String pathEx = this.getClass().getResource("/").getPath()
        + "com/mosaiker/heanservice/HeanServiceApplicationTests.class";
    File fileEx = new File(pathEx);
    FileInputStream fileInputStreamEx1 = new FileInputStream(fileEx);
    MultipartFile picPart3 = new MockMultipartFile("pic3", "Mockpic3", "image/jpg",
        fileInputStreamEx1);
    MultipartFile[] pic3 = new MultipartFile[]{picPart3};
    when(pictureService.uploadPicture(anyObject(), anyObject())).thenReturn(null);
    JSONObject UploadEx = heanController.uploadHean(pic3, 10000L, "hello", "0.1,0.1,0.1");
    String expected4 = "{\"badPicture\":0,\"rescode\":3}";
    assertEquals(expected4, UploadEx.toJSONString());
  }

  /**
   * Method: getPicture(@PathVariable String pId)
   */
  @Test
  public void testGetPicture() throws Exception {
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(heanController).build();

    Picture pic1 = new Picture("pic1", new Binary("pic1".getBytes()), "JPEG", 1L);
    Picture not_found = new Picture("not_found", new Binary("not_found".getBytes()), "JPEG", 1L);
    when(pictureService.findPictureByPId("pic1")).thenReturn(pic1);
    byte[] expected = pic1.getContent().getData();
    when(pictureService.findPictureByPId("pic2")).thenReturn(not_found);
    byte[] expected2 = null;

    mockMvc.perform(MockMvcRequestBuilders.get("/pictures/get/pic1")
        .accept(MediaType.IMAGE_JPEG_VALUE))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("pic1"))
        .andDo(MockMvcResultHandlers.print())
        .andReturn();

    mockMvc.perform(MockMvcRequestBuilders.get("/pictures/get/pic2")
        .accept(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("not_found"))
        .andDo(MockMvcResultHandlers.print())
        .andReturn();
  }

  /**
   * Method: setLike(@RequestBody JSONObject param, @RequestHeader("uId") Long uId)
   */
  @Test
  public void testSetLike() throws Exception {
    when(heanService.setLike("1", 1L)).thenReturn(true);

    JSONObject param = new JSONObject() {{
      put("hId", "1");
    }};
    JSONObject expect = new JSONObject() {{
      put("rescode", 0);
    }};

    assertTrue(MyJSONUtil.compareTwoJSONObject(expect, heanController.setLike(param, 1L)));
  }

  /**
   * Method: setStar(@RequestBody JSONObject param, @RequestHeader("uId") Long uId)
   */
  @Test
  public void testSetStar() throws Exception {
    when(heanService.setStar("1", 1L)).thenReturn(true);

    JSONObject param = new JSONObject() {{
      put("hId", "1");
    }};
    JSONObject expect = new JSONObject() {{
      put("rescode", 0);
    }};

    assertTrue(MyJSONUtil.compareTwoJSONObject(expect, heanController.setStar(param, 1L)));
  }

  /**
   * Method: cancelStar(@RequestBody JSONObject param, @RequestHeader("uId") Long uId)
   */
  @Test
  public void testCancelStar() throws Exception {
    when(heanService.cancelStar("1", 1L)).thenReturn(Boolean.FALSE);

    JSONObject param = new JSONObject() {{
      put("hId", "1");
    }};
    JSONObject expect = new JSONObject() {{
      put("rescode", 0);
    }};

    assertTrue(MyJSONUtil.compareTwoJSONObject(expect, heanController.cancelStar(param, 1L)));
  }

}
