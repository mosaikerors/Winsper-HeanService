package com.mosaiker.heanservice.service.serviceImple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.alibaba.fastjson.JSONObject;
import com.mosaiker.heanservice.entity.Hean;
import com.mosaiker.heanservice.entity.HeanComment;
import com.mosaiker.heanservice.repository.HeanCommentRepository;
import com.mosaiker.heanservice.repository.HeanRepository;
import com.mosaiker.heanservice.service.UserInfoService;
import java.util.ArrayList;
import java.util.Date;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * HeanCommentServiceImple Tester.
 *
 * @author <DeeEll-X>
 * @version 1.0
 * @since <pre>Aug 1, 2019</pre>
 */
public class HeanCommentServiceImpleTest {

  private HeanComment heanComment1 = new HeanComment("1", 1L, "test", new Date().getTime(), null,
      new ArrayList<>());
  private HeanComment heanComment2 = new HeanComment("1", 1L, "test", new Date().getTime(), "3",
      new ArrayList<>());
  private HeanComment heanComment3 = new HeanComment("1", 1L, "test", new Date().getTime(), null,
      new ArrayList<>());
  private Hean hean1 = new Hean(1L, new Date(), "hean1", 0.01, 0.01, 0.01, new ArrayList<String>());


  @Mock
  private HeanCommentRepository heanCommentRepository;
  @Mock
  private HeanRepository heanRepository;
  @Mock
  private UserInfoService userInfoService;
  @InjectMocks
  private HeanCommentServiceImple heanCommentServiceImple;

  @Before
  public void before() throws Exception {
    MockitoAnnotations.initMocks(this);

  }

  @After
  public void after() throws Exception {
  }

  /**
   * Method: findHeanCommentByCId(String cId)
   */
  @Test
  public void testFindHeanCommentByCId() throws Exception {
    when(heanCommentRepository.findHeanCommentByCommmentId("1")).thenReturn(heanComment1);
    assertEquals(heanComment1, heanCommentServiceImple.findHeanCommentByCId("1"));
  }

  /**
   * Method: saveComment(HeanComment newComment)
   */
  @Test
  public void testSaveComment() throws Exception {
    when(heanRepository.findByHId("1")).thenReturn(hean1);

    when(heanCommentRepository.findHeanCommentByCommmentId("3")).thenReturn(heanComment3);
    heanComment1.setCommmentId("1");
    heanComment2.setCommmentId("2");
    heanComment3.setCommmentId("3");
    when(heanRepository.save(hean1)).thenReturn(hean1);
    assertSame(0, heanCommentServiceImple.saveComment(heanComment2));
    assertSame(0, heanCommentServiceImple.saveComment(heanComment1));
  }

  /**
   * Method: getComJSONObject(String cId)
   */
  @Test
  public void testGetComJSONObject() throws Exception {
    when(heanCommentServiceImple.findHeanCommentByCId("1")).thenReturn(heanComment1);
    when(heanCommentServiceImple.findHeanCommentByCId("2")).thenReturn(heanComment2);
    when(heanCommentServiceImple.findHeanCommentByCId("3")).thenReturn(heanComment3);
    when(userInfoService.getSimpleInfo(1L)).thenReturn(new JSONObject() {{
      put("username", "test1");
      put("uId", 1L);
    }});

    assertEquals(heanComment1.getContent(),
        heanCommentServiceImple.getComJSONObject("1").getString("content"));
    assertEquals(heanComment1.getContent(),
        heanCommentServiceImple.getComJSONObject("2").getString("content"));
  }

  /**
   * Method: findAllByUId(Long owner)
   */
  @Test
  public void testFindAllByUId() throws Exception {
    when(heanCommentRepository.findAllByUId(1L)).thenReturn(new ArrayList<HeanComment>(){{add(heanComment1);add(heanComment2);add(heanComment3);}});
    when(userInfoService.getSimpleInfo(1L)).thenReturn(new JSONObject() {{
      put("username", "test1");
      put("uId", 1L);
    }});
    when(heanRepository.findByHId("1")).thenReturn(hean1);
    when(heanCommentRepository.findHeanCommentByCommmentId("3")).thenReturn(heanComment3);
    assertEquals("test",heanCommentServiceImple.findAllByUId(1L).getJSONObject(0).getString("content"));
  }


}
