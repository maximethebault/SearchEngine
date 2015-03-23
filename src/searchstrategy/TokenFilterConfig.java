package searchstrategy;

import java.util.HashMap;
import java.util.Map;

public class TokenFilterConfig {
    private String name;
    private Map<String, String> parameters;

    public TokenFilterConfig(String name) {
        this.name = name;
        this.parameters = new HashMap<String, String>();
    }

    public TokenFilterConfig(String name, Map<String, String> parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }
}