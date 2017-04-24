package org.gids.robot.speech;

import java.io.IOException;

import edu.cmu.sphinx.api.Configuration;
import org.gids.robot.drone.ParrotController;
import org.gids.robot.microphone.CustomLiveSpeechRecognizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Breandan Considine
 */
public class ASRService
{
    public static final double MASTER_GAIN = 0.85;
    public static final double CONFIDENCE_LEVEL_THRESHOLD = 0.5;

    private static final String ACOUSTIC_MODEL = "resource:/edu.cmu.sphinx.models.en-us/en-us";

    private static final String DICTIONARY_PATH = "resource:/edu.cmu.sphinx.models.en-us/cmudict-en-us.dict";
    private static final String GRAMMAR_PATH = "resource:/org.gids.robot/grammars";
    private static final Logger LOGGER = LoggerFactory.getLogger(ASRService.class);

    private final ParrotController parrotController;

    private Thread speechThread;

    public ASRService(ParrotController parrotController)
    {
        this.parrotController = parrotController;
    }

    private CustomLiveSpeechRecognizer recognizer;

    public void init()
    {
        Configuration configuration = new Configuration();
        configuration.setAcousticModelPath(ACOUSTIC_MODEL);
        configuration.setDictionaryPath(DICTIONARY_PATH);
        configuration.setGrammarPath(GRAMMAR_PATH);
        configuration.setUseGrammar(true);

        configuration.setGrammarName("command");

        try
        {
            this.recognizer = new CustomLiveSpeechRecognizer(configuration);
//            recognizer.setMasterGain(MASTER_GAIN);
            this.speechThread = new Thread(new ASRControlLoop(this.recognizer, this.parrotController), "ASR Thread");
            this.recognizer.startRecognition(true);
            // Fire up control-loop
            this.speechThread.start();
        } catch (IOException e)
        {
            LOGGER.error("Couldn't initialize speech recognizer:", e);
        }
    }

    public boolean activate()
    {
//        if (getStatus() == Status.INIT) {
//            // Cold start prune cache
//            recognizer.startRecognition(true);
//        }

        return ListeningState.activate();
    }

    public boolean deactivate()
    {
        return ListeningState.standBy();
    }

    public void dispose()
    {
        // Deactivate in the first place, therefore actually
        // prevent activation upon the user-input
        this.deactivate();
        this.terminate();
    }

    private void terminate()
    {
        this.recognizer.stopRecognition();
    }

    // This is for testing purposes solely
    public static void main(String[] args)
    {
        ASRService asrService = new ASRService(new ParrotController(""));
        asrService.init();
        ListeningState.activate();
    }
}
