# ProgrammingAssignment2Coen390

Requirements
General
•	Two Activities: mainActivity and profileActivity.
•	profileActivity is a child Activity to mainActivity (up navigation is provided).
•	A DialogFragment to add profiles to the database
•	Opened from the mainActivity using the Floating Action Button
MainActivity
•	mainActivity has a textview at the top which displays:
•	the total number of profiles stored in the database.
•	how the database entries are displayed in the listview (by ID (increasing order) or by Name (alphabetical by Surname)) 
•	mainActivity has a Floating button that opens an “Insert Profile” DialogFragment.
•	mainActivity has a ListView that displays all the profiles stored in the database. 
•	Each line corresponds to one profile
•	Each line shows either the profile name or the profile ID
•	Each line starts with the line number of the entry in the listview
•	Every item in the list is clickable. When an item is clicked, go to profileActivity.
•	The mainActivity will have a toolbar with an action to toggle between two modes:
•	Profile name Display mode: 
	Default display mode
	Each listview entry is a line number and the profile “Surname, name”. 
	The listview entries will be ordered alphabetically using the Surname first. 
	display mode can be toggled to “By ID” using an action in the action bar. 
•	Profile ID Display mode: 
	display mode can be toggled using an action in the action bar. 
	Each listview entry is a line number and the profile ID number. 
	The listview entries will be ordered in increasing order using the ID number. 
	display mode can be toggled to “By Name” using an action in the action bar. 
InsertProfile DialogFragment
•	Insert Profile Dialog Fragment has:
•	2 EditTexts for profile Name and Surname (2 strings, no special characters).
•	1 EditText for profile ID# (8-digit integer, 10000000 to 99999999, no duplicate ID# allowed!)
•	1 EditText for profile GPA (1 float number, 0 to 4.3)
•	2 buttons for save profile and cancel. 
•	Save button: 
	saves the input of the edit texts as a new profile in the database
	returns to main activity and reloads the listview.
	Does not save if some fields are empty or information entered is invalid.
	A toast notifies the user about missing/invalid entries.
•	Cancel button: closes the dialog and returns to main activity.
•	See Dialog Fragment example from Tutorial 3-4 for an example
ProfileActivity
•	profileActivity has:
•	TextView(s) that display all the information of the profile: (Must show the profile corresponding to the entry clicked from the listview in the previous activity).
	Name
	Surname
	ID#
	GPA
•	Textview header to the Access History listview.
•	ListView displaying the timestamps, formatted as yyyy-mm-dd @ hh:mm:ss, indicating when the profile was created, opened, closed & deleted. 
•	Delete button which deletes the profile that is currently opened in the profileActivity then go back to the mainActivity and reloads the listview of the profile. 
•	A toolbar with an Up navigation to the mainActivity
•	When the profile is created, a “created” entry is added to the access table.
•	Each time a profile is opened from the mainActivity, a new access “open” entry is added to the access table.
•	Each time a profile is closed, a new access “close” entry is added to the access table. 
•	Deleting a profile does not delete the access entries in the database, it adds a “profile deleted” entry with a timestamp. 
DatabaseHelper
•	DatabaseHelper provides:
•	Creation of an SQLite database with two tables
•	Helper functions to
	Add entries in both tables
	Read entries in both tables
	Delete entries in only the Profile table
•	All add/read functions should return Access or Profile objects
•	You must create the Access and Profile Classes to use their objects with the helper. 
•	Use Setters/Getters and an appropriate constructor function
•	See Course.java class file from Tutorial 3-4 for an example
