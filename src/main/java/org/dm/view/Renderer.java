package org.dm.view;

import org.dm.core.GameMap;

public interface Renderer {

    void render(GameMap map, int turn);

    void showMessage(String message);
}
