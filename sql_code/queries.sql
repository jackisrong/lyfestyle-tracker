Select up.username, us.sleepTime from userPerson up, UserSleepEntry us WHERE up.username = us.username

"select trunc(us.sleepdate, 'IW'), AVG(us.sleepTime), SUM(us.sleepTime) from UserSleepEntry us where us.username = " + username + " AND us.sleepdate >= TRUNC(SYSDATE,'mm') GROUP BY trunc(us.sleepdate, 'IW')"


"Select us.username, us.sleepTime from UserSleepEntry us WHERE us.username = " + username + " AND sleepDate >= TRUNC(SYSDATE, 'DY') AND us.sleepDate < TRUNC(SYSDATE, 'DY') + 7 ORDER BY us.sleepdate"