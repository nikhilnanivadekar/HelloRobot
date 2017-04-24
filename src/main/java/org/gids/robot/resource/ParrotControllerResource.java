package org.gids.robot.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;
import org.gids.robot.drone.ParrotController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Nikhil Nanivadekar.
 */
@Path("/ardrone")
@Produces(MediaType.APPLICATION_JSON)
public class ParrotControllerResource
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ParrotControllerResource.class);

    private final ParrotController parrotController;

    public ParrotControllerResource(ParrotController parrotController)
    {
        this.parrotController = parrotController;
    }

    @GET
    @Timed
    @Path("/executeOverride/{overrideAction}")
    public String executeOverride(
            @PathParam("overrideAction") String overrideAction)
    {
        LOGGER.info("Executing:{}", overrideAction);
        return this.parrotController.executeOverride(overrideAction.toUpperCase());
    }
}
