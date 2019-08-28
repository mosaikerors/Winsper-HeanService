package com.mosaiker.heanservice.service.serviceImple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mosaiker.heanservice.entity.Hean;
import com.mosaiker.heanservice.entity.HeanComment;
import com.mosaiker.heanservice.entity.Marked;
import com.mosaiker.heanservice.repository.HeanRepository;
import com.mosaiker.heanservice.repository.MarkRepository;
import com.mosaiker.heanservice.service.HeanCommentService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * HeanServiceImple Tester.
 *
 * @author <DeeEll-X>
 * @version 1.0
 * @since <pre>July 4, 2019</pre>
 */
public class HeanServiceImpleTest {

  @Mock
  private HeanRepository heanRepository;
  @Mock
  private MarkRepository markRepository;
  @Mock
  private HeanCommentService heanCommentService;
  @InjectMocks
  private HeanServiceImple heanServiceImple;

  private Hean hean1 = new Hean(1L, new Date(), "hean1", 0.01, 0.01, 0.01, new ArrayList<String>());


  @Before
  public void before() throws Exception {
    MockitoAnnotations.initMocks(this);

  }

  @After
  public void after() throws Exception {
  }

  /**
   * Method: findHeansByUId(Long uId)
   */
  @Test
  public void testFindHeansByUId() throws Exception {
    Hean hean1 = new Hean(10000L, new Date(), "test hean1", 100.1, 100.1, 100.1, null);
    Hean hean2 = new Hean( 10000L, new Date(), "test hean2", 100.1, 100.1, 100.1, null);
    List<Hean> heanList = Arrays.asList(hean1, hean2);
    when(heanRepository.findAllByUId(10000L)).thenReturn(heanList);
    List<Hean> heanResult = heanServiceImple.findHeansByUId(10000L);
    assertEquals(heanResult, heanList);
  }

  /**
   * Method: findAllHeans()
   */
  @Test
  public void testFindAllHeans() throws Exception {
    Hean hean1 = new Hean(10000L, new Date(), "test hean1", 100.1, 100.1, 100.1, null);
    Hean hean2 = new Hean(10000L, new Date(), "test hean2", 100.1, 100.1, 100.1, null);
    List<Hean> heanList = Arrays.asList(hean1, hean2);
    Long loc = new Geohash().encode(100.1,100.1);
    when(heanRepository.findAllByGeoStrBetween(loc-250*250,loc+250*250)).thenReturn(heanList);
    List<Hean> heanResult = heanServiceImple.findAllHeans(100.1,100.1);
    assertEquals(heanList,heanResult);
  }

  /**
   * Method: deleteByHId(String hId)
   */
  @Test
  public void testDeleteByHId() throws Exception {
    Hean hean1 = new Hean( 10000L, new Date(), "test hean1", 100.1, 100.1, 100.1, null);
    when(heanRepository.deleteByHId("hean1")).thenReturn(true);
    Boolean result = heanServiceImple.deleteByHId("hean1");
    verify(heanRepository).deleteByHId("hean1");
    assertEquals(true,result);
    verifyNoMoreInteractions(heanRepository);

  }

  /**
   * Method: upload(Hean hean)
   */
  @Test
  public void testUpload() throws Exception {
    Hean hean1 = new Hean( 10000L, new Date(), "test hean1", 100.1, 100.1, 100.1, null);
    when(heanRepository.save(hean1)).thenReturn(hean1);
    Hean result = heanServiceImple.upload(hean1);
    verify(heanRepository).save(hean1);
    assertEquals(hean1,result);
    verifyNoMoreInteractions(heanRepository);
  }

  @Test
  public void testFindAllMarkedByUId() throws Exception{
    List<String> stars = new ArrayList<String>(){{add("1");}};
    List<Hean> heans = new ArrayList<Hean>(){{add(hean1);}};
    when(markRepository.findByUId(1L)).thenReturn(new Marked(1L,stars));
    when(heanRepository.findByHId("1")).thenReturn(hean1);
    assertEquals(heans,heanServiceImple.findAllMarkedByUId(1L));

  }

  @Test
  public void testSetLike() throws  Exception{
    List<Long> likes = new ArrayList<Long>();
    when(heanRepository.findLikeUIdsByHId("1")).thenReturn(likes);
    when(heanRepository.findByHId("1")).thenReturn(hean1);
    assertTrue(heanServiceImple.setLike("1",1L));
  }

  @Test
  public void testSetStar() throws  Exception{
    List<Long> stars = new ArrayList<Long>();
    when(heanRepository.findStarUIdsByHId("1")).thenReturn(stars);
    when(heanRepository.findByHId("1")).thenReturn(hean1);
    when(markRepository.findByUId(1L)).thenReturn(new Marked(1L,new ArrayList<>()));
    assertTrue(heanServiceImple.setStar("1",1L));
  }

  @Test
  public void testCancelStar() throws  Exception{
    List<Long> stars = new ArrayList<Long>(){{add(1L);}};
    when(heanRepository.findStarUIdsByHId("1")).thenReturn(stars);
    when(heanRepository.findByHId("1")).thenReturn(hean1);
    when(markRepository.findByUId(1L)).thenReturn(new Marked(1L,new ArrayList<String>(){{add("1");}}));
    assertFalse(heanServiceImple.cancelStar("1",1L));
  }

  @Test
  public void findHeanByHId() throws Exception{
    when(heanRepository.findByHId("1")).thenReturn(hean1);
    assertEquals(hean1,heanServiceImple.findHeanByHId("1"));
  }

  @Test
  public void allComments() throws Exception{
    HeanComment heanComment = new HeanComment("1",1L,"test",new Date().getTime(),null,new ArrayList<String>());
    hean1.addComment("1");
    when(heanRepository.findByHId("1")).thenReturn(hean1);
    when(heanCommentService.getComJSONObject("1")).thenReturn(new JSONObject());
    assertEquals(new JSONArray(){{add(new JSONObject());}},heanServiceImple.allComments("1"));
  }

}

