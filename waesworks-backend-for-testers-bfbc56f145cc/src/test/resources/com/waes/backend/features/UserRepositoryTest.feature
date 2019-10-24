@AllScenarios
Feature: As a client I should receive correct response when valid request provided PART-1

		@try1
Scenario Outline: Find a user in repository by username 
	Given the username "<username>" was registered in the database 
		|baseURI											  |
		|http://localhost:8081/waesheroes/api/v1/users/details|
	When a client app attempts to request user "<username>" details 
	Then the response should be a JSON object equal to 
		|id |name			 |username	|email 					|superpower 						|dateOfBirth 	|isAdmin |
		|2	|Zuper Dooper Dev|dev		|zd.dev@wearewaes.com	|Debug a repellent factory storage. |1999-10-10		|false	 |
		
	Examples: 
		|id |name			 |username	|email 					|superpower 						|dateOfBirth 	|isAdmin |
		|2	|Zuper Dooper Dev|dev		|zd.dev@wearewaes.com	|Debug a repellent factory storage. |1999-10-10		|false	 |
		
		
		
		
		@try2
Scenario Outline: Find a user in repository by username and password 
	Given the username "<username>" was registered in the database by "<password>" 
		|baseURI											  |
		|http://localhost:8081/waesheroes/api/v1/users/all	  |
	When a client app attempts to request user "<username>" details by "<password>" 
	Then print the results for autorized "<username>" 
	
	Examples: 
		|is_admin	|name	 	|password			|username	|
		|true		|An Admin	|secret_password	|admin		|
		
		
@try3 
Scenario Outline: Find a user in repository by username or email 
	Given the user information has captured by "<username>" or "<email>" 
		|baseURI											  |uName	| eMail					 |
		|http://localhost:8081/waesheroes/api/v1/users/details|dev     	| zd.dev@wearewaes.com   |
		|http://localhost:8081/waesheroes/api/v1/users/details|tester   | as.tester@wearewaes.com|
	When a client app attempts to request user information 
	Then print the user information from the response 
		
	Examples: 
		|id |name			 	|username	|email 					|superpower 						|dateOfBirth 	|isAdmin |
		|2	|Zuper Dooper Dev	|			|zd.dev@wearewaes.com	|Debug a repellent factory storage. |1999-10-10		|false	 |
		|3	|Al Skept-Cal Tester|tester		|						|Voltage AND Current.				|2014-07-15		|false	 |
		
		
		
		
	@try4  
Scenario Outline: Delete a user in repository by username and email 
	Given the user information has captured by "<username>" and "<email>" 
		|id |name			 |username	|email 					|superpower 	|dateOfBirth 	|isAdmin |
		|4	|New User		 |new_user	|new.user@wearewaes.com	|Kamehameha.	|1984-09-18  	|false	 |

	Given new user to make deleting process smoother
	|name			 |username	|email 					|superpower 	|dateOfBirth 	|isAdmin |password|
	|New User		 |new_user	|new.user@wearewaes.com	|Kamehameha.	|1984-09-18  	|false	 |wololo  |
	When data is deleted for the user 
	|username|
	|tester  |
	Then validate if only one data deleted
	When attemting to receive the deleted user data 
	Then the user should not be present in the response
		
	Examples:
		|is_admin	|name	 	|password			|username	|email			      |
		|true		|An Admin	|secret_password	|admin		|a.admin@wearewaes.com|
	
	 
			
			
			
			
			
