package com.mosaiker.heanservice.controller;

import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertTrue;

import com.alibaba.fastjson.JSONObject;
import com.mosaiker.heanservice.service.HeanCommentService;
import com.mosaiker.heanservice.utils.MyJSONUtil;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * HeanCommentController Tester.
 *
 * @author <DeeEll-X>
 * @version 1.0
 * @since <pre>July 31, 2019</pre>
 */
public class HeanCommentControllerTest {

  @Mock
  private HeanCommentService heanCommentService;
  @InjectMocks
  private HeanCommentController heanCommentController;

  @Before
  public void before() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @After
  public void after() throws Exception {
  }

  /**
   * Method: add(@RequestBody JSONObject param, @RequestHeader("uId")Long uId)
   */
  @Test
  public void testAddSuccess() throws Exception {
    JSONObject param = new JSONObject();
    param.put("hId", "1");
    param.put("targetCommentId", "0");
    param.put("content", "agree.");
    when(heanCommentService.saveComment(anyObject())).thenReturn(0);
    JSONObject ret = heanCommentController.add(param, 1L);
    JSONObject expect = new JSONObject();
    expect.put("rescode", 0);
    assertTrue(MyJSONUtil.compareTwoJSONObject(ret, expect));
  }

  @Test
  public void testAddEx() throws Exception {
    JSONObject param = new JSONObject();
    param.put("hId", "1");
    param.put("targetCommentId", "0");
    param.put("content", "");
    JSONObject ret = heanCommentController.add(param, 1L);
    JSONObject expect = new JSONObject();
    expect.put("rescode", 3);
    assertTrue(MyJSONUtil.compareTwoJSONObject(ret, expect));
  }

  /**
   * Method: getAllComms(@RequestHeader("uId")Long uId)
   */
  @Test
  public void testGetAllComms() throws Exception {
    when(heanCommentService.findAllByUId(1L)).thenReturn(null);
    JSONObject ret = heanCommentController.getAllComms(1L);
    JSONObject expect = new JSONObject();
    expect.put("rescode", 0);
    expect.put("comments", null);
    boolean compare = MyJSONUtil.compareTwoJSONObject(ret, expect);
    assertTrue(compare);
  }


}
