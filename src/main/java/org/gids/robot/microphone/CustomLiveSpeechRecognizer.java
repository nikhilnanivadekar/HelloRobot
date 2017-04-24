package org.gids.robot.microphone;

import java.io.IOException;

import edu.cmu.sphinx.api.AbstractSpeechRecognizer;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.decoder.ResultListener;
import edu.cmu.sphinx.frontend.endpoint.SpeechClassifier;
import edu.cmu.sphinx.frontend.util.StreamDataSource;

/**
 * High-level class for live speech recognition.
 * <p>
 * Created by Breandan Considine
 */
public class CustomLiveSpeechRecognizer extends AbstractSpeechRecognizer
{
    /*
     * sphinx4 default sensitivity is 13.
     */
    private static final int SPEECH_SENSITIVITY = 20;

    private final CustomMicrophone microphone;

    /**
     * Constructs new live recognition object.
     *
     * @param configuration common configuration
     * @throws IOException if model IO went wrong
     */
    public CustomLiveSpeechRecognizer(Configuration configuration) throws IOException
    {
        super(configuration);
        this.microphone = new CustomMicrophone(16000, 16, true, false);

        this.context.getInstance(StreamDataSource.class)
                .setInputStream(this.microphone.getStream());

        this.context.setLocalProperty(String.format("speechClassifier->%s", SpeechClassifier.PROP_THRESHOLD), SPEECH_SENSITIVITY);
    }

    /**
     * Starts recognition process.
     *
     * @param clear clear cached microphone data
     * @see CustomLiveSpeechRecognizer#stopRecognition()
     */
    public void startRecognition(boolean clear)
    {
        this.recognizer.allocate();
        this.microphone.startRecording();
    }

    /**
     * Stops recognition process.
     * <p>
     * Recognition process is paused until the next call to startRecognition.
     *
     * @see CustomLiveSpeechRecognizer#startRecognition(boolean)
     */
    public void stopRecognition()
    {
        this.microphone.stopRecording();
        this.recognizer.deallocate();
    }

    public void addResultListener(ResultListener listener)
    {
        this.recognizer.addResultListener(listener);
    }

    public void removeResultListener(ResultListener listener)
    {
        this.recognizer.removeResultListener(listener);
    }

//    public void setMasterGain(double mg) {
//        microphone.setMasterGain(mg);
//    }
//
//    public void setNoiseLevel(double mg) {
//        microphone.setNoiseLevel(mg);
//    }
}
