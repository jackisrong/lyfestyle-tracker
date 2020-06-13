drop table ConsultantSuggestsPlan;
drop table UserHiresConsultant;
drop table ConsultantWorksForCompany;
drop table Company;
drop table Consultant;
drop table UserMealLog;
drop table UserSleepEntry;
drop table UserExerciseLog;
drop table DietContainsMealLog;
drop table MealLogEntry;
drop table ExerciseLogEntry;
drop table Cardio;
drop table Sport;
drop table WorkoutPlanContainsWorkout;
drop table WorkoutPlan;
drop table Diet;
drop table Workout;
drop table Meal;
drop table UserPerson;
drop table Plan;
drop table People;
drop table MealCalories;





CREATE TABLE MealCalories (
	carbohydrates INTEGER,
	fat INTEGER,
	protein INTEGER,
	caloriesPerServing INTEGER,
	PRIMARY KEY (carbohydrates, fat, protein)
);

grant select on MealCalories to public;


CREATE TABLE People (
	username VARCHAR(20) PRIMARY KEY,
	name VARCHAR(20),
	password VARCHAR(50),
	email VARCHAR(50)
);

grant select on People to public;

CREATE TABLE Plan (
	planId INTEGER PRIMARY KEY,
	createdByUsername VARCHAR(20) NOT NULL,
	FOREIGN KEY (createdByUsername) REFERENCES People(username)
		ON DELETE CASCADE
);


grant select on Plan to public;

CREATE TABLE UserPerson (
	username VARCHAR(20),
	passiveAmountOfCalories INTEGER,
	weight INTEGER,
	age INTEGER,
	height INTEGER,
	PRIMARY KEY (username),
	FOREIGN KEY (username) REFERENCES People(username)
);

grant select on UserPerson to public;

CREATE TABLE Meal (
	mealId INTEGER PRIMARY KEY,
	type VARCHAR(20),
	description VARCHAR(200),
	servingSizeGrams INTEGER,
	carbohydrates INTEGER,
	fat INTEGER,
	protein INTEGER,
	FOREIGN KEY (carbohydrates,fat, protein) REFERENCES MealCalories(carbohydrates,fat, protein)
);

grant select on Meal to public;


CREATE TABLE Workout (
	workoutId INTEGER PRIMARY KEY,
	description VARCHAR(200),
	caloriesBurnt REAL,
	timeworkout INTEGER
);

grant select on Workout to public;

CREATE TABLE Diet (
	dietId INTEGER PRIMARY KEY,
	weeklyCalories INTEGER,
	FOREIGN KEY (dietId) REFERENCES Plan(planId)
		ON DELETE CASCADE
);

grant select on Diet to public;


CREATE TABLE WorkoutPlan (
	workoutPlanId INTEGER PRIMARY KEY,
	exercisePerWeek INTEGER,
	FOREIGN KEY (workoutPlanId) REFERENCES Plan(planId)
		ON DELETE CASCADE
);

grant select on WorkoutPlan to public;


CREATE TABLE WorkoutPlanContainsWorkout (
	workoutPlanId INTEGER,
	workoutId INTEGER,
	PRIMARY KEY(workoutPlanId, workoutId),
	FOREIGN KEY (workoutPlanId) REFERENCES WorkoutPlan
		ON DELETE CASCADE,
	FOREIGN KEY (workoutId) REFERENCES Workout(workoutId)
		ON DELETE CASCADE
);

grant select on WorkoutPlanContainsWorkout to public;


CREATE TABLE Sport (
	workoutId INTEGER PRIMARY KEY,
	intensity INTEGER,
	sportType VARCHAR(20),
	FOREIGN KEY (workoutId) REFERENCES Workout(workoutId)
		ON DELETE CASCADE
);

grant select on Sport to public;

CREATE TABLE Cardio (
	workoutId INTEGER PRIMARY KEY,
	distance REAL,
	avgSpeed REAL,
	FOREIGN KEY (workoutId) REFERENCES Workout(workoutId)
		ON DELETE CASCADE
);

CREATE TABLE ExerciseLogEntry(
	workoutId INTEGER,
	logTime TIMESTAMP,
	PRIMARY KEY (workoutId, logTime),
	FOREIGN KEY (workoutId) REFERENCES Workout(workoutId)
		ON DELETE CASCADE
);

grant select on ExerciseLogEntry to public;

CREATE TABLE MealLogEntry (
	mealID INTEGER,
	logTime TIMESTAMP,
	numberOfServings REAL,
	PRIMARY KEY (mealID, logTime),
	FOREIGN KEY (mealID) REFERENCES Meal(mealID)
		ON DELETE CASCADE
);

grant select on MealLogEntry to public;


CREATE TABLE DietContainsMealLog (
	dietId INTEGER,
	logTime TIMESTAMP,
	mealId INTEGER,
	PRIMARY KEY (dietId, logTime, mealId),
	FOREIGN KEY (dietId) REFERENCES Diet(dietId)
		ON DELETE CASCADE,
	FOREIGN KEY (mealId, logTime) REFERENCES MealLogEntry(mealId, logTime)
		ON DELETE CASCADE
);

grant select on DietContainsMealLog to public;

CREATE TABLE UserExerciseLog (
	username VARCHAR(20),
	logTime TIMESTAMP,
	workoutId INTEGER,
	PRIMARY KEY (username, logTime, workoutId),
	FOREIGN KEY (username) REFERENCES UserPerson
		ON DELETE CASCADE,
	FOREIGN KEY (workoutId, logTime) REFERENCES ExerciseLogEntry(workoutId, logTime)
		ON DELETE CASCADE
);

grant select on UserExerciseLog to public;


CREATE TABLE UserSleepEntry (
	username VARCHAR(20),
	sleepDate DATE,
	howRestful INTEGER,
	sleepTime INTEGER,
	PRIMARY KEY (username, sleepDate),
	FOREIGN KEY (username) REFERENCES UserPerson(username)
		ON DELETE CASCADE
);

grant select on UserSleepEntry to public;

CREATE TABLE UserMealLog (
	username VARCHAR(20),
	logTime TIMESTAMP,
	mealId INTEGER,
	PRIMARY KEY (username, logTime, mealId),
	FOREIGN KEY (username) REFERENCES UserPerson(username)
		ON DELETE CASCADE,
	FOREIGN KEY (mealId, logTime) REFERENCES MealLogEntry(mealId, logTime)
		ON DELETE CASCADE
);

grant select on UserMealLog to public;

CREATE TABLE Consultant (
	username VARCHAR(20) PRIMARY KEY,
	rating INTEGER,
	FOREIGN KEY (username) REFERENCES People(username)
		ON DELETE CASCADE
);

grant select on Consultant to public;

CREATE TABLE Company (
	companyID INTEGER PRIMARY KEY,
	name VARCHAR(200),
	location VARCHAR(200)
);

grant select on Company to public;

CREATE TABLE ConsultantWorksForCompany (
	consultantUsername VARCHAR(20),
	companyId INTEGER,
	PRIMARY KEY (consultantUsername, companyId),
	FOREIGN KEY (consultantUsername) REFERENCES Consultant(username)
		ON DELETE CASCADE,
	FOREIGN KEY (companyId) REFERENCES Company(companyId)
		ON DELETE CASCADE
);

grant select on ConsultantWorksForCompany to public;

CREATE TABLE UserHiresConsultant (
	userUsername VARCHAR(20),
	consultantUsername VARCHAR(20),
	contractNumber INTEGER,
	PRIMARY KEY (userUsername, consultantUsername),
	FOREIGN KEY (userUsername) REFERENCES UserPerson(username)
		ON DELETE CASCADE,
	FOREIGN KEY (consultantUsername) REFERENCES Consultant(username)
		ON DELETE CASCADE
);

grant select on UserHiresConsultant to public;

CREATE TABLE ConsultantSuggestsPlan (
	userUsername VARCHAR(20),
	consultantUsername VARCHAR(20),
	planId INTEGER,
	logTime TIMESTAMP,
	PRIMARY KEY (userUsername, consultantUsername, planId),
	FOREIGN KEY (userUsername, consultantUsername) REFERENCES UserHiresConsultant(userUsername, consultantUsername)
		ON DELETE CASCADE,
	FOREIGN KEY (planId) REFERENCES Plan(planID)
		ON DELETE CASCADE
);

grant select on ConsultantSuggestsPlan to public;


insert into People values ('bob123', 'bob' ,'12345;)','bob123@gmail.com');
insert into People values ('IluvHs','Susan','1D<3,', 'email@hotmail.com');
insert into People values ('Erin','Erin','pass<*<$', 'erin@gmail.com');
insert into People values ('David','David','catsTheMusical','dobrik@gmail.com');


insert into UserPerson values ('bob123', 600, 140, 35, 180);
insert into UserPerson values ('IluvHs', 800, 90, 14, 100);
insert into Consultant values ('Erin',2);
insert into Consultant values ('David',5);


insert into Company values (0, 'BELL','54 Main st, Vancouver BC');
insert into Company values (1, 'INTEL','53 Main st, Vancouver BC');


insert into ConsultantWorksForCompany values ('Erin', 0);

insert into ConsultantWorksForCompany values ('David', 1);

insert into UserHiresConsultant values ('bob123', 'David', 145);

insert into UserHiresConsultant values ('bob123', 'Erin', 1457);

insert into UserHiresConsultant values ('IluvHs', 'Erin', 2102);


insert into UserHiresConsultant values ('IluvHs', 'David', 2101);




insert into Workout values (0, 'Tennis, singles', 626, 70);
insert into Workout values (1, 'Basketball, intense', 715, 80);
insert into Workout values (5, 'walk, medium', 353, 60);
insert into Workout values (6, 'run', 380, 35);

insert into Sport values (0, 4, 'Tennis');
insert into Sport values (1, 7, 'Basketball');

insert into Cardio values (5, 8, 5.4);
insert into Cardio values (6, 5, 9);

insert into Plan values (0, 'David');
insert into Plan values (1, 'David');

insert into WorkoutPlan values (0, 2100);
insert into WorkoutPlan values (1, 1000);

insert into WorkoutPlanContainsWorkout values (0, 0);
insert into WorkoutPlanContainsWorkout values (1, 1);

insert into MealCalories values (79, 47, 26, 778);
insert into MealCalories values (38, 28, 12, 372);

insert into Meal values (0, 'Breakfast', 'Scrambled eggs, banana yogurt', 500, 79, 47, 26);
insert into Meal values (1, 'Lunch', 'salmon sandwich', 300, 38, 28, 12);

insert into Plan values (5, 'Erin');
insert into Plan values (9, 'Erin');

insert into Diet values (5, 9000);
insert into Diet values (9, 20000);

insert into MealLogEntry values (0,TO_TIMESTAMP('2020-05-20 09:42:00',  'YYYY-MM-DD HH24:MI:SS'), 1);
insert into MealLogEntry values (1,TO_TIMESTAMP('2020-07-02 20:00:00',  'YYYY-MM-DD HH24:MI:SS'), 1);

insert into UserMealLog values ('IluvHs',TO_TIMESTAMP('2020-05-20 09:42:00',  'YYYY-MM-DD HH24:MI:SS'), 0);
insert into UserMealLog values ('bob123',TO_TIMESTAMP('2020-07-02 20:00:00',  'YYYY-MM-DD HH24:MI:SS'), 1);

insert into DietContainsMealLog values (5,TO_TIMESTAMP('2020-05-20 09:42:00',  'YYYY-MM-DD HH24:MI:SS'), 0);
insert into DietContainsMealLog values (9,TO_TIMESTAMP('2020-07-02 20:00:00',  'YYYY-MM-DD HH24:MI:SS'), 1);


insert into UserSleepEntry values ('bob123',TO_DATE('2020-05-28', 'YYYY-MM-DD'),1,5);
insert into UserSleepEntry values ('IluvHs',TO_DATE('2020-05-28', 'YYYY-MM-DD'),5,7);


insert into ExerciseLogEntry values (0, TO_TIMESTAMP('2020-07-02 20:00:00',  'YYYY-MM-DD HH24:MI:SS'));
insert into ExerciseLogEntry values (1, TO_TIMESTAMP('2020-09-22 23:15:00',  'YYYY-MM-DD HH24:MI:SS'));

insert into UserExerciseLog values ('IluvHs', TO_TIMESTAMP('2020-07-02 20:00:00',  'YYYY-MM-DD HH24:MI:SS'), 0);
insert into UserExerciseLog values ('bob123',TO_TIMESTAMP('2020-09-22 23:15:00',  'YYYY-MM-DD HH24:MI:SS'),1);

insert into ConsultantSuggestsPlan values ('bob123', 'David', 0,TO_TIMESTAMP('2020-05-20 09:42:00',  'YYYY-MM-DD HH24:MI:SS'));
insert into ConsultantSuggestsPlan values ('IluvHs', 'David', 9,TO_TIMESTAMP('2020-07-02 20:00:00',  'YYYY-MM-DD HH24:MI:SS'));
insert into ConsultantSuggestsPlan values ('bob123', 'Erin', 1,TO_TIMESTAMP('2020-04-20 09:42:00',  'YYYY-MM-DD HH24:MI:SS'));
insert into ConsultantSuggestsPlan values ('IluvHs', 'Erin', 5,TO_TIMESTAMP('2020-01-02 20:00:00',  'YYYY-MM-DD HH24:MI:SS'));
