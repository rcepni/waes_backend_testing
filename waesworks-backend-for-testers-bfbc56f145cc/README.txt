
How to Execute the test cases

1) Find CukesRunner.java file  locating as shown ("waesworks-backend-for-testers-bfbc56f145cc/src/test/java/com/waes/backend/runners/CukesRunner.java")
	- Everything in the class has designed the to run all the test cases at ones and generate a big clean cucumber html report, locating as shown
		("/waesworks-backend-for-testers-bfbc56f145cc/target/cucumber-html-reports/overview-features.html")
	- If you are willing to run a specigic test case, just change the tag name from (@AllScenarios) to (@try1) or create your own tags in the feature files
	- All the test cases has written in Cucumber by the help of Gherkin language, locating as shown 
		("/waesworks-backend-for-testers-bfbc56f145cc/src/test/resources/com/waes/backend/features/UserRepositoryTest.feature")

2) Run the file as Junit Test which triggers the Cucumber.class and so on

3) Find Cucumber Html Report at location ("/waesworks-backend-for-testers-bfbc56f145cc/target/cucumber-html-reports/overview-features.html")

4) Open "overview-features.html" file with your desired browser to see very readable Cucumber report.
	- Please click on Features, scenarios, steps, outputs for the deep details


