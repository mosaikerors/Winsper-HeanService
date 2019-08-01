package com.mosaiker.heanservice.service.serviceImple;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mosaiker.heanservice.entity.Contribution;
import com.mosaiker.heanservice.repository.ContributionRepository;
import java.util.ArrayList;
import java.util.Date;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
* ContributionServiceImple Tester.
*
* @author <DeeEll-X>
* @since <pre>Aug 1, 2019</pre>
* @version 1.0
*/
public class ContributionServiceImpleTest {
  @Mock
  private ContributionRepository contributionRepository;
  @InjectMocks
  private ContributionServiceImple contributionServiceImple;
  private Contribution contribution = new Contribution(1L, "1", new Date().getTime());

@Before
public void before() throws Exception {
  MockitoAnnotations.initMocks(this);
}

@After
public void after() throws Exception {
}

/**
*
* Method: findContributionsByDate(Long date)
*
*/
@Test
public void testFindContributionsByDate() throws Exception {
  when(contributionRepository.findAllByDateAfter(anyObject())).thenReturn(new ArrayList<Contribution>(){{add(contribution);}});
  assertEquals(new ArrayList<Contribution>(){{add(contribution);}},contributionServiceImple.findContributionsByDate(new Date().getTime()));
}

/**
*
* Method: findContributionsByHId(String hId)
*
*/
@Test
public void testFindContributionsByHId() throws Exception {
  when(contributionRepository.findAllByHId("1")).thenReturn(new ArrayList<Contribution>(){{add(contribution);}});
  assertEquals(new ArrayList<Contribution>(){{add(contribution);}},contributionServiceImple.findContributionsByHId("1"));
}

/**
*
* Method: findContributionByCId(Long cId)
*
*/
@Test
public void testFindContributionByCId() throws Exception {
  when(contributionRepository.findByCId(1L)).thenReturn(contribution);
  assertEquals(contribution,contributionServiceImple.findContributionByCId(1L));
}

/**
*
* Method: addNewContribution(String hId)
*
*/
@Test
public void testAddNewContribution() throws Exception {
  contributionServiceImple.addNewContribution("1");
  verify(contributionRepository).save(anyObject());
}


}
