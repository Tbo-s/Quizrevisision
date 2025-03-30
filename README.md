# Quiz app

## Test documentation

| Navigate to quiz | |
|--|----------|
| Source | [app/src/androidTest/java/com/example/quizapp4/MainActivityTest.java](https://github.com/Tbo-s/Quizrevisision/blob/26527c4100e7c47abbcae2f23a8aada62383313c/QuizApp4/app/src/androidTest/java/com/example/quizapp4/MainActivityTest.java#L65) |
| Description | When the user clicks on the "Quiz" button in the gallery, the Quiz activity should be started. |
| Status | Passed |
-------------------

| Add image to gallery | |
|--|----------|
| Source | [app/src/androidTest/java/com/example/quizapp4/MainActivityTest.java](https://github.com/Tbo-s/Quizrevisision/blob/26527c4100e7c47abbcae2f23a8aada62383313c/QuizApp4/app/src/androidTest/java/com/example/quizapp4/MainActivityTest.java#L88) |
| Description | When the user clicks on the "Add picture" button in the gallery, the user should be prompted to choose an image and then write the name associated with it. This should result in a new image added to the gallery. |
| Status | Passed |


| Delete image from gallery | |
|--|----------|
| Source | [app/src/androidTest/java/com/example/quizapp4/MainActivityTest.java](https://github.com/Tbo-s/Quizrevisision/blob/26527c4100e7c47abbcae2f23a8aada62383313c/QuizApp4/app/src/androidTest/java/com/example/quizapp4/MainActivityTest.java#L71) |
| Description | When the user clicks on the "-" button next to an image in the gallery, the image should be removed. |
| Status | Passed |
-------------------

| Correct answer in quiz | |
|--|----------|
| Source | [app/src/androidTest/java/com/example/quizapp4/QuizActivityTest.java](https://github.com/Tbo-s/Quizrevisision/blob/26527c4100e7c47abbcae2f23a8aada62383313c/QuizApp4/app/src/androidTest/java/com/example/quizapp4/QuizActivityTest.java#L51) |
| Description | When a correct option is chosen in the quiz and the answer is submitted by clicking the "Submit" button, both the number of total answers and the number of correct answers should be increased by 1. |
| Status | Passed |


| Wrong answer in quiz | |
|--|----------|
| Source | [app/src/androidTest/java/com/example/quizapp4/QuizActivityTest.java](https://github.com/Tbo-s/Quizrevisision/blob/26527c4100e7c47abbcae2f23a8aada62383313c/QuizApp4/app/src/androidTest/java/com/example/quizapp4/QuizActivityTest.java#L29) |
| Description | When a wrong option is chosen in the quiz and the answer is submitted by clicking the "Submit" button, the number of total answers should be increased by 1, but the number of correct answers should stay the same. |
| Status | Passed |
-------------------
