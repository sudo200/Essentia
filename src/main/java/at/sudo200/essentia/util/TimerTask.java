package at.sudo200.essentia.util;

import org.jetbrains.annotations.NotNull;

/**
 * Utility class for adapting lambdas into a {@link java.util.TimerTask}
 *
 * @author sudo200
 */
public class TimerTask extends java.util.TimerTask {
    private final Runnable runnable;

    public TimerTask(@NotNull Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void run() {
        runnable.run();
    }
}
