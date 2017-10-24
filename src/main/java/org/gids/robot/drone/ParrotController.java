package org.gids.robot.drone;

import com.dronecontrol.droneapi.DroneController;
import com.dronecontrol.droneapi.ParrotDroneController;
import com.dronecontrol.droneapi.commands.composed.PlayLedAnimationCommand;
import com.dronecontrol.droneapi.data.Config;
import com.dronecontrol.droneapi.data.enums.LedAnimation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Nikhil Nanivadekar.
 * <p>
 * Concept and implementation adapted from: Autonomous4j
 * <p>
 * GitHub Link: https://github.com/RaspberryPiWithJava/Autonomous4j
 */
public class ParrotController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ParrotController.class);
    private static final float MAX = 1f;
    private static final float MIN = -MAX;
    private DroneController droneController;

    private final String ipAddress;
    private final Config config;

    public ParrotController(String ipAddress)
    {
        this.ipAddress = ipAddress;
        this.config = new Config("AR Parrot Drone", "Nikhil", 0);
    }

    public boolean connect()
    {
        try
        {
            this.droneController = ParrotDroneController.build();
            this.droneController.start(this.config);
            this.droneController.addVideoDataListener(bufferedImage ->
            {
                //Do Nothing
            });
        }
        catch (Exception e)
        {
            LOGGER.error("Exception creating new drone connection", e);
            return false;
        }
        return true;
    }

    public void disconnect()
    {
        if (this.droneController != null)
        {
            this.droneController.stop();
        }
    }

    public void maintainPosition(long milliseconds)
    {
        try
        {
            Thread.sleep(milliseconds);
        }
        catch (Exception e)
        {
            LOGGER.error("", e);
        }
    }

    public ParrotController hover()
    {
        this.move(0, 0, 0, 0);
        return this;
    }

    public ParrotController takeOff()
    {
        this.droneController.takeOff();
        return this;
    }

    public ParrotController land()
    {
        this.droneController.land();
        return this;
    }

    public ParrotController shutdown()
    {
        this.land();
        this.disconnect();
        return this;
    }

    public ParrotController forward(int speed)
    {
        return this.move(0f, -this.percentToFloat(speed), 0f, 0f);
    }

    public ParrotController reverse(int speed)
    {
        return this.forward(-speed);
    }

    public ParrotController up(int speed)
    {
        return this.move(0f, 0f, this.percentToFloat(speed), 0f);
    }

    public ParrotController down(int speed)
    {
        return this.up(-speed);
    }

    public ParrotController left(int speed)
    {
    // disable left in case someone calls it by mistake
//        return this.move(-this.percentToFloat(speed), 0f, 0f, 0f);
        return this;
    }

    public ParrotController right(int speed)
    {
    // disable right in case someone calls it by mistake
//        return this.left(-speed);
        return this;
    }

    public ParrotController move(float roll, float pitch, float gaz, float yaw)
    {
        roll = this.limit(roll, MIN, MAX);
        pitch = this.limit(pitch, MIN, MAX);
        gaz = this.limit(gaz, MIN, MAX);
        yaw = this.limit(yaw, MIN, MAX);

        this.droneController.move(roll, pitch, yaw, gaz);

        return this;
    }

    private float limit(float number, float min, float max)
    {
        return (number > max ? max : (number < min ? min : number));
    }

    private float percentToFloat(int speed)
    {
        return speed / 100.0f;
    }

    public ParrotController playLedAnimation(float frequency, int durationSeconds)
    {
        return this;
    }

    public ParrotController playLedAnimation(LedAnimation animation, float frequency, int durationSeconds)
    {
        this.droneController.executeCommandsAsync(
                new PlayLedAnimationCommand(this.config.getLoginData(),
                        animation,
                        frequency,
                        durationSeconds));
        this.maintainPosition(durationSeconds * 1000);

        return this;
    }

    public void executeAction(String result)
    {
        if (result.equalsIgnoreCase("fly up"))
        {
            this.up(10);
        }
        if (result.equalsIgnoreCase("fly down"))
        {
            this.down(10);
        }
        if (result.equalsIgnoreCase("fly left"))
        {
            this.left(2);
        }
        if (result.equalsIgnoreCase("fly right"))
        {
            this.right(2);
        }
        if (result.equalsIgnoreCase("fly off"))
        {
            this.takeOff();
        }
        if (result.equalsIgnoreCase("land"))
        {
            this.land();
        }
        if (result.equalsIgnoreCase("stop"))
        {
            this.land();
        }
        if (result.equalsIgnoreCase("terminate"))
        {
            this.shutdown();
        }
    }

    public String executeOverride(String overrideAction)
    {
        switch (overrideAction)
        {
            case "TAKEOFF":
                this.takeOff();
                return "Executed Take-Off";
            case "LAND":
                this.land();
                return "Executed Land";
            case "UP":
                this.up(10);
                return "Executed Up";
            case "DOWN":
                this.down(10);
                return "Executed Down";
            case "FORWARD":
                this.forward(10);
                return "Executed Forward";
            case "REVERSE":
                this.reverse(10);
                return "Executed Reverse";
            case "LEFT":
                this.left(20);
                return "Executed Left";
            case "RIGHT":
                this.right(20);
                return "Executed Right";
            case "SHUTDOWN":
                this.shutdown();
                return "Executed Shutdown";
            default:
                throw new IllegalStateException("Unknown command passed");
        }
    }
}
