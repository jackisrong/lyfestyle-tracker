//selects the user sleep entries
"Select up.username, us.sleepTime from userPerson up, UserSleepEntry us WHERE up.username = us.username"

//selects the truncated sleep weeks, avg for those weeks and sum for those weeks for the entire month, grouped by their weeks
"select trunc(us.sleepdate, 'IW'), AVG(us.sleepTime), SUM(us.sleepTime) from UserSleepEntry us where us.username = " + username + " AND us.sleepdate >= TRUNC(SYSDATE,'mm') GROUP BY trunc(us.sleepdate, 'IW')"

//selects the user sleep times for the past week
"Select us.username, us.sleepTime from UserSleepEntry us WHERE us.username = " + username + " AND sleepDate >= TRUNC(SYSDATE, 'DY') AND us.sleepDate < TRUNC(SYSDATE, 'DY') + 7 ORDER BY us.sleepdate"

"INSERT INTO userSleepEntry VALUES ('"+ username + "', TO_DATE('" + dateResult.getText() +"', 'YYYY-MM-DD'), " + s1.getSelectedItem().toString() + ", " + s2.getSelectedItem().toString() +")");


'SELECT * FROM People p, UserPerson up WHERE up.username = '' + username + '' AND p.username = up.username'
'SELECT * FROM People p, Consultant c WHERE c.username = ' + username  + ' AND c.username = p.username'


"SELECT ele.logtime, w.description, w.caloriesburnt, w.timeworkout FROM workout w, exerciselogentry ele, userexerciselog uel WHERE w.workoutid = ele.workoutid AND w.workoutid = uel.workoutid AND ele.logtime = uel.logtime AND uel.username = '"+ username + "' ORDER BY uel.logtime DESC"

"SELECT uml.logTime, m.description, mle.numberOfServings, m.type FROM userMealLog uml, Meal m, MealLogEntry mle WHERE uml.username = 'bob123' AND mle.mealId = m.mealID AND uml.mealId = mle.mealID AND uml.logTime = mle.logTime ORDER BY mle.logTime DESC"

"Select c.username, c.rating, co.name FROM Consultant c, Company co, ConsultantWorksForCompany cw WHERE cw.consultantUsername = c.username AND co.companyID = cw.companyId AND c.username NOT IN (SELECT consultantUsername FROM UserHiresConsultant WHERE userUsername = '" + mParam1 +"') ORDER BY c.rating"
"Select c.username, c.result, co.name FROM Consultant c, Company co, ConsultantWorksForCompany cw WHERE cw.consultantUsername = c.username AND co.companyID = cw.companyId AND c.username IN (SELECT consultantUsername FROM UserHiresConsultant WHERE userUsername = '" + mParam1 +"') ORDER BY c.result"


"Select c.username, c.result, co.name FROM Consultant c, Company co, ConsultantWorksForCompany cw WHERE cw.consultantUsername = c.username AND co.companyID = cw.companyId AND c.username NOT IN (SELECT consultantUsername FROM UserHiresConsultant WHERE userUsername = 'bob123') ORDER BY c.result"


"Select u.username, p.email, uhc.contractNumber From UserPerson u, UserHiresConsultant uhc, People p Where uhc.userUsername = u.username AND p.username = u.username AND uhc.consultantUsername = '" +mParam1+ "'"

"Select u.username, p.email, uhc.contractNumber From UserPerson u, UserHiresConsultant uhc, People p Where uhc.userUsername = u.username AND p.username = u.username AND uhc.consultantUsername = '" +mParam1+ "' AND NOT EXISTS ((Select planId FROM Plan WHERE createdByUsername = uhc.consultantUsername) MINUS (Select csp.planId FROM ConsultantSuggestsPlan csp Where csp.userUsername = u.username AND csp.consultantUsername = uhc.consultantUsername))"

"Insert Into ConsultantSuggestsPlan Values('" + userUsername + "', '" + username + "', " + plan + ", TO_TIMESTAMP('" + formattedDate + "',  'YYYY-MM-DD HH24:MI:SS'))"



"Select age, weight, height from UserPerson where username = '" + username + "'"

"UPDATE userPerson SET " + column + " = " + setTo + " where username = '" + username + "'"


//Inserting a MEAL
"INSERT INTO MealCalories Values(" + carbs + ", " + fat + ", " + protein + ", " + calories + ")"
 "INSERT INTO MEAL Values(" + mealId.getText().toString() + ", '" + mealType.getText().toString() + "', '" +
                    mealDesc.getText().toString() + "', " + mealServingSize.getText().toString() + ", " + carbs + ", " + fat + ", " + protein + ")

 "INSERT INTO MealLogEntry Values(" + mealId.getText().toString() +
                     ", TO_TIMESTAMP('" + dateResult + " " + timeResult + "', 'YYYY-MM-DD HH24:MI:SS'), " + mealServingNum.getText().toString() + ")"

 "INSERT INTO UserMealLog Values('" + getIntent().getStringExtra("username") +
                         "', TO_TIMESTAMP('" + dateResult + " " + timeResult + "', 'YYYY-MM-DD HH24:MI:SS'), " + mealId.getText().toString() + ")"


 //Inserting a Workou//ADDING A WORKOUT
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




 "Insert Into UserHiresConsultant Values('" + username + "', '" + usernameC +"', TO_TIMESTAMP('" + timeStamp +"', 'YYYY-MM-DD HH24:MI:SS'))"



 "SELECT planId, createdByUsername, exercisePerWeek FROM Plan, WorkoutPlan WHERE createdByUsername = 'David' AND planId = workoutPlanId"

"SELECT planId, createdByUsername, weeklyCalories FROM Plan, Diet WHERE createdByUsername = '" + username + "' AND planId = dietId"

"SELECT planID, consultantUsername as createdByUsername, logTime FROM ConsultantSuggestsPlan WHERE userUsername = '" + username + "' "


"Select m.mealId, ml.logTime, m.description, m.servingSizeGrams, m.type From DietContainsMealLog dcm, Meal m, MealLogEntry ml WHERE dcm.dietID = " + dietPlanID +" AND ml.logTime = dcm.logTime AND ml.mealId = dcm.mealId AND LOWER(m.description) LIKE '%" + searchTerm.toLowerCase() + "%' ORDER BY " + sortBy + " " + sortByOrder

"SELECT m.mealid, uml.logTime, m.description, mle.numberOfServings, m.type FROM userMealLog uml, Meal m, MealLogEntry mle WHERE uml.username = '" + username + "' AND mle.mealId = m.mealID AND uml.mealId = mle.mealID AND uml.logTime = mle.logTime AND LOWER(m.description) LIKE '%" + searchTerm.toLowerCase() + "%' ORDER BY " + sortBy + " " + sortByOrder