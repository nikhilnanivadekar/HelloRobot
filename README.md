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

1. Once you have created the `drone-api.jar` (with all the dependencies), copy the jar to libraries folder in this project's repo.
2. Change the name of this dependency in `build.gradle` to match the name of jar which you created.
3. Run the gradle clean, build.
4. The project should build and compile fine.
5. Run the DroneApplication.java.
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