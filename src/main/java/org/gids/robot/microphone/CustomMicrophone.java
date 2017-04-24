package org.gids.robot.microphone;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Breandan Considine
 */
public class CustomMicrophone
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomMicrophone.class);

    private final TargetDataLine line;
    private final AudioInputStream inputStream;

    private static final int DURATION = 4500;
    private static final String TEMP_FILE = "/tmp/X.wav";

    public CustomMicrophone(
            float sampleRate,
            int sampleSize,
            boolean signed,
            boolean bigEndian)
    {

        AudioFormat format =
                new AudioFormat(sampleRate, sampleSize, 1, signed, bigEndian);

        try
        {

            this.line = AudioSystem.getTargetDataLine(format);
            this.line.open();

            if (this.line.isControlSupported(FloatControl.Type.MASTER_GAIN))
            {
                LOGGER.info("Microphone: MASTER_GAIN supported");
            }
            else
            {
                LOGGER.warn("Microphone: MASTER_GAIN NOT supported");
            }

            //masterGainControl = findMGControl(line);

        } catch (LineUnavailableException e)
        {
            throw new IllegalStateException(e);
        }

        this.inputStream = new AudioInputStreamWithAdjustableGain(this.line);
    }

    public void startRecording()
    {
        this.line.start();
    }

    public void stopRecording()
    {
        this.line.stop();
    }

    public AudioInputStream getStream()
    {
        return this.inputStream;
    }

    //TODO Refactor this API into a CustomMicrophone instance
    public static File recordFromMic(long duration) throws IOException
    {
        CustomMicrophone mic = new CustomMicrophone(16000, 16, true, false);

        //Why is this in a thread?
        new Thread(() ->
        {
            try
            {
                Thread.sleep(duration);
            } catch (InterruptedException ignored)
            {
            } finally
            {
                mic.stopRecording();
            }
        }).start();

        mic.startRecording();

        File out = new File(TEMP_FILE);

        AudioSystem.write(mic.getStream(), AudioFileFormat.Type.WAVE, out);

        return out;
    }
}
