package org.gids.robot.speech;

import java.util.Locale;

import javax.sound.sampled.AudioInputStream;

import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import marytts.modules.synthesis.Voice;
import marytts.util.data.audio.AudioPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Breandan Considine
 */
public class TTSService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TTSService.class);
    private static MaryInterface maryTTS;
    private static Voice voice;

    private static TTSService instance = null;

    protected TTSService()
    {
    }

    public static TTSService getInstance()
    {
        if (instance != null)
        {
            return instance;
        }

        try
        {
            maryTTS = new LocalMaryInterface();
            Locale systemLocale = Locale.getDefault();
            if (maryTTS.getAvailableLocales().contains(systemLocale))
            {
                voice = Voice.getDefaultVoice(systemLocale);
            }

            if (voice == null)
            {
                voice = Voice.getVoice(maryTTS.getAvailableVoices().iterator().next());
            }

            maryTTS.setLocale(voice.getLocale());
            maryTTS.setVoice(voice.getName());
        } catch (MaryConfigurationException e)
        {
            e.printStackTrace();
        }
        instance = new TTSService();

        return instance;
    }

    public void say(String text)
    {
        if (text == null || text.isEmpty())
        {
            return;
        }

        try
        {
            AudioInputStream audio = maryTTS.generateAudio(text);
            AudioPlayer player = new AudioPlayer(audio);
            player.start();
            player.join();
        } catch (SynthesisException | InterruptedException e)
        {
            LOGGER.error(String.format("Sorry! Could not pronounce '%s'", text), e);
        }
    }

    public static void main(String[] args)
    {
        TTSService.getInstance();
        /*
        Scanner scan = new Scanner(System.in);

        while (true)
        {
            System.out.println("Text to speak:");
            TTSService.getInstance().say(scan.nextLine());
        }
        */
    }
}
