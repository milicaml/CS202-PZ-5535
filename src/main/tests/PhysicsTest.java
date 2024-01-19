
import com.pz.game.Entity;
import com.pz.game.Physics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhysicsTest {

    private Entity entity1;
    private Entity entity2;
    private Entity entity3;

    @BeforeEach
    void setUp() {
        entity1 = new Entity(null, 10, 10);
        entity2 = new Entity(null, 20, 20);
        entity3 = new Entity(null, 30, 30);
        Physics.addEntity(entity1);
        Physics.addEntity(entity2);
        Physics.addEntity(entity3);
    }

    @Test
    void testHandleCollision() {
        // Set up initial positions and collision types
        entity1.setPosition(0, 0);
        entity1.setCollisionType(Physics.CollisionType.DISCRETE);

        entity2.setPosition(20, 20);
        entity2.setCollisionType(Physics.CollisionType.DISCRETE);

        entity3.setPosition(50, 50);
        entity3.setCollisionType(Physics.CollisionType.NONE);

        // Call the handleCollision method
        Physics.handleCollision();

        // Assert that the entities have been properly modified after the collision
        assertEquals(0, entity1.getTranslateX());
        assertEquals(-17.5, entity1.getTranslateY());

        assertEquals(14.0, entity2.getTranslateX());
        assertEquals(-3, entity2.getTranslateY());

        assertEquals(50, entity3.getTranslateX());
        assertEquals(50, entity3.getTranslateY());
    }
}