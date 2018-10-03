# DejaPhoto App
This is an app that changes the wallpaper photos on a users phone based on the date, time, and location of images. Images with a similarity to a user's current location, time, or are given preference. The user can also release photos to prevent them from coming back into the cycle, or add karma which makes them more likely to show up. This is extended by allowing users to add friends with whom they want to share their photos. If a user so enables, their pictures can be included in the ranking for wallpaper display on their freind's phones too.

## Installations
- Android Studio: full suite to develop android applications
- android phone: cheap and realistic testing.

## Project Motivation
This is the project for a Software Development class. There was a strong focus on the process of agile software development. It was definitely a challenge getting everyone to work together efficiently in such a short period of time toward a goal that is quite intricate. 

## File Descriptions
Downloading Android Studio and loading this project will sort the files effectively. Many files in this repository are from android studio and not actually things we developed ourselves.

Files can be found in: `app/src/main/java/com/example/android/`
- ActionReceiver.java: this class is used to track the 15 seconds a currUser has to undo karma or release. if they don't undo then it sends an intent service. implemented based on a similar class structure as the DJWidgetProvider.java
- AddFriendsActivity.java: This class will add the friends into the firebase for each app user and take care of any friends requests, so that user can accept or decline the request
- AlbumActivity.java: in development
- AutoWallpaperChangeTask.java: this class will handle auto wallpaper change with default timer
- BuildDisplayCycle.java: creates the order in which images are shown
- ChangeImage.java: This intent service changes the image when called according to the passed parameter it also moves the head of the display cycle to hold the number/position of the picture that is displayed. It calls the wallpaper service manager to actually change the wallpaper.
- DatabaseSync.java: his class will sync every local change after a set duration
- DejaPhotoWidgetProvider.java: uses android APIs to create a widget that is responsive to presses on buttons
- DisplayActivity.java: show image
- FileManager.java: handles of information including images and information related to the images.
- Friends.java: controls the ability to send and add friends
- Global.java: Used to keep track of global information in the app
- MainActivity.java: general controller of the app home interface
- Photo.java: class file; Each photo object has relevant information needed to rank it according to specifications
- PhotoLocation.java: gets longitude and latitude values from the EXIF data in each photo. The long/lat values are then converted to a city value or street address with geocode
- PhotoPicker.java: add photos from camera roll or photos on phone to the new DejaPhoto album.
- PhotoStorage.java: uploads and downloads images from database
- Rank.java: algorithm controlling the order in which photos appear, based on things like time, day of week, location, karma etc.
- Request.java: 
- Rerank.java: intent service creates a new rank object which has the correct order of the pictures that are supposed to be displayed. this is then sent to the build display cycle.
- SQLiteHelper.java: gets information from SQL style database on phone
- SetActivity.java: controls main page
- SetLocationActivity.java: controls editing of the location associated with a photo
- ShareActivity.java: controls sharing page
- TrackerService.java: helps app keep track of time and location
- User.java: class file
- WallpaperChanger.java: controls timing and settings of changing the wallpaper

## Process: How to Interact
After downloading the app and signing up, you can add friends via their Gmail addresses. This enables sharing between people. You can also select to add photos from your own library to the DejaPhoto album, and once DejaPhoto is enabled, your wallpaper will change on regular intervals (default is 15 seconds). You can enable the sharing of your picutres, see only your pictures, or see only the photos of your friends on your wallpaper. This can extend to a larger group of people all sharing their images.

## Licensing, Authors, Acknowledgements, etc.
Contributors: Mateusz Kucz, Justin Lee, Lei Wan, Michael Chen, Amanda Chan
