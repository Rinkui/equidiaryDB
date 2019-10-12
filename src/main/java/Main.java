import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7001);
        app.get("/", ctx -> ctx.result("Hello World"));
        app.get("/users/:name", ctx -> ctx.result("Hello " + ctx.pathParam("name")));
    }
}
