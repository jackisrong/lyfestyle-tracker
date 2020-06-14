# LyfeStyle Fitness Tracker
*A CPSC 304 Introduction to Relational Databases Project*

Ever wanted to track your diet? Maybe even your workouts? Well fear no longer because LyfeStyle Fitness Tracker is here!

Sure, there might be 100 other apps out there that do the same thing, but we made this one. From scratch. In about a week. I cried. Luis cried. We all cried.

Here are some features that we've currently implemented:
* Web JSON API
  * Web API written in PHP to pass on Oracle database access to app (not currently secured or authenticated) for proper 3-tier architecture
  * API access from Android app using POST HTTP connection and parsing received JSON data
* Login
  * Functional with username/password verification with database for both users and consultants
* Meal and exercise tracking
  * Meal logs for the user are populated from database and displayed in a nice table that is searchable by description terms and sortable ascending and descending by any column
* Sleep tracking
  * Graphs created (using external library) using user's sleep data from database
  * Ability to create new sleep logs
* User consultant hiring

Here are some features that we'll be implementing (hopefully):
* Account creation 
* Dashboard graphs and data
* Add new meal/exercise logs and diet/workout plans
* Consultant hiring
* Consultant version of dashboard with plans and users
* A beautiful UI and delightful UX

Future ideas after the end of this course if we still have the motivation to maintain this:
* Water intake tracker
* API tokens

Thank you for reading. Send food.
