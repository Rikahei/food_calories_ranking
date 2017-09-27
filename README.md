# Fast food calories ranking
An Android appication build for ondiet users which eat fast food oftenly.

## Design Workflow
### Get known user location
User location provided by Google service from FusedLocationProvider API.
### Downloading Json Feedback from Google map API.
Use Async Task to download Json Data in background to avoid UI freezing. In this APP, only the resturant name is needed, I use webapi insert of android map api to simplify the coding.
### Foods Data
All data was collected on fast food resturant offical websites. Data update is not regularly and maybe out to date.
### Display Information
Base on API feedback, the app will show nearest meal to the user within a range.


