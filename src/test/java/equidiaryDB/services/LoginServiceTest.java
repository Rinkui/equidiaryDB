package equidiaryDB.services;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import equidiaryDB.EquidiaryDB;
import equidiaryDB.GenericTestCase;
import org.junit.After;
import org.junit.Before;

import java.nio.file.Files;

import static equidiaryDB.TestConstant.CONFIG_PROPERTIES;
import static equidiaryDB.TestConstant.CONFIG_TEST;
import static org.junit.Assert.assertEquals;

public class LoginServiceTest extends GenericTestCase
{
    private static final String EQUIDIARYDB_PORT = "7001";
    private static final String EQUIDIARYDB_IP   = "localhost";
    private static final String EQUIDIARYDB_PATH = "http://" + EQUIDIARYDB_IP + ":" + EQUIDIARYDB_PORT;
    private static final String LOGIN_ENDPOINT   = "/login";

    private static HttpResponse<String> response;

    @Before
    public void setup() throws Exception
    {
        Files.deleteIfExists(CONFIG_PROPERTIES);
        Files.copy(CONFIG_TEST, CONFIG_PROPERTIES);
        EquidiaryDB.start();
    }

    @After
    public void teardown()
    {
        EquidiaryDB.stop();
        response = null;
    }

    @Test
    public void loginNominal() throws Exception {
        String loginBody = givenUserWithPasswordBody("marie", "marie");
        whenLogin(loginBody);
        thenResponseIs("Hello marie");
    }

    @Test
    public void loginWithNonExistingUsernameInDB() throws Exception {
        String loginBody = givenUserWithPasswordBody("wrong", "marie");
        whenLogin(loginBody);
        thenResponseIs("Wrong username or password");
    }

    @Test
    public void loginWithWrongPasswordInDB() throws Exception {
        String loginBody = givenUserWithPasswordBody("wrong", "marie");
        whenLogin(loginBody);
        thenResponseIs("Wrong username or password");
    }

    // GIVEN
    private String givenUserWithPasswordBody( final String userName,
                                              final String password )
    {
        return "{\"username\":" + userName + ";\"password\":" + password + "}";
    }

    // WHEN
    private void whenLogin( String loginBody ) throws UnirestException
    {
        response = Unirest.post(EQUIDIARYDB_PATH + LOGIN_ENDPOINT).body(loginBody).asString();
    }

    // THEN
    private void thenResponseIs( String expected )
    {
        assertEquals(expected, response.getBody());
    }
}
