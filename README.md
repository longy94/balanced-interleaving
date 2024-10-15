# Balanced Interleaving
Implement balanced interleaving ranking algorithm with Java.
# Installation
```bash
git clone https://github.com/longy94/balanced-interleaving.git
```
# Design
In this project, the post from __new__ algorithm is taken first. <br/>
The program will go through post from new and old in turn. For each post, add it to merged list if it doesn't exist in that list. <br/>
I have several ideas about how to check if a post is already added or not: <br/>
+ Go through merged list: Time complexity is `O(n)` but no need extra space
+ Use extra space to mark added post: Time complexity is `O(1)` but it requires extra space
  + Hash set: Implementation is easier, and is able to handle all types of post id.
  + Bitmap: Save a lot of spaces, but implementation is a little bit difficult and can only be used to handle numeric post id.

As a web service, quick response is more important than space usage. And considering that post id maybe not be numeric if you're using `UUID` or `Snowflake Id`. Therefore, I decided to use a hash set to implement balanced interleaving.<br/>
New posts and old posts are generated when application starts and won't change, so the result won't change with the same request.

# Test
Run `DemoApplication` <br/>
Input to browser
http://localhost:8080/interleaving/{userId}?page={page}&per_page={perPage} <br/>

# Unit Test
Normal case and error cases are listed in `DemoApplicationTests`.<br/>
Old posts is an ascending list without any duplicate and gap ranging from `1` to `n`.<br>
New posts is generated randomly. You can also customize input by returning your own list in `DemoApplicationTests#createNewPosts()`