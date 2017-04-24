package org.gids.robot.speech;

/**
 * Grammar is defined in resources/org.gids.robot/grammars/command.gram
 */
public final class SpeechCallback
{
    public static void speechCallback(String result)
    {
        TTSService ttsService = TTSService.getInstance();
        if (result.equals("<unk>"))
        {
            ttsService.say("Sorry I did not follow the command");
        }
        if (result.equalsIgnoreCase("what is your name"))
        {
            ttsService.say("My name is Nimbus");
            ttsService.say("What is your name?");
        }
        if (result.equalsIgnoreCase("Hello"))
        {
            ttsService.say("Hi");
        }
        if (result.equalsIgnoreCase("Hi"))
        {
            ttsService.say("Hello");
        }
        if (result.equalsIgnoreCase("I am Nick"))
        {
            ttsService.say("Hello Nick! Are you enjoying GIDS?");
            ttsService.say("Hello Bangalore! How are you doing today?");
        }
        if (result.contains("fly")
                || result.contains("land")
                || result.contains("stop"))
        {
            ttsService.say("Nimbus executing");
            ttsService.say(result);
        }
        if (result.equalsIgnoreCase("terminate"))
        {
            ttsService.say("Hasta la vista, baby");
            ttsService.say("I'll be back");
        }
    }
}
