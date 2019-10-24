package com.waes.backend.stepDefs;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.junit.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.waes.backend.beans.Users;
import com.waes.backend.utilities.ApiUtils;
import com.waes.backend.utilities.ReportingUtils;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;

public class UserServiceImplTestSteps {

	ReportingUtils reportUtil = new ReportingUtils();
	ApiUtils apiUtils = new ApiUtils();
	Properties prop = new Properties();
	Gson gSon = new Gson();

	Map<String, String> dataMap = new HashMap<String, String>();
	ObjectMapper mapper = new ObjectMapper();
	String baseURI = "";
	String endPoint = "";
	String url = "";
	Users usersPojo;
	Users userPojo;
	List<Users> listUsersPojo;

	String strResponse = "";

	@Given("user information has captured")
	public void setUpData(DataTable dataT) {

		List<Map<String, String>> mapsData = apiUtils.getMapsFromDataTable(dataT);

		dataMap = mapsData.get(0);

		Assert.assertTrue("No Data Provided : ", !dataMap.isEmpty());
		reportUtil.writeToCucumberReport("User data has captured for Username: " + dataMap.get("username"));
	}

	@When("user logins successfully")
	public void loginAndGetAllUsersInfo() {

		baseURI = apiUtils.getURIProperty("baseURI");

		endPoint = apiUtils.getURIProperty("AllUsers_EndPoint");

		String uri = baseURI + endPoint;

		String secretPass = apiUtils.getRealPass(dataMap.get("username"));

		Response response = apiUtils.getResponseWithPass(dataMap.get("username"), secretPass, uri);
		int statusCode = response.statusCode();
		strResponse = response.jsonPath().prettyPrint();

		Type listType = new TypeToken<ArrayList<Users>>() {
		}.getType();
		listUsersPojo = new Gson().fromJson(strResponse, listType);

		Assert.assertEquals("Status code : ", 200, statusCode);
		reportUtil.writeToCucumberReport("Captured Status Code : " + statusCode);
		reportUtil.writeToCucumberReport("Response has received successfully");

	}

	@Then("validate the number of user data has received and print the results")
	public void validateNumberOfUsersRespons() {

		int numUsers = listUsersPojo.size();

		Assert.assertTrue("No user information has captured : ", (numUsers > 0));
		reportUtil.writeToCucumberReport(numUsers + " Users data has received.");
		reportUtil.writeToCucumberReport(strResponse);

	}

	@When("an autorized user updates the captured data")
	public void loginAndGetUserInfoWithPassw(DataTable dataT) {
		Map<String, String> mapData = dataT.asMaps().get(0);

		baseURI = apiUtils.getURIProperty("baseURI");

		String secretPass = apiUtils.getRealPass(mapData.get("username"));

		// *** having an immutable map issue, so copping the password from datatable for
		// now
//		mapData.put("password", secretPass);

		String jsonRequestBody = gSon.toJson(mapData);

		Response response = apiUtils.getRequest(baseURI).auth().preemptive()
				.basic(mapData.get("username"), mapData.get("password")).header("Content-Type", "application/json")
				.body(jsonRequestBody).put(baseURI);
		// apiUtils.getResponseWithPassAndJson(dataMap.get("username"), secretPass ,
		// baseURI, jsonStr);

		int statusCode = response.statusCode();
		strResponse = response.jsonPath().prettyPrint();

		// convertion from Json to Pojo
		userPojo = gSon.fromJson(strResponse, Users.class);

		Assert.assertEquals("Status code : ", 200, statusCode);
		reportUtil.writeToCucumberReport("Captured Status Code : " + statusCode);
		reportUtil.writeToCucumberReport("Response has received successfully");

	}

	@Then("updated data should be as expected")
	public void validateUpdatedResponseBody() {

		if (!dataMap.get("superpower").equalsIgnoreCase(userPojo.getSuperpower())) {
			reportUtil.writeToCucumberReport("Superpower has changed to -> " + userPojo.getSuperpower());
		}
		if (!dataMap.get("email").equalsIgnoreCase(userPojo.getEmail())) {
			reportUtil.writeToCucumberReport("Email has changed to -> " + userPojo.getEmail());
		}
		if (!(dataMap.get("isAdmin") + "").equalsIgnoreCase(userPojo.getIsAdmin() + "")) {
			reportUtil.writeToCucumberReport("IsAdmin has changed to -> " + userPojo.getIsAdmin());
		}

		Assert.assertTrue("Response body could not found.", !strResponse.isEmpty());
		reportUtil.writeToCucumberReport(
				"Updated user information for username : " + dataMap.get("username") + "\n" + strResponse);

	}

	@Given("creates new user if needed")
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

	}

	@When("data is deleted for by an authorized user")
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

	@Then("validate the response for removal of the user")
	public void validataRemoval() {

		Assert.assertTrue("Response body could not found.", !strResponse.isEmpty());
		reportUtil.writeToCucumberReport("Captured full response : \n " + strResponse);

	}

	@Then("validate below user detais from response body")
	public void validateUserDetails(DataTable dataT) {
		Map<String, String> theUserMap = dataT.asMaps().get(0);

		int numUsers = listUsersPojo.size();

		Assert.assertTrue("No user information has captured : ", (numUsers > 0));
		reportUtil.writeToCucumberReport(numUsers + " Users data has received.");

		for (Users user : listUsersPojo) {
			if (user.getUsername().equalsIgnoreCase(theUserMap.get("username"))) {

				Assert.assertEquals("id :", user.getId()+"", theUserMap.get("id"));
				Assert.assertEquals("name :", user.getName(), theUserMap.get("name"));
				Assert.assertEquals("username :", user.getUsername(), theUserMap.get("username"));
				Assert.assertEquals("email :", user.getEmail(), theUserMap.get("email"));
				
				// *** This step has ignored because we update the data on previous PUT step
//				Assert.assertEquals("superpower :", user.getSuperpower(), theUserMap.get("superpower"));
				
				Assert.assertEquals("dateOfBirth :", user.getDateOfBirth(), theUserMap.get("dateOfBirth"));
				Assert.assertEquals("isAdmin :", user.getIsAdmin()+"", theUserMap.get("isAdmin"));
				reportUtil.writeToCucumberReport("Username : " + theUserMap.get("username") +" data has matched with full users data from Response.");
				break;
			}
		}
		
		reportUtil.writeToCucumberReport("Full response for all users : \n"+ strResponse);

	}

}
