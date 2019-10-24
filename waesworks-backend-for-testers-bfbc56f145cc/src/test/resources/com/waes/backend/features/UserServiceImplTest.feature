@AllScenarios
Feature: As a client I should receive correct response when valid request provided PART-2 

	
		
@try5 
Scenario: Successfully log in a user 
	Given user information has captured 
		|is_admin	|name	 	|password			|username	|baseURI											  |
		|true		|An Admin	|secret_password	|admin		|http://localhost:8081/waesheroes/api/v1/users/all	  |
		
	When user logins successfully 
	Then validate the number of user data has received and print the results 
	
	
@try6 
Scenario: Successfully update an existing user 
	Given user information has captured 
		|id |name			 |username	|email 					|superpower 						|dateOfBirth 	|isAdmin |
		|2	|Zuper Dooper Dev|dev		|zd.dev@wearewaes.com	|Debug a repellent factory storage. |1999-10-10		|false	 |
	When an autorized user updates the captured data 
		|id |name			 |username	|email 					|superpower 						|dateOfBirth 	|isAdmin |password|
		|2	|Zuper Dooper Dev|dev		|zd.dev@wearewaes.com	|A new power.						|1999-10-10		|false	 |wizard  |
	Then updated data should be as expected 
	
	
	
@try7 
Scenario: Successfully delete an existing user 
	Given user information has captured 
		|id |name			 |username	|email 					|superpower 						|dateOfBirth 	|isAdmin |
		|4	|New User		 |new_user	|new.user@wearewaes.com	|Kamehameha.					    |1984-09-18  	|false	 |
		
		
	Given creates new user if needed 
		|name			 |username	|email 					|superpower 						|dateOfBirth 	|isAdmin |password|
		|New User		 |new_user	|new.user@wearewaes.com	|Kamehameha.					    |1984-09-18  	|false	 |wololo  |
		
	When data is deleted for by an authorized user 
		|username|
		|tester  |
	Then validate the response for removal of the user 
	
	
@try8 
Scenario: Successfully retrieve existing users 
	Given user information has captured 
		|is_admin	|name	 	|password			|username	|
		|true		|An Admin	|secret_password	|admin		|
	When user logins successfully 
	Then validate below user detais from response body 
		|id |name			 |username	|email 					|superpower 						|dateOfBirth 	|isAdmin |
		|2	|Zuper Dooper Dev|dev		|zd.dev@wearewaes.com	|Debug a repellent factory storage. |1999-10-10		|false	 |
		
		
		
		
