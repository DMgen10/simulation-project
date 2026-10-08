package org.dm.actions;

import org.dm.core.GameMap;
import org.dm.core.GameSettings;

public interface Action {

     void execute(GameMap map, GameSettings settings);
}
