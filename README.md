# HelloRobot
Repository for code of voice controlled AR Parrot Drones and other robots.

# Authors (in alphabetical order)
1. Breandan Considine
2. Nikhil J. Nanivadekar

# Description


# Instructions

This project is created such that it can be easily setup in IntelliJ. If you want to use any other IDE, setup instructions might be different.

## Before you Start
You will need to create `drone-api.jar`.
You can create the jar from the [Parroteer Project](https://github.com/parrotsonjava/parroteer). 
Make sure you include the dependencies while creating the jar.

## Step by Step Procedure
1. Once you have created the `drone-api.jar` (with all the dependencies), copy the jar to libraries folder in this project's repo.
2. Change the name of this dependency in `build.gradle` to match the name of jar which you created.
3. Run the gradle clean, build.
4. The project should build and compile fine.
5. Run the DroneApplication.java.
6. It should spin up a Dropwizard Application with an override UI accessible at localhost:8080/ui/index.html .
7. Now you have an application which follows speech commands as well as has an override control center in case you need to take immediate control.
8. You can find all the commands supported in command.gram.

This application follows the general Dropwizard Application paradigms.

# Inspiration and Help
Main sources for AR Parrot Drone controller are:
1. [Parroteer Project](https://github.com/parrotsonjava/parroteer).
2. [Autonomous4j](https://github.com/RaspberryPiWithJava/Autonomous4j)
