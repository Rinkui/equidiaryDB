package equidiaryDB.config;

public class NullConfig extends Config {
    public static final Config INSTANCE = new NullConfig();

    private NullConfig() {
        super(null);
    }
}
