import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONObject;

import Database.DataBase;
import io.javalin.Javalin;
import io.javalin.http.UnauthorizedResponse;;

public class Main {

    private static Options argumentReader() {
        Option configFileOption = Option.builder("conf").longOpt("configurationFile")
                .desc("Localisation of the configuration file").hasArg(true).argName("configurationFile").required(true)
                .build();

        final Options options = new Options();
        options.addOption(configFileOption);

        return options;
    }

    private static Properties readConfFile(Path configFilePath) throws IOException {
        Properties prop = new Properties();
        prop.load(Files.newInputStream(configFilePath));
        return prop;
    }

    public static void main(String[] args) throws ParseException, IOException, SQLException {
        // Command line reader
        final Options options = argumentReader();
        final CommandLineParser parser = new DefaultParser();
        final CommandLine line = parser.parse(options, args);

        // Read the configuration file
        Path configFilePath = FileSystems.getDefault().getPath(line.getOptionValue("configurationFile"));
        Properties configurationValues = readConfFile(configFilePath);
        String hostDB = configurationValues.getProperty("hostDB");
        String portDB = configurationValues.getProperty("portDB");
        String userDB = configurationValues.getProperty("userDB");
        String passwordDB = configurationValues.getProperty("passwordDB");
        String schemaDB = configurationValues.getProperty("schemaDB");

        if(hostDB == null || portDB == null || userDB == null || passwordDB == null || schemaDB == null){
            throw new IllegalArgumentException("One mandatry information is missing on configuration file given in argument.");
        }

        // Instantiate Database Object
        DataBase db = new DataBase(hostDB, portDB, userDB, passwordDB, schemaDB);

        Javalin app = Javalin.create().start(7001);
        app.get("/", ctx -> ctx.result("Hello World"));
        app.get("/users/:name", ctx -> ctx.result("Hello " + ctx.pathParam("name")));
        app.post("/login", ctx -> {
            String body = ctx.body();
            JSONObject jo = new JSONObject(body);
            String username = jo.getString("username");
            String password = jo.getString("password");
            int userId = db.isCorrectUser(username, password);
            if(userId >= 0){
                ctx.result("Hello " + ctx.pathParam("name"));
            } else {
                throw new UnauthorizedResponse("Wrong username or password");
            }
        });
    }
}
