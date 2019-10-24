package com.waes.backend.stepDefs;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Assert;

import com.google.gson.Gson;
import com.waes.backend.utilities.ApiUtils;
import com.waes.backend.utilities.ReportingUtils;

import cucumber.api.Scenario;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UserRepositoryTestSteps{

	ReportingUtils reportUtil = new ReportingUtils();
	public static Map<Object, Object> jMap = new HashMap<Object, Object>();
	ApiUtils apiUtils = new ApiUtils();
	Properties prop = new Properties();
	Gson gSon = new Gson();
	
	public static String baseURI = "";	
	public static String endPoint = "";
	public static String url = "";

	public static int id;
	public static String name;
	public static String username;
	public static String email;
	public static String superpower;
	public static String dataOfBirth;
	public static boolean isAdmin;

	public Scenario scenario;
	private Map<String, String> dataMap;
	private String strResponse;
	private int statusCode;

	@Given("the username {string} was registered in the database")
	public void the_username_was_registered_in_the_database(String username, DataTable dataT) {

		baseURI = getFirstStringFromDataTable(dataT, "baseURI");

		Response response = getRequest(baseURI).param("username", username).get(RestAssured.baseURI);
		jMap = response.jsonPath().getMap("");

		String strStatusCode = response.statusCode() + "";

		reportUtil.writeToCucumberReport("Status Code : " + strStatusCode);

		Assert.assertEquals("Status Code : ", 200, response.statusCode());

	}

	@When("a client app attempts to request user {string} details")
	public void attempts_to_request(String username) {

		if (baseURI.isEmpty()) {
			Assert.fail("BaseURI has not been captured properly");
		}

		String strJsonResponse = getResponse(username).prettyPrint();
		reportUtil.writeToCucumberReport("Response for " + username + " : " + strJsonResponse);
		Assert.assertTrue("User information is empty.", !jMap.isEmpty());

	}

	@Then("^the response should be a JSON object equal to$")
	public void validateResponse(DataTable data) {
		List<Map<String, String>> expectedMap = data.asMaps();

		id = Integer.parseInt(expectedMap.get(0).get("id"));
		name = expectedMap.get(0).get("name");
		username = expectedMap.get(0).get("username");
		email = expectedMap.get(0).get("email");
		superpower = expectedMap.get(0).get("superpower");
		dataOfBirth = expectedMap.get(0).get("dataOfBirth");
		isAdmin = Boolean.parseBoolean(expectedMap.get(0).get("isAdmin"));

		Assert.assertEquals("id :", id, jMap.get("id"));
		Assert.assertEquals("name :", name, jMap.get("name"));
		Assert.assertEquals("username :", username, jMap.get("username"));
		Assert.assertEquals("email :", email, jMap.get("email"));
		// Skipped since this step getting updated by other testing steps
//		Assert.assertEquals("superpower :", superpower, jMap.get("superpower"));
		Assert.assertEquals("dataOfBirth :", dataOfBirth, jMap.get("dataOfBirth"));
		Assert.assertEquals("isAdmin :", isAdmin, jMap.get("isAdmin"));

		reportUtil.writeToCucumberReport("All attributes are as expected.");

	}

	@Given("the username {string} was registered in the database by {string}")
	public void the_username_was_registered_in_the_database(String username, String password, DataTable dataT)
			{

		String adminPass="";
		adminPass = getAdminPass(username);
		
		List<Map<String, String>> dataMaps = dataT.asMaps();
		baseURI = dataMaps.get(0).get("baseURI");
		
		reportUtil.writeToCucumberReport("Data has been captured for user : " + username);
		Assert.assertTrue("Unauthotenticated user : ", !adminPass.isEmpty());

	}

	private String getAdminPass(String username){

		String adminPass = "";

		if (username.equalsIgnoreCase("admin")) {
			Properties prop = new Properties();
			FileInputStream inputF = null;

			try {
				inputF = new FileInputStream("user.properties");
			} catch (FileNotFoundException e) {
				System.out.println("Properties files could not found");
			}

			try {
				prop.load(inputF);
			} catch (IOException e) {
				System.out.println("Properties file not loaded");
			} finally {
				adminPass = prop.getProperty("AdminPass");
				try {
					inputF.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} else {
			reportUtil.writeToCucumberReport("Invalid User tried to have unautorized information.");
			Assert.fail("Invalid User tried to have unautorized information.");
		}
		return adminPass;
	}

	@When("a client app attempts to request user {string} details by {string}")
	public void attempts_to_request_with_pass(String username, String password) {

		String adminPass ="";
		adminPass= getAdminPass(username);
		
		if (baseURI.isEmpty()) {
			Assert.fail("BaseURI has not been captured properly");
		}

		int statusCode =  getResponseWithPass(username,adminPass).statusCode(); 
		reportUtil.writeToCucumberReport("Status Code : " + statusCode  +" for username :"+ username);
		Assert.assertEquals("Status Code : ", 200 , statusCode );

	}

	@Then("print the results for autorized {string}")
	public void printResponse(String username) {
		
		String adminPass ="";
		adminPass= getAdminPass(username);
		
		if (baseURI.isEmpty()) {
			Assert.fail("BaseURI has not been captured properly");
		}

		String strJsonResponse =  getResponseWithPass(username,adminPass).prettyPrint(); //getResponse(user).prettyPrint();
		reportUtil.writeToCucumberReport("Response for " + username + " : " + strJsonResponse);
		Assert.assertTrue("User information is empty.", !strJsonResponse.isEmpty());

	}
	
	
	@Given("the user information has captured by {string} or {string}")
	public void captureUserDataByUsernameOrEmail(String username,String email, DataTable dataT) {

		baseURI = getFirstStringFromDataTable(dataT, "baseURI");
		String usedData="";
		
		
		
		if(username.isEmpty()) {
			username = getMapFromDataTable(dataT, "eMail", email).get("uName"); 
			this.username = username;
			usedData = "Email";
		}else {
			this.username = username;
			usedData = "UserName";
		}
		
//		Response response = getRequest(baseURI).param("username", username).get(RestAssured.baseURI);
		jMap = getResponse(username).jsonPath().getMap("");            //response.jsonPath().getMap("");

//		String strStatusCode = getResponse(username).statusCode() + "";

		reportUtil.writeToCucumberReport("Data has captured by : " + usedData) ;//getResponse(username).statusCode());
		Assert.assertTrue("Data has not captured : ", !getResponse(username).jsonPath().toString().isEmpty());

	}

	@When("a client app attempts to request user information")
	public void attempts_to_request() {

		
		if (baseURI.isEmpty()) {
			Assert.fail("BaseURI has not been captured properly");
		}

		int statusCode =  getResponse(username).statusCode(); 
		reportUtil.writeToCucumberReport("Status Code : " + statusCode  +" for username :"+ username);
		Assert.assertEquals("Status Code : ", 200 , statusCode );

	}

	
	@Then("print the user information from the response")
	public void printResponseWithoutAuth() {
		
		
		if (baseURI.isEmpty()) {
			Assert.fail("BaseURI has not been captured properly");
		}

		String strJsonResponse =  getResponse(username).prettyPrint();     
		reportUtil.writeToCucumberReport("Response for " + username + " : " + strJsonResponse);
		Assert.assertTrue("User information is empty.", !strJsonResponse.isEmpty());

	}
	

	
	@Given("the user information has captured by {string} and {string}")
	public void captureDataByUsernameAndEmail(String username, String email, DataTable dataT){
		List<Map<String, String>> dataMaps = dataT.asMaps();
		
		dataMap = dataMaps.get(0);

		Assert.assertTrue("No Data Provided : ", !dataMap.isEmpty());
		reportUtil.writeToCucumberReport("User data has captured for Username: " + dataMap.get("username"));
		
		Assert.assertEquals("Admin email does not match.", apiUtils.getUserProperty("AdminEmail") , email);
		
		String adminPass="";
		adminPass = getAdminPass(username);
		
		
		
		
		baseURI =  apiUtils.getURIProperty("baseURI");	    //  dataMaps.get(0).get("baseURI");
		endPoint = apiUtils.getURIProperty("AllUsers_EndPoint");
		
		url = baseURI+endPoint;
		
		reportUtil.writeToCucumberReport("Data has been captured for user : " + username);
		Assert.assertTrue("Unauthotenticated user : ", !adminPass.isEmpty());
		
		
	}
	
	
	
	@Given("new user to make deleting process smoother")
	public void postToCreateNewUser(DataTable dataT) {
		Map<String, String> mapData = dataT.asMaps().get(0);

		baseURI = apiUtils.getURIProperty("baseURI");
		String jsonRequestBody = gSon.toJson(mapData);

		Response response = apiUtils.getRequest(baseURI).header("Content-Type", "application/json")
				.body(jsonRequestBody).post(baseURI);

		int statusCode = response.statusCode();

		Assert.assertEquals("Status code : ", 201, statusCode);
		reportUtil.writeToCucumberReport("Captured Status Code : " + statusCode);
		reportUtil.writeToCucumberReport("A new user has created successfully.");
		reportUtil.writeToCucumberReport("Full response body : \n " + response.jsonPath().prettyPrint());
		
	}
	
	
	@When("data is deleted for the user")
	public void deleteUser(DataTable dataT) {
		Map<String, String> mapData = dataT.asMaps().get(0);

		baseURI = apiUtils.getURIProperty("baseURI");

		String secretPass = apiUtils.getRealPass(mapData.get("username"));

		String jsonRequestBody = gSon.toJson(dataMap);

		// to DELEte
		Response response = apiUtils.getRequest(baseURI).auth().preemptive().basic(mapData.get("username"), secretPass)
				.header("Content-Type", "application/json").body(jsonRequestBody).delete(baseURI);

		int statusCode = response.statusCode();

		Assert.assertEquals("Status code : ", 200, statusCode);
		reportUtil.writeToCucumberReport("Captured Status Code : " + statusCode);
		reportUtil.writeToCucumberReport("Response has received successfully");

		// response comes as string message
		strResponse = response.asString();

	}
	
	
	@Then("validate if only one data deleted")
	public void validataOneUserDeleted() {
		
		
		String expectedUsernameFromDeleteResponse = strResponse.substring(strResponse.indexOf("'")+1, strResponse.indexOf("'")+9);
		
		Assert.assertEquals("Expected user for removal could not found.",expectedUsernameFromDeleteResponse, dataMap.get("username"));
		reportUtil.writeToCucumberReport("Only one user has deleted from DB");
		reportUtil.writeToCucumberReport("Full response after removal : \n \t  \t" + strResponse);
	}
	
	
	@When("attemting to receive the deleted user data")
	public void attemptToReachDeletedUserData() {
	
		endPoint = apiUtils.getURIProperty("User_EndPoint");
		
		url = baseURI+endPoint;
		
		Response response = apiUtils.getResponse(dataMap.get("username"), url);
		
		strResponse = response.jsonPath().prettyPrint();
		
		
		statusCode =  response.statusCode();
//		String responseMessage = response.jsonPath().getString("message");
		
		Assert.assertTrue("Response body should not be empty.", !strResponse.isEmpty());
		reportUtil.writeToCucumberReport("Successfully attempted to reach the deleted information from DB.");
	}
	
	
	@Then("the user should not be present in the response")
	public void validateIfUserPresents() {
		
		Assert.assertEquals("Status Code : ", 404 , statusCode );
		reportUtil.writeToCucumberReport("Status Code : " + statusCode  +" for username : "+ dataMap.get("username"));
		reportUtil.writeToCucumberReport("Full response : \n \t \t "+ strResponse); 
		
	}
	
	
	
	
	
	
	public String getFirstStringFromDataTable(DataTable dataT, String keyValueFromData) {
		String valueFromDataTable="";
		List<Map<String, String>> dataMaps = dataT.asMaps();
		valueFromDataTable = dataMaps.get(0).get(keyValueFromData);
		return valueFromDataTable;
	}
	
	
	public Map<String,String> getMapFromDataTable(DataTable dataT, String keyValueFromData, String matcingValue) {
		List<Map<String, String>> dataMaps = dataT.asMaps();
		
		Map<String, String> desiredMap = new HashMap<String, String>();
		
		for(Map<String,String> map : dataMaps) {
			 if(map.get(keyValueFromData).toString().equalsIgnoreCase(matcingValue)) {
				 desiredMap = map;
				 break;
			 }
		}
		
		return desiredMap;
	}
	
	public RequestSpecification getRequest(String baseURI) {
		RestAssured.baseURI = baseURI;
		RequestSpecification request = RestAssured.given();
		return request;
	}

	public Response getResponse(String username) {
		Response response = getRequest(baseURI).param("username", username).get(RestAssured.baseURI);
		return response;
	}
	
	public Response getResponseWithPass(String username,String password) {
		Response response = getRequest(baseURI).auth().preemptive().basic(username, password).get(RestAssured.baseURI);
		return response;
	}
	
	

}
