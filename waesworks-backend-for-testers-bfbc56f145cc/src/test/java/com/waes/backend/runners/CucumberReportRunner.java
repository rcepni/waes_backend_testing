package com.waes.backend.runners;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import cucumber.api.junit.Cucumber;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;

public class CucumberReportRunner extends Cucumber{

	private static final String PROJECT_NAME = "Backend for Testers";
	
	public CucumberReportRunner(Class clazz) throws InitializationError {
		super(clazz);
	}

	
	 @Override
	  public void run(RunNotifier notifier) {
	    super.run(notifier);
	    generateReport();
	  }
	
	 public static void generateReport() {

		    File reportOutputDirectory = new File("target");         
		    List<String> jsonFiles = new ArrayList<>();
		    jsonFiles.add("target/cucumber-report.json");

		    // set values from respective build tool
		    Configuration configuration = new Configuration(reportOutputDirectory, PROJECT_NAME);

		    ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
		    reportBuilder.generateReports();
		  }
	
}
