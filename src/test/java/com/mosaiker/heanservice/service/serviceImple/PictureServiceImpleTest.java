package com.mosaiker.heanservice.service.serviceImple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.mosaiker.heanservice.entity.Picture;
import com.mosaiker.heanservice.repository.PictureRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.bson.types.Binary;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

/**
* PictureServiceImple Tester.
*
* @author <Authors name>
* @since <pre>���� 4, 2019</pre>
* @version 1.0
*/
public class PictureServiceImpleTest {
  @Mock
  private PictureRepository pictureRepository;
  @InjectMocks
  private PictureServiceImple pictureServiceImple;

@Before
public void before() throws Exception {
  MockitoAnnotations.initMocks(this);
}

@After
public void after() throws Exception {
}

/**
*
* Method: findPictureByPId(String pId)
*
*/
@Test
public void testFindPictureByPId() throws Exception {
  Picture pic1 = new Picture("pic1",new Binary("pic1".getBytes()),"JPEG",1L);
  when(pictureRepository.findPictureByPId("pic1")).thenReturn(pic1);
  Picture picResult=pictureServiceImple.findPictureByPId("pic1");
  assertEquals(pic1,picResult);

  when(pictureRepository.findPictureByPId("pic2")).thenReturn(null);
  when(pictureRepository.findPictureByPId("5d1ebb99e5b9b1ac58d09308")).thenReturn(pic1);
  Picture picResult2 = pictureServiceImple.findPictureByPId("pic2");
  verify(pictureRepository).findPictureByPId("pic2");
  assertEquals(pic1,picResult2);

}

/**
*
* Method: findPicturesByPIds(List<String> pIds)
*
*/
@Test
public void testFindPicturesByPIds() throws Exception {
  List<String> pIds = Arrays.asList("pic1","pic2","pic9");
  List<Picture> result = pictureServiceImple.findPicturesByPIds(pIds);
  assertNull(result);
  verifyNoMoreInteractions(pictureRepository);
}

/**
*
* Method: uploadPicture(MultipartFile file, String baseUrl)
*
*/
@Test
public void testUploadPicture() throws Exception {


  String filePath=this.getClass().getResource("").getPath()+"PictureServiceImpleTest.class";
  File file = new File(filePath);
  FileInputStream fileInputStream1 = new FileInputStream(file);
  FileInputStream fileInputStream2 = new FileInputStream(file);
  FileInputStream fileInputStream3 = new FileInputStream(file);
  FileInputStream fileInputStream4 = new FileInputStream(file);

  MultipartFile pic1 = new MockMultipartFile("pic1", "Mockpic1", "image/jpg", fileInputStream1);
  MultipartFile pic2 = new MockMultipartFile("pic2", "Mockpic2", "image/jpeg", fileInputStream2);
  MultipartFile pic3 = new MockMultipartFile("pic3", "Mockpic3", "image/png", fileInputStream3);
  MultipartFile pic4 = new MockMultipartFile("pic4", "Mockpic4", "image/gif", fileInputStream4);

    Picture picture1 = new Picture();
  picture1.setContent(new Binary(pic1.getBytes()));
  picture1.setContentType(pic1.getContentType());
  picture1.setSize(pic1.getSize());
  when(pictureRepository.save(picture1)).thenReturn(picture1);
  Picture savedFile1 = pictureRepository.save(picture1);
  String path1 = pictureServiceImple.uploadPicture(pic1, filePath);
  assertEquals(picture1, savedFile1);
  assertEquals(filePath + "null", path1);

  Picture picture2 = new Picture();
  picture2.setContent(new Binary(pic2.getBytes()));
  picture2.setContentType(pic2.getContentType());
  picture2.setSize(pic2.getSize());
  when(pictureRepository.save(picture2)).thenReturn(picture2);
  Picture savedFile2 = pictureRepository.save(picture2);
  String path2 = pictureServiceImple.uploadPicture(pic2, filePath);
  assertEquals(picture2, savedFile2);
  assertEquals(filePath + "null", path2);

  Picture picture3 = new Picture();
  picture3.setContent(new Binary(pic3.getBytes()));
  picture3.setContentType(pic3.getContentType());
  picture3.setSize(pic3.getSize());
  when(pictureRepository.save(picture3)).thenReturn(picture3);
  Picture savedFile3 = pictureRepository.save(picture3);
  String path3 = pictureServiceImple.uploadPicture(pic3, filePath);
  assertEquals(picture3, savedFile3);
  assertEquals(filePath + "null", path3);

  Picture picture4 = new Picture();
  picture4.setContent(new Binary(pic4.getBytes()));
  picture4.setContentType(pic4.getContentType());
  picture4.setSize(pic4.getSize());
try{
  pictureServiceImple.uploadPicture(pic4, filePath);
  fail("No exception thrown.");
}catch(Exception ex){
  IOException ex1 = new IOException();
    assertEquals(ex.getClass(),ex1.getClass());
  }



}


}
