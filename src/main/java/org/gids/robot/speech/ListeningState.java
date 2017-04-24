package org.gids.robot.speech;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by breandan on 10/23/2015.
 */
public class ListeningState
{
    private static final AtomicReference<Status> STATUS = new AtomicReference<>(Status.INIT);

    public enum Status
    {
        INIT,
        ACTIVE,
        STANDBY,
        TERMINATED
    }

    private static Status setStatus(Status s)
    {
        return STATUS.getAndSet(s);
    }

    public static Status getStatus()
    {
        return STATUS.get();
    }

    public static boolean isTerminated()
    {
        return getStatus() == Status.TERMINATED;
    }

    public static boolean isInit()
    {
        return getStatus() == Status.INIT;
    }

    public static boolean isActive()
    {
        return getStatus() == Status.ACTIVE;
    }

    public static boolean standBy()
    {
        return Status.STANDBY == setStatus(Status.STANDBY);
    }

    public static boolean activate()
    {
        return Status.ACTIVE == setStatus(Status.ACTIVE);
    }
}
