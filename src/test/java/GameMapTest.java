import org.dm.GameMap;
import org.dm.Position;
import org.dm.entities.Entity;
import org.dm.entities.Predator;
import org.junit.Assert;
import org.junit.Test;

public class GameMapTest {

    public GameMap map = new GameMap(10,10);
    public Position position = new Position(5,5);
    public Entity entity = new Predator();

    @Test
    public void addTest(){
        map.add(position,entity);
        Assert.assertFalse(map.isPositionBusy(position));
    }

    @Test
    public void getEntityTest(){
        map.add(position,entity);
        Assert.assertEquals(map.getEntity(position),map.getEntity(position));
    }

    @Test
    public void removeTest(){
        map.add(position,entity);
        map.remove(position);
        Assert.assertFalse(map.isPositionBusy(position));
    }

    @Test
    public void isPositionBusyTest(){
        map.add(position,entity);
        map.remove(position);
        Assert.assertFalse(map.isPositionBusy(position));
    }

    @Test
    public void isOutOfBoundsTest(){
        Position freakPosition = new Position(11,11);
        Assert.assertTrue(map.isOutOfBounds(freakPosition));
    }

   }
