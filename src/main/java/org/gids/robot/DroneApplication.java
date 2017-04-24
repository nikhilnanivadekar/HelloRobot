package org.gids.robot;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.gids.robot.drone.ParrotController;
import org.gids.robot.resource.ParrotControllerConfiguration;
import org.gids.robot.resource.ParrotControllerResource;
import org.gids.robot.speech.ASRService;
import org.gids.robot.speech.TTSService;

/**
 * Created by Nikhil Nanivadekar
 * <p>
 * This is the main class for Dropwizard Application.
 * When you run the application following takes place:
 * <p>
 * 1) A Dropwizard server is started
 * <p>
 * 2) An Audio Recognition System is started
 * <p>
 * 3) A Text to Speech Service is started
 * <p>
 * 4) A DroneController is started which connects to AR Drone
 * <p>
 * 5) An UI is created for manual override. UI is accessible at: http://localhost:8080/ui/index.html
 * <p>
 */
public class DroneApplication extends Application<ParrotControllerConfiguration>
{
    private ParrotController parrotController;

    public static void main(String[] args) throws Exception
    {
        new DroneApplication().run(args);
    }

    @Override
    public void run(ParrotControllerConfiguration configuration, Environment environment) throws Exception
    {
        ParrotControllerResource resource = new ParrotControllerResource(this.parrotController);
        environment.jersey().register(resource);
        TTSService.getInstance();
        ASRService asrService = new ASRService(parrotController);
        asrService.init();
    }

    @Override
    public void initialize(Bootstrap<ParrotControllerConfiguration> bootstrap)
    {
        this.parrotController = new ParrotController("192.168.1.3");
        this.parrotController.connect();
        bootstrap.addBundle(new AssetsBundle("/ui", "/ui", null, "ui"));
    }
}
