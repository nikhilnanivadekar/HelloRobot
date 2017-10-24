# HelloRobot

Repository for code of voice controlled [Parrot AR Drones](http://developer.parrot.com/) and other robots.

# Authors (in alphabetical order)

1. Breandan M. Considine
2. Nikhil J. Nanivadekar

# Description

This repository contains up into several directories.

 * [drone](src/main/java/org/gids/robot/drone) - Flight controls for the Parrot Drone.
 * [speech](src/main/java/org/gids/robot/speech) - Speech recognition and synthesis.
    * [en-us model](/src/main/resources/edu.cmu.sphinx.models.en-us/en-us) is provided by default, however [pretrained models](https://sourceforge.net/projects/cmusphinx/files/Acoustic%20and%20Language%20Models/) for a number of languages are also available.

# Instructions

This project is created such that it can be easily setup in IntelliJ IDEA. If you want to use any other IDE, setup instructions might be different.

## Before you Start

You will need to create `drone-api.jar`.
You can create the jar from the [Parroteer Project](https://github.com/parrotsonjava/parroteer). 
Make sure you include the dependencies while creating the jar.

## Step by Step Procedure

1. Once you have created the `drone-api.jar` (with all the dependencies), copy the jar to `libraries/` folder in this project's repo.
2. Change the name of this dependency in `build.gradle` to match the name of jar which you created.
3. Run the gradle clean, build.
4. The project should build and compile fine.
5. Run the [DroneApplication.java](src/main/java/org/gids/robot/DroneApplication.java).
6. It should spin up a Dropwizard Application with an override UI accessible at localhost:8080/ui/index.html .
7. Now you have an application which follows speech commands as well as has an override control center in case you need to take immediate control.
8. You can find all the commands supported in [command.gram](src/main/resources/org.gids.robot/grammars/command.gram). Please see the [JSpeech Grammar Format](https://www.w3.org/TR/jsgf/) for more information on how to add your own commands.

This application follows the general Dropwizard Application paradigms.

# Inspiration and Help

Main sources for AR Parrot Drone controller are:

1. [Parroteer Project](https://github.com/parrotsonjava/parroteer).
2. [Autonomous4j](https://github.com/RaspberryPiWithJava/Autonomous4j)

We use the following libraries for speech recognition and synthesis:

1. [CMUSphinx](http://cmusphinx.sourceforge.net/)
2. [MaryTTS](http://mary.dfki.de/)
3. [Drone API Jar with Dependencies](https://github.com/RaspberryPiWithJava/Autonomous4j/tree/master/lib)

# Project in Detail
## Building the project
Ideally the build should work fine with gradle. Refer to `build.gradle`.
The project has 3 aspects to it namely, getting the CMU model, drone-api jars and standard library dependencies.

If the build is failing due to:
 1) Any dependencies missing in ASR or TTS, 
the main reason for that is the model in this project is outdated and needs to be upgraded.
Simplest way to get the updated dependencies is by searching for `edu.cmu.sphinx` dependency and updating to latest.
 2) Drone API jar is broken. This can happen if there is a mistake in `build.gradle` and local repository is not being referenced properly.
 Simplest way to fix that is to navigate to `flatDir` part of `build.gradle` and update the path to absolute path.
 
## Project components:

 1) Dropwizard application: This is to make use of the container and create it as a deployable
    - Main class: org.gids.robot.DroneApplication
 2) Automatic Speech Recognition (ASR): This is responsible to detect speech
	- Control loop run method: org.gids.robot.speech.ASRControlLoop#run
 3) Text to Speech (TTS): This is to playback text
	- Playback: org.gids.robot.speech.TTSService#say
 4) Parrot Controller: This is the interface to communicate with the AR Drone
	- Controller class: org.gids.robot.drone.ParrotController
 5) User Interface: http://localhost:8080/ui/index.html 
	
## Steps to start the Application:

### Starting the UI: 
 - Make sure you have internet connectivity
 - Comment the following lines in `org.gids.robot.DroneApplication`

 ```java
 	// this.parrotController = new ParrotController(DRONE_IP);
 	// this.parrotController.connect();
 ```
 - Run `org.gids.robot.DroneApplication` like a normal Java program
 - If you get an exception _regarding server config not found_ you need to update the run configuration with following parameters:
 	- Program arguments: server \<Enter Absolute path to example.yml>
 	- Working Directory: Base folder of your project Example: C:\HelloRobot
 	- Use Classpath of module: hello-robot-main
 	- Once the process runs and server is up, the UI should be up.
 	
 **NOTE: DO NOT CLOSE THE WEB BROWSER ON WHICH THE UI IS AVAILABLE**
 	
### Connecting to Parrot Drone:
 - Start the drone
 - Connect to the drone via WiFi. This means will you disconnect from the internet. That is fine and expected.
 - Get the i/p address of drone
 - Update the i/p address in `org.gids.robot.DroneApplication`, you should see a static final variable `DRONE_IP`
 - Make sure the following lines are uncommented in `org.gids.robot.DroneApplication`
 
 ```java
    this.parrotController = new ParrotController(DRONE_IP);
    this.parrotController.connect();
 ```
 - Run `org.gids.robot.DroneApplication` like a normal Java program
 - Switch to the UI. You can use the console to fly the drone
 - In addition to console you can use keyboard to fly the drone
 - Also, you can use speech commands to fly the drone

### Adding New Commands for Voice Recognition
 - You can add new commands in `command.gram`
 - You can add a new set of commands by creating a new tag or adding it to the `public <command>` line.
   Wherever you add it, you need to make sure the tags go in the `public <command>` line.
   For example 
 
 ```xml
    <new_command> = awesome command
    public <command> = EXISTING COMMANDS | <new_command> | <new_inline_command>
 ```
 
 - This will enable for the voice recognition to detect the command
 - Add what the system should play/say once new command is detected in `org.gids.robot.speech.SpeechCallback.speechCallback`
 - Add actions that the drone should perform in `org.gids.robot.drone.ParrotController.executeAction`
