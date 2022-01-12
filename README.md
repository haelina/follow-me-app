# FollowMe-app

A Spring Boot project made for mooc course "Web-palvelinohjelmointi Java 2021" (Open University studies in University of Helsinki).

The application is a Spring Boot app with Thymeleaf templates and it uses Postgres database for storing user-related data and images.

The purpose of the app is to allow user registration and login and then the user can add images and write posts to his/her own pages. A user can also comment and like other users' images and posts, or start to follow them.

## Link to Heroku

The application is running in Heroku. [Go and see it yourself!](https://follow-me2.herokuapp.com/)

Test user details for logging in:
username: miraakkeli
password: testi123

## What it looks like?

<p align="middle">
<img src="/images/fol1.jpg" width="100%" height="auto">
</p>

---

<p align="middle">
<img src="/images/fol2.jpg" width="100%" height="auto">
</p>

---

<p align="middle">
<img src="/images/fol3.jpg" width="100%" height="auto">
</p>

## Features implemented in the app

- User is able to register/create new account
  - This creates new user to postgres database
  - A new userpage is also created for the user (path: /users/newUserNameHere)
- It is possible to search for users by username or parts of username
  - A search field is in front page
- User is able to follow other users
  - Start following -buttons can be seen in front page user listing
- User is able to see who they are following and who are following the user
  - "My followers" and "I follow" -lists are in user's own page
- User has image gallery
  - User can add maximum of 10 images to gallery
  - User can delete images
  - User can set one image as profile image
- User's own page
  - Shows user's name and follower details
  - User can write new posts to the wall
- Like-buttons
  - User is able to like posts and images while logged in
  - The posts and images show how many likes it has gathered
- Commenting
  - User is able to add comments to posts and images

## Known bugs

- Start following -button in front page
  - The following functionality works
  - The button should be at least disabled if a user already follows this user
  - Start following -button should also be visible in more places than the front page
- Profile image
  - Profile image can not be deleted
  - Profile image disappears only if the current profile image is deleted from the gallery
- Visual flaws
  - All the buttons and elements are currently not styled with bootstrap classes
  - The page is designed for desktop and it doesn't work well in mobile view
