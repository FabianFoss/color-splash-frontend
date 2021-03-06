package com.mygdx.game.controllers;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import com.mygdx.game.ColorSplash;
import com.mygdx.game.EventsConstants;
import com.mygdx.game.views.GetReadyView;
import com.mygdx.game.views.MainMenuView;
import com.mygdx.game.ViewManager;
import com.mygdx.game.models.DisplayColors;
import com.mygdx.game.models.GameInfo;

import io.socket.emitter.Emitter;

public class GameLobbyController {

    private GameInfo gameInfo;
    private boolean isHost;
    private boolean isLoading = true;
    private DisplayColors colorInfo;
    private final ViewManager viewManager;

    public GameLobbyController(ViewManager viewManager) {
        this.viewManager = viewManager;
        this.gameCreated();
        this.displayColors();
    }

    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    public GameInfo getGameInfo() {
        return gameInfo;
    }

    public boolean isHost() {
        return isHost;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void displayColors() {
        ColorSplash.socketManager.createListener(EventsConstants.displayColors, colorListener());
    }

    public Emitter.Listener colorListener() {
        return new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gson gson = new Gson();
                colorInfo = gson.fromJson(args[0].toString(), DisplayColors.class);
                Gdx.app.postRunnable (new Runnable() {
                    @Override
                    public void run() {
                        setGetReadyView();
                    }
                });
            }
        };
    }

    public void gameCreated() {
        ColorSplash.socketManager.createListener(EventsConstants.gameInfo, gameCreatedListener());
    }

    public void startGame() {
        ColorSplash.socketManager.startGame(getGameInfo().getGameId());
        boolean loading = true;
        while (loading) {
            loading = isLoading();
        }
        setGetReadyView();
    }

    public void leaveGame() {
        ColorSplash.socketManager.leaveGame(this.gameInfo.getGameId());
        setMainMenuView();
    }

    public Emitter.Listener gameCreatedListener() {
        return new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                isLoading = true;
                Gson gson = new Gson();
                gameInfo = gson.fromJson(args[0].toString(), GameInfo.class);
                isHost = gameInfo.getHostId().equals(ColorSplash.socketManager.getSocketId());
                isLoading = false;
            }
        };
    }

    public void setMainMenuView() {
        viewManager.set(new MainMenuView(viewManager));
    }

    public void setGetReadyView() {
        ColorSplash.socketManager.removeEventListener(EventsConstants.gameInfo);
        viewManager.set(new GetReadyView(viewManager, colorInfo));
    }
}
