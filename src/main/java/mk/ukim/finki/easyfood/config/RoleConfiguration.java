package mk.ukim.finki.easyfood.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "app.roles")
public class RoleConfiguration {

    private Map<String, RoleConfig> configs = new HashMap<>();

    public static class RoleConfig {
        private String dashboardUrl;
        private List<String> allowedPaths;
        private String displayName;
        private int priority;

        public String getDashboardUrl() { return dashboardUrl; }
        public void setDashboardUrl(String dashboardUrl) { this.dashboardUrl = dashboardUrl; }

        public List<String> getAllowedPaths() { return allowedPaths; }
        public void setAllowedPaths(List<String> allowedPaths) { this.allowedPaths = allowedPaths; }

        public String getDisplayName() { return displayName; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }

        public int getPriority() { return priority; }
        public void setPriority(int priority) { this.priority = priority; }
    }

    public Map<String, RoleConfig> getConfigs() { return configs; }
    public void setConfigs(Map<String, RoleConfig> configs) { this.configs = configs; }

    public String getDashboardUrl(String role) {
        RoleConfig config = configs.get(role.replace("ROLE_", ""));
        return config != null ? config.getDashboardUrl() : "/home";
    }

    public List<String> getAllowedPaths(String role) {
        RoleConfig config = configs.get(role.replace("ROLE_", ""));
        return config != null ? config.getAllowedPaths() : List.of();
    }
}