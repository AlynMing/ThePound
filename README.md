# ThePound

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
1. [Schema](#Schema)

## Overview
### Description
A new addition to the AAMU SSO banner. The Pound allows students and faculty to like, comment, and pre-register for events hosted by groups/organizations affiliated with Alabama A&M University.

### App Evaluation
- **Category:** Social Networking / Informational
- **Mobile:** This app would be primarily developed for mobile usage but could be used for desktop purposes, such as Facebook or other similar apps. Functionality wouldn't be limited to mobile devices, however mobile version could potentially have more features.
- **Story:** Allows users to stay informed more easily with events occuring on campus. This includes social events as well as educational talks.
- **Market:** This app is exclusively for individuals who attend Alabama A&M University.
- **Habit:** This app could be used as often or unoften as the user wanted depending on how deep their social life is, and what exactly they're looking for.

## Product Spec
### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* User logs in to access the AAMU Event Board.
* User has a liked events page.
* User can search specific events based on event name or tags if applicable.
* User can follow pages of organizations' events they like.
* Profile pages for each user
* Settings (Accesibility, Notification, General, etc.)

**Optional Nice-to-have Stories**

* Theme editor for the look of the individuals app

### 2. Screen Archetypes

* Login 
* Home Screen - User is give a list of upcoming events
   * Upon liking an event, the user can choose to follow thta organization.
* My Events Screen - Users can view the events they plan on attending as well as passed events
   * Upon selecting an event the user is given the details regarding that event.
* Profile Screen 
   * Allows user to view the events of organizations that they have liked
* Settings Screen
   * Lets people change language, and app notification settings.

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Home
* Search
* My Events
* Profile
* Settings

## Schema 
### Models

#### Events

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user event |
   | author        | Pointer to User| image author |
   | image         | File     | image that user posts |
   | caption       | String   | image caption by author |
   | commentsCount | Number   | number of comments that has been posted to an image |
   | likesCount    | Number   | number of likes for the post |
   | createdAt     | DateTime | date when post is created |
   | updatedAt     | DateTime | date when post is last updated |
   
#### User

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user event |
   | author        | Pointer to User| image author |
   | image         | File     | image that user posts |
   | caption       | String   | image caption by author |
   | commentsCount | Number   | number of comments that has been posted to an image |
   | likesCount    | Number   | number of likes for the post |
   | createdAt     | DateTime | date when post is created |
   | updatedAt     | DateTime | date when post is last updated | 
   
### Comments

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user event |
   | author        | Pointer to User| image author |
   | image         | File     | image that user posts |
   | caption       | String   | image caption by author |
   | commentsCount | Number   | number of comments that has been posted to an image |
   | likesCount    | Number   | number of likes for the post |
   | createdAt     | DateTime | date when post is created |
   | updatedAt     | DateTime | date when post is last updated | 

### Likes
   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user event |
   | author        | Pointer to User| image author |
   | image         | File     | image that user posts |
   | caption       | String   | image caption by author |
   | commentsCount | Number   | number of comments that has been posted to an image |
   | likesCount    | Number   | number of likes for the post |
   | createdAt     | DateTime | date when post is created |
   | updatedAt     | DateTime | date when post is last updated | 

### Networking
#### List of network requests
   - Home Screen
      - (Read/GET) Query all events posted
      - (Create/EVENT) Create a new like on an event
      - (Delete) Delete existing like
      - (Create/POST) Create a new comment on a post
      - (Delete) Delete existing comment
   - My Event List Screen
      - (Read/Event) View events liked object
   - Profile Screen
      - (Read/GET) Query logged in user object
      - (Update/PUT) Update user profile image
   - Search Bar
      - (Read/GET) Query selected events based on characters entered
