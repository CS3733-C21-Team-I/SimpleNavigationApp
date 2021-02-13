package edu.wpi.ithorian.database;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class DatabaseStructureTesting {

    @Test
    public void testDatabaseRefAllocation() {
        DatabaseRef ref1 = DatabaseRef.getConnection("jdbc:derby:navDB", false);

        DatabaseRef ref2 = DatabaseRef.getConnection("jdbc:derby:navDB", false);

        assertNotNull(ref2);
        assertEquals(ref1, ref2);
    }

    @Test
    void testInitMultipleManagers() {
        NavDatabaseManager.init(false);
        UserDatabaseManager.init(false);
        ServiceTicketDatabaseManager.init(true);

        assertNotNull(NavDatabaseManager.getInstance());
        assertNotNull(UserDatabaseManager.getInstance());
    }

    @Test
    void testInitMultipleManagersWithRegen() {
        NavDatabaseManager.init(true);
        UserDatabaseManager.init(true);

        assertNotNull(NavDatabaseManager.getInstance());
        assertNotNull(UserDatabaseManager.getInstance());
    }
}
