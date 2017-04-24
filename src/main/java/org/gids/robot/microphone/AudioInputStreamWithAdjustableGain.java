package org.gids.robot.microphone;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.TargetDataLine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Breandan Considine
 */
public class AudioInputStreamWithAdjustableGain extends AudioInputStream
{
    private static final double DEFAULT_MASTER_GAIN = 1.0;
    private static final double DEFAULT_NOISE_LEVEL = 0.0;
    private static final Logger LOGGER = LoggerFactory.getLogger(AudioInputStreamWithAdjustableGain.class);

    private double masterGain;
    private double noiseLevel;

    AudioInputStreamWithAdjustableGain(TargetDataLine line)
    {
        super(line);

        this.masterGain = DEFAULT_MASTER_GAIN;
        this.noiseLevel = DEFAULT_NOISE_LEVEL;
    }

    @Override
    public int read() throws IOException
    {
        return super.read();
    }

    @Override
    public int read(byte[] b) throws IOException
    {
        int read = super.read(b);

        this.dump(b, 0, b.length);

        for (int i = 0; i < read; ++i)
        {
            b[i] = this.adjust(b[i]);
        }

        this.dump(b, 0, b.length);

        return read;
    }

    private void dump(byte[] b, int off, int len)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = off; i < off + len - 1; ++i)
        {
            sb.append(b[i]);
            if (i != off + len - 1)
            {
                sb.append(", ");
            }
        }
        LOGGER.info(sb.toString());
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException
    {
        int read = super.read(b, off, len);

        //dump(b, off, read);

        for (int i = off; i < off + read; ++i)
        {
            b[i] = this.adjust(b[i]);
        }

        //dump(b, off, read);

        return read;
    }

    public double setMasterGain(double mg)
    {
        double pmg = this.masterGain;
        this.masterGain = mg;
        return pmg;
    }

    public double setNoiseLevel(double nl)
    {
        double pnl = this.noiseLevel;
        this.noiseLevel = nl;
        return pnl;
    }

    private byte adjust(byte b)
    {
        return this.cut((byte) (b * this.masterGain));
    }

    private byte cut(byte b)
    {
        return b < Byte.MAX_VALUE * this.noiseLevel && b > Byte.MIN_VALUE * this.noiseLevel ? 0 : b;
    }
}
