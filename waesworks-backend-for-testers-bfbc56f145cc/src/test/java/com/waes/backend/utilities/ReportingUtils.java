package com.waes.backend.utilities;

import com.waes.backend.stepDefs.BaseClass;

import cucumber.api.Scenario;

public class ReportingUtils extends BaseClass {
	
	
	Scenario scenario;
	
	public ReportingUtils(){
		this.scenario = super.scenario;
	}
	
	
	public void embedJSONBodyWithingCucumberReport(String incomingResponseBody, String textToReport) {
		
		try {
			String JSONBody = incomingResponseBody;
			byte[] objToEmbed= JSONBody.getBytes();
			
			scenario.embed(objToEmbed, "application/json");
			scenario.write(textToReport);
		} catch (Exception e) {
			System.out.println("Application failed to embed the file content with the cucumber report.");
			
		}
		
	}
	
public void writeToCucumberReport(String textToReport) {
		
		try {
			
			scenario.write(textToReport );
		} catch (Exception e) {
			System.out.println("Application failed to embed the file content with the cucumber report.");
			
		}
		
	}

	
	
	
	
	
	
}
