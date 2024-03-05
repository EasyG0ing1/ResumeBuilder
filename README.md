# Resume Builder
Resume Builder is a program that provides you with critical questions about your previous job titles, that you answer, then automatically generate text to send to ChatGPT so that it can build your action statements that are most compatible with the `Applicant Tracking System` resume screener that most employers use today.

The best way to use it is to place your past job titles in the first question, each one on a new line.

Then, when answering the other questions, preface the answer with the job title followed by bullet points for that title, like this:

```
Job Title:
- answer information
- more information

Job Title:
- information
- more information
```

Each question has a tooltip that you can see when you hover the mouse on it, it gives you more insight into what the question is looking for.

When you've answered all of the questions, click on the button to copy the text to your clipboard, then go to your ChatGPT prompt and paste in the contents of your clipboard and it will give you a list of action statements that you can put into your resume.

## Installation
- You will need Java JDK 21 installed with the JAVA_HOME environment variable properly set.
- You will need Maven installed, whatever the latest version is.

When you type `mvn --version` you should get the response that indicates the correct maven version as well as the correct java version.

Then, from your shell, create or go into a folder that you can clone the project to then clone it:
```
git clone https://github.com/EasyG0ing1/ResumeBuilder.git
```

Next, I recommend adding this to your bash or zShell profile:
```BASH
resume() {
  cd /path/to/the/cloned/folder/ResumeBuilder
  mvn javafx:run
}
```
Then close and re-open your shell and run the program:
```BASH
resume
```

If the JDK and Maven are correct, it should run just fine.

Create an issue if you are having any problems, and I'll see if I can help.

Thanks!
