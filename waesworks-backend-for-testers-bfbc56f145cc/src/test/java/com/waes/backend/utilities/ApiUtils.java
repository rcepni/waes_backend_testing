package com.waes.backend.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Assert;

import io.cucumber.datatable.DataTable;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ApiUtils {

	ReportingUtils reportUtil = new ReportingUtils();

	public String getFirstStringFromDataTable(DataTable dataT, String keyValueFromData) {
		String valueFromDataTable = "";
		List<Map<String, String>> dataMaps = dataT.asMaps();
		valueFromDataTable = dataMaps.get(0).get(keyValueFromData);
		return valueFromDataTable;
	}

	public Map<String, String> getMapFromDataTable(DataTable dataT, String keyValueFromData, String matcingValue) {
		List<Map<String, String>> dataMaps = dataT.asMaps();

		Map<String, String> desiredMap = new HashMap<String, String>();

		for (Map<String, String> map : dataMaps) {
			if (map.get(keyValueFromData).toString().equalsIgnoreCase(matcingValue)) {
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

	public Response getResponse(String username, String baseURI) {
		Response response = getRequest(baseURI).param("username", username).get(RestAssured.baseURI);
		return response;
	}

	public Response getResponseWithPass(String username, String password, String baseURI) {
		Response response = getRequest(baseURI).auth().preemptive().basic(username, password).get(RestAssured.baseURI);
		return response;
	}

	public List<Map<String, String>> getMapsFromDataTable(DataTable dataT) {
		return dataT.asMaps();
	}

	public String getRealPass(String username) {
		String adminPass = "";
		Properties prop = new Properties();

		if (username.equalsIgnoreCase("admin")) {

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

		} else if (username.equalsIgnoreCase("dev")) {
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
				adminPass = prop.getProperty("DevPass");
				try {
					inputF.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} else if (username.equalsIgnoreCase("tester")) {
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
				adminPass = prop.getProperty("TesterPass");
				try {
					inputF.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}else {
			reportUtil.writeToCucumberReport("Invalid User tried to have unautorized information.");
			Assert.fail("Invalid User tried to have unautorized information.");
		}
		return adminPass;
	}

	
	
	public String getUserProperty(String desiredValue) {
		
		Properties prop = new Properties();
		String propertyPath = "user.properties";
		String value="";
		
		FileInputStream inputF = null;
		
		try {
			inputF = new FileInputStream(propertyPath);
		} catch (FileNotFoundException e) {
			System.out.println("Properties files could not found");
		}

		try {
			prop.load(inputF);
		} catch (IOException e) {
			System.out.println("Properties file not loaded");
		} finally {
			value = prop.getProperty(desiredValue);
			try {
				inputF.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return value;
	}
	
	
	
	public String getURIProperty(String propertyNAme) {
		
			FileInputStream inputF = null;
			Properties prop = new Properties();

			String value="";
			
			try {
				inputF = new FileInputStream("URI.properties");
			} catch (FileNotFoundException e) {
				System.out.println("Properties files could not found");
			}

			try {
				prop.load(inputF);
			} catch (IOException e) {
				System.out.println("Properties file not loaded");
			} finally {
				value = prop.getProperty(propertyNAme);
				try {
					inputF.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		
		return value;
	}

	public Response getResponseWithPassAndJson(String username, String password, String baseURI, String json) {

		Response response = getRequest(baseURI).param("username", username).body(json).get(RestAssured.baseURI);

		return response;
	}
	
	public Response getPutResponse(String username, String password, String baseURI, String json) {
		Response response = getRequest(baseURI).auth().preemptive().basic(username, password)
				.header("Content-Type", "application/json").put(baseURI); 
		return response;
	}

}
