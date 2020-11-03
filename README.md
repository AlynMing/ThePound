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

- [x] Create an API database to store user info and posts.
- [x] User logs in to access the AAMU Event Board.
- [ ] User can scroll through the listed events.
- [ ] User has a liked events page.
- [ ] Profile pages for each user
- [ ] Settings (Accesibility, Notification, General, etc.)

**Optional Nice-to-have Stories**

- [ ] Theme editor for the look of the individuals app
- [ ] User can follow pages of organizations' events they like.
- [ ] User can search specific events based on event name or tags if applicable.

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
<img src="https://github.com/NylanW1/ThePound/blob/main/IMG_1979.jpg" width=800><br>

**Tab Navigation** (Tab to Screen)

* Home
* Search
* My Events
* Profile
* Settings

## Wireframes

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
   | username       | String| user's diplayed name |
   | image         | File     | user's profile image |
   | createdAt     | DateTime | date when post is created |
   | updatedAt     | DateTime | date when post is last updated | 
   
### Comments

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user event |
   | author        | Pointer to User| comment author |
   | event         | Pointer to Event     | event comment was posted on |
   | createdAt     | DateTime | date when post is created |
   | updatedAt     | DateTime | date when post is last updated | 

### Likes
   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user event |
   | author        | Pointer to User| comment author |
   | event         | Pointer to Event     | event that was liked |
   | caption       | String   | image caption by author |
   | createdAt     | DateTime | date when post is created |
   | updatedAt     | DateTime | date when post is last updated | 

### Networking
#### List of network requests
   - Home Screen
      - (Read/GET) Query all events posted
      ```swift
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        query.include(Event.KEY_USER);
        query.setLimit(20);
        query.addDescendingOrder(Event.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> events, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue with getting events", e);
                    return;
                }
                for (Post post: posts){
                    Log.i(TAG, "Event: " + event.getDescription() + " username: " + event.getUser().getUsername());
                }
                allEvents.addAll(events);
                adapter.notifyDataSetChanged();
            }
         }
        
        ```
      - (Create/POST) Create a new like on an event
      ```swift        
        Like like = new Like();
        like.setEvent(event);
        like.setUser(currentUser);
        like.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue with liking event", e);
                    Toast.makeText(getContext(), "Issue with liking event", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Liked!!");
            }
        });
        ```
      - (Delete) Delete existing like
      ```swift        
        like.deleteLike(where user = current user && event = event);
        event.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue with removing like", e);
                    Toast.makeText(getContext(), "Issue with saving like", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Unliked!!");
        ```
      - (Create/POST) Create a new comment on a post
       ```swift        
        Comment comment = new Comment();
        comment.setText(text);
        comment.setUser(currentUser);
        comment.setEvent(event);
        comment.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue with commenting", e);
                    Toast.makeText(getContext(), "Issue with commenting", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ```
      - (Delete) Delete existing comment
   - My Event Liked List Screen
      - (Read/Event) View events liked by user
       ```swift
        ParseQuery<Like> query = ParseQuery.getQuery(Like.class);
        query.include(Like.KEY_USER);
        query.include(Like.KEY_EVENT);
        query.whereEqualTo(Event.KEY_USER, ParseUser.getCurrentUser());
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Like>() {
            @Override
            public void done(List<Like> likes, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue with getting likes", e);
                    return;
                }
                for (Like like: likes){
                    Log.i(TAG, "Liked: " + like.getEvent().getEventName();         
                }
                //turn likes into events
                allEvents.addEvents(events);
                adapter.notifyDataSetChanged();
            }
          });     
      ```
   - Profile Screen
      - (Read/GET) Query logged in user object
      - (Update/PUT) Update user profile image
      ```swift
        currentUser.put("profilePic", new ParseFile(photoFile));
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue with saving posts", e);
                    Toast.makeText(getContext(), "Issue with saving picture",                         Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Profile picture save was successful!!");
                ivPostImage.setImageResource(0);
            }
        });
        ```
   - Search Bar
      - (Read/GET) Query selected events based on characters entered
      
## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='https://github.com/NylanDW/InstagramCopy/blob/master/walkthrough2.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).  
