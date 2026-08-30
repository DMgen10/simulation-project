import org.dm.GameMap;
import org.dm.Position;
import org.dm.entities.Predator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GameMapTest {
    private Position position;
    private GameMap gameMap;
    @Before
    public void createPosition(){
        position = new Position(10,10);
    }

    @Before
    public void crateGameMap(){
        gameMap = new GameMap(11,11);
    }


    @Test
    public void isPositionIsBusyTest(){
        createPosition();
        crateGameMap();
        position = getPosition();
        gameMap = getGameMap();
        gameMap.add(new Position(10,10), new Predator());
        Assert.assertTrue(gameMap.isPositionIsBusy(position));

    }

    public Position getPosition() {
        return position;
    }

    public GameMap getGameMap() {
        return gameMap;
    }
}
