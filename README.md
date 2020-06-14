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
* Meal and exercise tracking
  * Meal and exercise logs for the user are populated from the database and displayed in a nice table that is searchable by description terms and sortable ascending or descending by any column so you can see which meals you got the most calories from and yell at yourself internally (searching and sorting are both done with SQL queries since this is a databases course even though it isn't efficient)
  * Ability to add new meal or exercise log entry for that mediocre pasta you made yourself for dinner or that sweaty basketball game you had earlier today
  * Ability to edit an existing meal or exercise log entry in case you spelt "basketball" as "took a nap" by accident
* Sleep tracking
  * Graphs created (with external library) using user's sleep data from the database so you can visualize your weekly patterns of exactly when you decided you've played enough League of Legends and should probably sleep
  * Ability to create new sleep logs so you can look back at your sleep patterns when you wonder why you're tired all the time
* User consultant management
  * Beautiful table laying out the consultants you've hired so you can finally get that summer bod you've been telling yourself you'll get even though it's already July
  * Ability to hire a consultant for that summer bod goal with just 4-ish clicks!

Here are some features that we'll be implementing very very soon:
* Account creation
* Dashboard graphs, data, and summary
* Add new diet/workout plans
* Edit/delete existing meal/exercise logs and diet/workout plans
* Consultant version of app with list of users and plans
* Cleaning up code and names, minimising coupling and cohesion, adding comments for readability

Future ideas after the end of this course if we still have the motivation to maintain this:
* Water intake tracker so you remember to stay hydrated and live that healthy life
* API tokens so our API isn't _that_ insecure (don't be insecure haha... you're beautiful!)

Thank you for reading. Send food.
