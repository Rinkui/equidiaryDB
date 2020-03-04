package equidiaryDB.database;

import equidiaryDB.EquidiaryDB;
import org.junit.Test;

public class DBMigrationTest
{
    @Test
    public void toto() throws Exception
    {
        givenEmptyDataBase();
        whenStartApp();
        thenAppStartCorrectly();
    }

    private void thenAppStartCorrectly()
    {

    }

    private void whenStartApp() throws Exception
    {
        EquidiaryDB.start();
    }

    private void givenEmptyDataBase()
    {

    }
}
