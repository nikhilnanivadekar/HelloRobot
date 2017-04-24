package org.gids.robot.speech;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import edu.cmu.sphinx.api.SpeechResult;
import org.gids.robot.drone.ParrotController;
import org.gids.robot.microphone.CustomLiveSpeechRecognizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Breandan Considine
 */
public class ASRControlLoop implements Runnable
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ASRControlLoop.class);

    private CustomLiveSpeechRecognizer recognizer;
    private ParrotController parrotController;

    public ASRControlLoop(CustomLiveSpeechRecognizer recognizer, ParrotController parrotController)
    {
        // Start-up recognition facilities
        this.recognizer = recognizer;
        this.parrotController = parrotController;
    }

    @Override
    public void run()
    {
        while (!ListeningState.isTerminated())
        {
            // This blocks on a recognition result
            String result = this.getResultFromRecognizer();
            SpeechCallback.speechCallback(result);
            if (this.parrotController != null)
            {
                this.parrotController.executeAction(result);
            }
        }
    }

    private String getResultFromRecognizer()
    {
        SpeechResult result = this.recognizer.getResult();

        LOGGER.info(String.format("Recognized: Top H:%s /%s  /%s ", result.getResult(), result.getResult().getBestToken(), result.getResult().getBestPronunciationResult()));

        return result.getHypothesis();
    }

    // Helpers
    public static synchronized void beep()
    {
        Thread t = new Thread(() ->
        {
            try
            {
                Clip clip = AudioSystem.getClip();
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                        ASRService.class.getResourceAsStream("/com.jetbrains.idear/sounds/beep.wav"));
                clip.open(inputStream);
                clip.start();
            } catch (Exception e)
            {
                System.err.println(e.getMessage());
            }
        });

        t.start();

        try
        {
            t.join();
        } catch (InterruptedException ignored)
        {
            // Do nothing
        }
    }

    private static String splitCamelCase(String s)
    {
        return s.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"),
                " ");
    }
}
