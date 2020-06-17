# LyfeStyle Fitness Tracker
*A CPSC 304 Introduction to Relational Databases Project*

Ever wanted to track your diet? Maybe even your workouts? Well fear no longer because LyfeStyle Fitness Tracker is here!

Sure, there might be 100 other apps out there that do the same thing, but we made this one. From scratch. In about a week. I cried. Luis cried. We all cried.

Here are some features that we've currently implemented:
* Web JSON API
  * Web API written in PHP to pass on Oracle database access to app (currently not secured) for proper 3-tier architecture
  * API access from Android app using POST HTTP connection and parsing of received JSON data
* Login
  * Username and password verification using database for both users and consultants, includes message if credentials are incorrect
  * App remembers your username and password to automatically log you in on app restart (currently no encryption is being used because we hate security)
* Account creation
  * Ability to create accounts for both users and consultants with specific registration requirements for each type of account
  * Accounts require unique usernames just like every other account thing out there (wow look at us go with these features)
* Meal and exercise tracking
  * Meal and exercise logs for the user are populated from the database and displayed in a nice table that is searchable by description terms and sortable ascending or descending by any column so you can see which meals you got the most calories from and yell at yourself internally (searching and sorting are both done with SQL queries since this is a databases course even though it isn't efficient)
  * Ability to add new meal or exercise log entries for that mediocre pasta you made yourself for dinner or that sweaty basketball game you had earlier today
  * Ability to edit an existing meal or exercise log entry in case you spelt "basketball" as "took a nap" by accident
  * Ability to delete an existing meal or exercise log entry in case you're showing your friends your cool new log on this cool new app and don't want to expose that you ate McDonald's for all 3 meals yesterday
  * Ability to duplicate an existing meal or exercise log entry in case you ate the same meal at Chipotle for 24 days in a row
* Diets and workout plans
  * Ability to create diets and workout plans to reach your fitness goals
  * Ability to add new meals/exercises to the plans to plan ahead for your week of being a skinny queen
* Sleep tracking
  * Graphs created (with external library) using user's sleep data from the database so you can visualize your weekly patterns of exactly when you decided you've played enough League of Legends and should probably sleep
  * Ability to create new sleep logs so you can look back at your sleep patterns when you wonder why you're tired all the time
* User consultant management
  * Beautiful table laying out the consultants you've hired so you can finally get that summer bod you've been telling yourself you'll get even though it's already July
  * Ability to hire a consultant for that summer bod goal with just 4-ish clicks!
* Consultant version of app
  * Dashboard specifically for consultants with a welcome message and ability to delete their account
  * Ability to associate themselves with a company or add a new company
  * Ability to see all their clients that they'll be helping get their summer bod
  * Ability to see their biggest clients who are using all of their plans (e z division SQL query)
  * Ability to see the diets and workout plans that they've created

Here are some features that we'll be implementing very very soon:
* Dashboard graphs and summaries
* Cleaning up code and names, minimising coupling, maximizing cohesion, adding comments for readability

Future ideas after after this course if we still have the motivation to maintain this (aka probably not):
* Water intake tracker so you remember to stay hydrated and live that healthy life
* Use threads so it doesn't skip 200 frames and take 5 seconds to bring up the Food dashboard
* API tokens so our API isn't _that_ insecure (don't be insecure haha... you're beautiful!)

Thank you for reading. Send food.
