package giuseppetavella.zero_chiamate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppEnvironment {

    @Value("${whereami}")
    private String whereami;

    public boolean isLocal() {
        return "LOCAL".equals(whereami);
    }

    public boolean isProduction() {
        return "PRODUCTION".equals(whereami);
    }

    public boolean isPreview() {
        return "PREVIEW".equals(whereami);
    }

    public String get() {
        return whereami;
    }
}