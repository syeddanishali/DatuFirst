package com.datu.test;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.internal.runtime.StatefulKnowledgeSession;

import com.datu.patient.Patient;
import com.datu.patient.TileResponse;
import com.datu.patient.TileResponseEngagement;
import com.datu.result.ProgramEnrollmentAction;
import com.datu.result.RulesResult;

//Collect patient's initial information using MyInformation Survey
public class PI001_9Test extends RulesBaseTest {
  private StatefulKnowledgeSession ksession = null;
  private Patient patient;
  
	@Before
    public void setUp() {
        ksession = initializeKnowledgeSession();
        patient = new Patient();
    }
	
	@After
	public void tearDown() {
        if(ksession != null){
          ksession.dispose();
        }
    }
    
	@Test
	public void succesTest() {
	  System.out.println("Executing PI001_9Test.succesTest");
      TileResponseEngagement tileResponseEngagement = new TileResponseEngagement();
      tileResponseEngagement.setActionValue("Yes");
      
      TileResponse tileResponse = new TileResponse();
      tileResponse.setTileResponseId(34);
      tileResponse.getTileResponseEngagements().add(tileResponseEngagement);
    
      RulesResult rulesResult =  new RulesResult();
      patient.setRulesResult(rulesResult);
      patient.getTileResponses().add(tileResponse);
     
      ksession.insert(patient);
      ksession.fireAllRules();
      //List<DisplayCollection> dCollections = patient.getRulesResult().getDisplayCollections();
      boolean actualResult =  false;
    
      for(ProgramEnrollmentAction action : patient.getRulesResult().getProgramEnrollmentActions()){
        if(action.getProgramId() == 3 && action.getAction().equals("enroll")){
            actualResult = true;
            break;
        }
      }
      assertEquals("Positive condition", actualResult, true);
	}
	
	@Test
    public void failureTestTileResponseNot34() {
	  System.out.println("Executing PI001_9Test.failureTestTileResponseNot34");
      TileResponse tileResponse = new TileResponse();
      tileResponse.setTileResponseId(17);
    
      RulesResult rulesResult =  new RulesResult();
      patient.setRulesResult(rulesResult);
      patient.getTileResponses().add(tileResponse);
     
      ksession.insert(patient);
      ksession.fireAllRules();
      boolean actualResult =  false;
      for(ProgramEnrollmentAction action : patient.getRulesResult().getProgramEnrollmentActions()){
        if(action.getProgramId() == 3 && action.getAction().equals("enroll")){
            actualResult = true;
            break;
        }
      }
      assertEquals("Positive condition", actualResult, false);
    }
	
	@Test
    public void failureTestWithProgramEnrollments() {
	  System.out.println("Executing PI001_9Test.failureTestWithProgramEnrollments");
      TileResponse tileResponse = new TileResponse();
      tileResponse.setTileResponseId(34);
    
      RulesResult rulesResult =  new RulesResult();
      patient.setRulesResult(rulesResult);
      patient.getTileResponses().add(tileResponse);
     
      ProgramEnrollmentAction program = new ProgramEnrollmentAction();
      program.setProgramId(4);
      program.setAction("enroll22");
      patient.getRulesResult().getProgramEnrollmentActions().add(program);
      
      ksession.insert(patient);
      ksession.fireAllRules();
      //List<DisplayCollection> dCollections = patient.getRulesResult().getDisplayCollections();
      boolean actualResult =  false;

      for(ProgramEnrollmentAction action : patient.getRulesResult().getProgramEnrollmentActions()){
        if(action.getProgramId() == 3 && action.getAction().equals("enroll")){
            actualResult = true;
            break;
        }
      }
      assertEquals("Positive condition", actualResult, false);
    }
}