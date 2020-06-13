Select up.username, us.sleepTime from userPerson up, UserSleepEntry us WHERE up.username = us.username

"select trunc(us.sleepdate, 'IW'), AVG(us.sleepTime), SUM(us.sleepTime) from UserSleepEntry us where us.username = " + username + " AND us.sleepdate >= TRUNC(SYSDATE,'mm') GROUP BY trunc(us.sleepdate, 'IW')"


"Select us.username, us.sleepTime from UserSleepEntry us WHERE us.username = " + username + " AND sleepDate >= TRUNC(SYSDATE, 'DY') AND us.sleepDate < TRUNC(SYSDATE, 'DY') + 7 ORDER BY us.sleepdate"

map.put(
                    "extra",
                    "INSERT INTO userSleepEntry VALUES ('"
                            + username
                            + "', TO_DATE('"
                            + dateResult.getText()
                            +"', 'YYYY-MM-DD'), "
                            + s1.getSelectedItem().toString() + ", " + s2.getSelectedItem().toString() +")");


'SELECT * FROM People p, UserPerson up WHERE up.username = \'' . $username . '\' AND p.username = up.username'
'SELECT * FROM People p, Consultant c WHERE c.username = \'' . $username . '\' AND c.username = p.username'


"SELECT ele.logtime, w.description, w.caloriesburnt, w.timeworkout FROM workout w, exerciselogentry ele, userexerciselog uel WHERE w.workoutid = ele.workoutid AND w.workoutid = uel.workoutid AND ele.logtime = uel.logtime AND uel.username = '"+ username + "' ORDER BY uel.logtime DESC"

"SELECT uml.logTime, m.description, mle.numberOfServings, m.type FROM userMealLog uml, Meal m, MealLogEntry mle WHERE uml.username = 'bob123' AND mle.mealId = m.mealID AND uml.mealId = mle.mealID AND uml.logTime = mle.logTime ORDER BY mle.logTime DESC"