package com.iut.jumper.core.managers.GameLoopManager;

import android.util.Log;

import com.iut.jumper.core.managers.GameService;
import com.iut.jumper.views.GameView;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameLoopTask implements Runnable {

    private final ScheduledExecutorService scheduler;

    private final GameService gameService;

    private final GameView gameView;

    private volatile boolean running;
    private final long interval;
    private final TimeUnit unit;

    public GameLoopTask(ScheduledExecutorService scheduler, GameService gameService, GameView gameView) {
        this.gameService = gameService;
        this.scheduler = scheduler;

        this.gameView = gameView;

        this.interval = 16;
        this.unit = TimeUnit.MILLISECONDS;
        this.running = false;
    }

    public void start() {
        if (!this.running) {
            this.running = true;
            this.scheduleNextInvocation();
        }
    }

    public void pause() {
        if (this.running) {
            this.running = false;
        }
    }

    public void resume() {
        if (!this.running) {
            this.running = true;
            this.scheduleNextInvocation();
        }
    }

    private void scheduleNextInvocation() {
        this.scheduler.schedule(this, this.interval, this.unit);
    }

    @Override
    public void run() {
        if (!this.running) {
            return;
        }
        this.execute();
        this.scheduleNextInvocation();
    }

    private void execute() {
        Log.d("GAMELOOP", "beep");
        this.gameService.getPositionManager().update();
        this.gameView.postInvalidate();
    }
}
