package com.waes.backend.stepDefs;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;

public class BaseClass {

	public static Scenario scenario;
	
	
	@Before
	public void before(Scenario scenario) {
	    this.scenario = scenario;
	}
	
	@After
	public void after(Scenario scenario) {
		
		
	}
	
	
	public void embedJSONBodyWithingCucumberReport(String incomingResponseBody, String textToReport) {
		
		try {
			String JSONBody = incomingResponseBody;
			byte[] objToEmbed= JSONBody.getBytes();
			
			scenario.embed(objToEmbed, "application/json");
			scenario.write("The Json file has been embed : " + textToReport );
		} catch (Exception e) {
			System.out.println("Application failed to embed the file content with the cucumber report.");
			
		}
		
	}
	
	
}
