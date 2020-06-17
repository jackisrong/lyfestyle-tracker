//selects the user sleep entries
"Select up.username, us.sleepTime from userPerson up, UserSleepEntry us WHERE up.username = us.username"

//selects the truncated sleep weeks, avg for those weeks and sum for those weeks for the entire month, grouped by their weeks
"select trunc(us.sleepdate, 'IW'), AVG(us.sleepTime), SUM(us.sleepTime) from UserSleepEntry us where us.username = " + username + " AND us.sleepdate >= TRUNC(SYSDATE,'mm') GROUP BY trunc(us.sleepdate, 'IW')"

//selects the user sleep times for the past week
"Select us.username, us.sleepTime from UserSleepEntry us WHERE us.username = " + username + " AND sleepDate >= TRUNC(SYSDATE, 'DY') AND us.sleepDate < TRUNC(SYSDATE, 'DY') + 7 ORDER BY us.sleepdate"


//Inserting into sleep entries acording to two spinner inputs
"INSERT INTO userSleepEntry VALUES ('"+ username + "', TO_DATE('" + dateResult.getText() +"', 'YYYY-MM-DD'), " + s1.getSelectedItem().toString() + ", " + s2.getSelectedItem().toString() +")"

//Updates the workout according to values from the user
"UPDATE Workout SET description = '" + workoutDesc.getText().toString() + "', caloriesburnt = " + workoutCaloriesBurnt.getText().toString() + ", timeworkout = " + workoutLength.getText().toString() + " WHERE workoutid = " + workoutId"

//selects the userPeople who match the given username
"SELECT * FROM People p, UserPerson up WHERE up.username = ' + username + ' AND p.username = up.username"

//selects the consultants who match the given username
'SELECT * FROM People p, Consultant c WHERE c.username = ' + username  + ' AND c.username = p.username'


//selecting the the exercise log entries that match a username and are sorted in descending order of log time
"SELECT ele.logtime, w.description, w.caloriesburnt, w.timeworkout FROM workout w, exerciselogentry ele, userexerciselog uel WHERE w.workoutid = ele.workoutid AND w.workoutid = uel.workoutid AND ele.logtime = uel.logtime AND uel.username = '"+ username + "' ORDER BY uel.logtime DESC"



//Selects the combination of consultant usernames and companies they work for ordered by their rating
"Select c.username, c.result, co.name FROM Consultant c, Company co, ConsultantWorksForCompany cw WHERE cw.consultantUsername = c.username AND co.companyID = cw.companyId AND c.username IN (SELECT consultantUsername FROM UserHiresConsultant WHERE userUsername = '" + mParam1 +"' AND c.username = consultantUsername) ORDER BY c.result"

//Selects the username and rating from the consultants who arent currently  hired by the user
"Select c.username, c.result FROM Consultant c WHERE c.username NOT IN (SELECT consultantUsername FROM UserHiresConsultant WHERE userUsername = '" + username + "') ORDER BY c.result"

//Selects the users who have hired the current consultant shown by mParam1
"Select u.username, p.email, uhc.contractNumber From UserPerson u, UserHiresConsultant uhc, People p Where uhc.userUsername = u.username AND p.username = u.username AND uhc.consultantUsername = '" +mParam1+ "'"

//division statement that show all hired users who have had all of the plans created by the current consultant suggested to them
"Select u.username, p.email, uhc.contractNumber From UserPerson u, UserHiresConsultant uhc, People p Where uhc.userUsername = u.username AND p.username = u.username AND uhc.consultantUsername = '" +mParam1+ "' AND NOT EXISTS ((Select planId FROM Plan WHERE createdByUsername = uhc.consultantUsername) MINUS (Select csp.planId FROM ConsultantSuggestsPlan csp Where csp.userUsername = u.username AND csp.consultantUsername = uhc.consultantUsername))"

//consultant suggests a plan to a user
"Insert Into ConsultantSuggestsPlan Values('" + userUsername + "', '" + username + "', " + plan + ", TO_TIMESTAMP('" + formattedDate + "',  'YYYY-MM-DD HH24:MI:SS'))"


//selecting the user details from a userPerson
"Select age, weight, height from UserPerson where username = '" + username + "'"

//updating the values of the user
"UPDATE userPerson SET " + column + " = " + setTo + " where username = '" + username + "'"


//Inserting a MEAL
"INSERT INTO MealCalories Values(" + carbs + ", " + fat + ", " + protein + ", " + calories + ")"
 "INSERT INTO MEAL Values(" + mealId.getText().toString() + ", '" + mealType.getText().toString() + "', '" +
                    mealDesc.getText().toString() + "', " + mealServingSize.getText().toString() + ", " + carbs + ", " + fat + ", " + protein + ")

 "INSERT INTO MealLogEntry Values(" + mealId.getText().toString() +
                     ", TO_TIMESTAMP('" + dateResult + " " + timeResult + "', 'YYYY-MM-DD HH24:MI:SS'), " + mealServingNum.getText().toString() + ")"

 "INSERT INTO UserMealLog Values('" + getIntent().getStringExtra("username") +
                         "', TO_TIMESTAMP('" + dateResult + " " + timeResult + "', 'YYYY-MM-DD HH24:MI:SS'), " + mealId.getText().toString() + ")"


 //Inserting a Workoukout ADDING A WORKOUT
 "Insert Into Workout Values (" + workoutId.getText().toString() + ", '"
                     + workoutDesc.getText().toString() + "', " + workoutCaloriesBurnt.getText().toString() + ", "
                     + workoutLength.getText().toString() + ")"
 "Insert Into Cardio Values(" + workoutId.getText().toString() + ", "
                         + cardioTexts.get(0).getText().toString() + ", " + cardioTexts.get(1).getText().toString() + ")"
 "Insert Into SPORT Values(" + workoutId.getText().toString() + ", "
                         + sportTexts.get(0).getText().toString() + ", '" + sportTexts.get(1).getText().toString() + "')"

 "Insert into ExerciseLogEntry Values(" + workoutId.getText().toString()
                         + ", TO_TIMESTAMP('" + dateResult + " " + timeResult + "', 'YYYY-MM-DD HH24:MI:SS'))"
 "Insert into UserExerciseLog Values('" + getIntent().getStringExtra("username")
                         + "', TO_TIMESTAMP('" + dateResult + " " + timeResult + "', 'YYYY-MM-DD HH24:MI:SS')," + workoutId.getText().toString()+")"



//user hires a consultant
 "Insert Into UserHiresConsultant Values('" + username + "', '" + usernameC + "', " + rand + ")"

//Select all the workout plan details created by David
"SELECT planId, createdByUsername, exercisePerWeek FROM Plan, WorkoutPlan WHERE createdByUsername = 'David' AND planId = workoutPlanId"

//Select all the diet details created by David
"SELECT planId, createdByUsername, weeklyCalories FROM Plan, Diet WHERE createdByUsername = '" + username + "' AND planId = dietId"

//get all the plans suggested to a user
"SELECT planID, consultantUsername as createdByUsername, logTime FROM ConsultantSuggestsPlan WHERE userUsername = '" + username + "' "

//get all of the meal log entries from a diet that match a search description and are ordered by one of the columns of the table
"Select m.mealId, ml.logTime, m.description, m.servingSizeGrams, m.type From DietContainsMealLog dcm, Meal m, MealLogEntry ml WHERE dcm.dietID = " + dietPlanID +" AND ml.logTime = dcm.logTime AND ml.mealId = dcm.mealId AND LOWER(m.description) LIKE '%" + searchTerm.toLowerCase() + "%' ORDER BY " + sortBy + " " + sortByOrder

//get all of the meal log entries from a diet that match a search description and are ordered by one of the columns of the table with different columns
"SELECT m.mealid, uml.logTime, m.description, mle.numberOfServings, m.type FROM userMealLog uml, Meal m, MealLogEntry mle WHERE uml.username = '" + username + "' AND mle.mealId = m.mealID AND uml.mealId = mle.mealID AND uml.logTime = mle.logTime AND LOWER(m.description) LIKE '%" + searchTerm.toLowerCase() + "%' ORDER BY " + sortBy + " " + sortByOrder