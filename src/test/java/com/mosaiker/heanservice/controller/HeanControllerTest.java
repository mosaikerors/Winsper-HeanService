package com.mosaiker.heanservice.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.alibaba.fastjson.JSONObject;
import com.mosaiker.heanservice.entity.Hean;
import com.mosaiker.heanservice.entity.Picture;
import com.mosaiker.heanservice.service.HeanService;
import com.mosaiker.heanservice.service.PictureService;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
 * @since <pre>���� 5, 2019</pre>
 */
public class HeanControllerTest {

  private MockMvc mockMvc;
  @Mock
  private HeanService heanService;
  @Mock
  private PictureService pictureService;
  @InjectMocks
  private HeanController heanController;

  @Before
  public void before() throws Exception {
    MockitoAnnotations.initMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(heanController).build();

  }

  @After
  public void after() throws Exception {
  }

  /**
   * Method: findAllByUId(@RequestBody JSONObject param)
   */
  @Test
  public void testFindAllByUId() throws Exception {
    Date date = new Date();
    Hean hean1 = new Hean("hean1", 10000L, date, "test hean1", 100.1, 100.1, 100.1, null);
    Hean hean2 = new Hean("hean2", 10000L, date, "test hean2", 100.1, 100.1, 100.1, null);
    List<Hean> heanList = Arrays.asList(hean1, hean2);
    when(heanService.findHeansByUId(10000L)).thenReturn(heanList);

    JSONObject mockParam = new JSONObject();
    mockParam.put("uId", 10000L);
    String expected1 = "{\"heanArray\":[{\"createdTime\":" + date.getTime()
        + ",\"text\":\"test hean1\",\"longtitude\":100.1,\"latitude\":100.1,\"height\":100.1,\"pics\":null,\"hid\":\"hean1\",\"uid\":10000},{\"createdTime\":"
        + date.getTime()
        + ",\"text\":\"test hean2\",\"longtitude\":100.1,\"latitude\":100.1,\"height\":100.1,\"pics\":null,\"hid\":\"hean2\",\"uid\":10000}],\"message\":\"ok\"}";

    mockMvc.perform(MockMvcRequestBuilders.post("/hean/byUId")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON).content(mockParam.toJSONString()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(expected1))
        .andDo(MockMvcResultHandlers.print())
        .andReturn();

    mockParam.clear();
    mockParam.put("uId", "2000");
    when(heanService.findHeansByUId(2000L)).thenReturn(null);
    String expected2 = "<JSONObject><message>not found</message></JSONObject>";
    mockMvc.perform(MockMvcRequestBuilders.post("/hean/byUId")
        .contentType(MediaType.APPLICATION_JSON).content(mockParam.toJSONString()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(expected2))
        .andDo(MockMvcResultHandlers.print())
        .andReturn();
  }

  /**
   * Method: findAll()
   */
  @Test
  public void testFindAll() throws Exception {
    Date date = new Date();
    Hean hean1 = new Hean("hean1", 10000L, date, "test hean1", 100.1, 100.1, 100.1, null);
    Hean hean2 = new Hean("hean2", 10000L, date, "test hean2", 100.1, 100.1, 100.1, null);
    List<Hean> heanList = Arrays.asList(hean1, hean2);
    when(heanService.findAllHeans()).thenReturn(heanList);

    String expected1 =
        "{\"heanArray\":[{\"hId\":\"hean1\",\"uId\":10000,\"createdTime\":" + date.getTime()
            + ",\"text\":\"test hean1\",\"longtitude\":100.1,\"latitude\":100.1,\"height\":100.1,\"pics\":null},{\"hId\":\"hean2\",\"uId\":10000,\"createdTime\":"
            + date.getTime()
            + ",\"text\":\"test hean2\",\"longtitude\":100.1,\"latitude\":100.1,\"height\":100.1,\"pics\":null}],\"message\":\"ok\"}";
    mockMvc.perform(MockMvcRequestBuilders.get("/hean/all")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(expected1))
        .andDo(MockMvcResultHandlers.print())
        .andReturn();

  }

  /**
   * Method: deleteHean(@RequestBody JSONObject param)
   */
  @Test
  public void testDeleteHean() throws Exception {
    Hean hean1 = new Hean("hean1", 10000L, new Date(), "test hean1", 100.1, 100.1, 100.1, null);
    when(heanService.deleteByHId("hean2")).thenReturn(false);

    JSONObject mockParam = new JSONObject();
    mockParam.put("hId", "hean2");
    String expected1 = "{\"message\":\"not found\"}";
    mockMvc.perform(MockMvcRequestBuilders.delete("/hean/delete")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON).content(mockParam.toJSONString()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(expected1))
        .andDo(MockMvcResultHandlers.print())
        .andReturn();

    when(heanService.deleteByHId("hean1")).thenReturn(true);
    mockParam.put("hId", "hean1");
    String expected2 = "{\"message\":\"ok\"}";
    mockMvc.perform(MockMvcRequestBuilders.delete("/hean/delete")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON).content(mockParam.toJSONString()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(expected2))
        .andDo(MockMvcResultHandlers.print())
        .andReturn();
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
    /* text 与 pics同时为空的情况*/
    MultipartFile[] pic1 = new MultipartFile[]{};

    JSONObject result1 = heanController.uploadHean(null, 10000L, "", "0.1,0.1,0.1");
    JSONObject result2 = heanController.uploadHean(null, 10000L, null, "0.1,0.1,0.1");
    JSONObject result3 = heanController.uploadHean(pic1, 10000L, null, "0.1,0.1,0.1");
    JSONObject result4 = heanController.uploadHean(pic1, 10000L, "", "0.1,0.1,0.1");
    String expected1 = "{\"message\":\"pics and text cannot all be null\"}";
    assertEquals(expected1, result1.toJSONString());
    assertEquals(expected1, result2.toJSONString());
    assertEquals(expected1, result3.toJSONString());
    assertEquals(expected1, result4.toJSONString());

    /*经纬高格式不对的情况*/
    String filePath = this.getClass().getResource("").getPath() + "HeanControllerTest.class";
    File file = new File(filePath);
    FileInputStream fileInputStream1 = new FileInputStream(file);
    FileInputStream fileInputStream2 = new FileInputStream(file);
    MultipartFile picPart1 = new MockMultipartFile("pic1", "Mockpic1", "image/jpg",
        fileInputStream1);
    MultipartFile picPart2 = new MockMultipartFile("pic2", "Mockpic2", "image/jpeg",
        fileInputStream2);
    MultipartFile[] pic2 = new MultipartFile[]{picPart1, picPart2};
    JSONObject result5 = heanController.uploadHean(pic2, 10000L, "hello", "0.1,0.1,");
    String expected2 = "{\"message\":\"wrong location format\"}";
    assertEquals(expected2, result5.toJSONString());

    /*upload picture*/
    JSONObject result6 = heanController.uploadHean(pic2, 10000L, "hello", "0.1,0.1,0.1");
    when(pictureService.uploadPicture(picPart1, baseUrl)).thenReturn(null);
    when(pictureService.uploadPicture(picPart2, baseUrl)).thenReturn(null);
    String expected3 = "{\"message\":\"ok\",\"pictures\":[null,null]}";
    assertEquals(expected3, result6.toJSONString());

    /*upload exception*/
    String pathEx = this.getClass().getResource("/").getPath()
        + "com/mosaiker/heanservice/HeanServiceApplicationTests.class";
    File fileEx = new File(pathEx);
    FileInputStream fileInputStreamEx1 = new FileInputStream(fileEx);
    MultipartFile picPart3 = new MockMultipartFile("pic3", "Mockpic3", "image/jpg",
        fileInputStreamEx1);
    MultipartFile[] pic3 = new MultipartFile[]{picPart3};
    when(pictureService.uploadPicture(picPart3, baseUrl)).thenThrow(new IOException());
    JSONObject UploadEx = heanController.uploadHean(pic3, 10000L, "hello", "0.1,.0.1,0.1");
    String expected4 = "{\"message\":\"No.0 pic fail\"}";
    assertEquals(expected4, UploadEx.toJSONString());
  }

  /**
   * Method: getPicture(@PathVariable String pId)
   */
  @Test
  public void testGetPicture() throws Exception {

    Picture pic1 = new Picture("pic1", new Binary("pic1".getBytes()), "JPEG", 1L);
    Picture not_found = new Picture("not_found", new Binary("not_found".getBytes()), "JPEG", 1L);
    when(pictureService.findPictureByPId("pic1")).thenReturn(pic1);
    byte[] expected = pic1.getContent().getData();
    when(pictureService.findPictureByPId("pic2")).thenReturn(not_found);
    byte[] expected2 = null;

    mockMvc.perform(MockMvcRequestBuilders.get("/hean/pictures/get/pic1")
        .accept(MediaType.IMAGE_JPEG_VALUE))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("pic1"))
        .andDo(MockMvcResultHandlers.print())
        .andReturn();

    mockMvc.perform(MockMvcRequestBuilders.get("/hean/pictures/get/pic2")
        .accept(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("not_found"))
        .andDo(MockMvcResultHandlers.print())
        .andReturn();

  }


}
