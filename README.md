# Fast food calories ranking
An Android appication build for ondiet users which eat fast food oftenly.

## Design Workflow
### Get known user location
User location provided by Google service from FusedLocationProvider API.
### Downloading Json Feedback from Google map API.
Use Async Task to download Json Data in background to avoid UI freezing.
### Foods Data
All data was collected on fast food resturant offical websites. Data update is not regularly and maybe out to date.
### Display Information
Base on API feedback, the app will show nearest meal to the user within a range.

## Algorithm
To get local data to the user, the app request location information from google map api. After recived information from google map, the app
compare with local database, and update information to the user in UI.

### TO DO
Refactoring the datebase using firebase system. Add more food information to the database. Add filter for food sorting.

