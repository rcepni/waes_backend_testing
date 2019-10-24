package com.waes.backend.runners;

import cucumber.api.CucumberOptions;
import org.junit.runner.RunWith;

//@RunWith(Cucumber.class)
@RunWith(CucumberReportRunner.class)
@CucumberOptions(
		plugin = {
				"pretty",
				"json:target/cucumber-report.json",
				"html:target/defaut-cucumber-report"
				},
		tags= {"@AllScenarios"},
		features= {"src/test/resources/com/waes/backend/features"}, 
		glue= {"com/waes/backend/stepDefs"}
		,dryRun = false
		)

public class CukesRunner {
	
//	mvn test -Dcucumber.options="--tags @try"
	
}
