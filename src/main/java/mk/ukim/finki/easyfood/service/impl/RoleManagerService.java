package mk.ukim.finki.easyfood.service.impl;


import mk.ukim.finki.easyfood.config.RoleConfiguration;
import mk.ukim.finki.easyfood.model.enumerations.ROLE;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoleManagerService {

    private final RoleConfiguration roleConfiguration;
    /*
    CUSTOMER,
    DELIVERY_MAN,
    RESTAURANT_OWNER,
    ADMIN
     */

    private static final Map<String, Set<String>> ROLE_HIERARCHY = Map.of(
            "SUPER_ADMIN", Set.of("ADMIN", "RESTAURANT_OWNER", "CUSTOMER", "DELIVERY_MAN"),
            "ADMIN", Set.of("RESTAURANT_OWNER", "CUSTOMER", "DELIVERY_MAN"),
            "RESTAURANT_OWNER", Set.of("CUSTOMER"),
            "MANAGER", Set.of("CUSTOMER", "DELIVERY_MAN")
    );

    public RoleManagerService(RoleConfiguration roleConfiguration) {
        this.roleConfiguration = roleConfiguration;
    }


    public Set<String> getEffectiveRoles(String role) {
        Set<String> effectiveRoles = new HashSet<>();
        effectiveRoles.add(role);

        if (ROLE_HIERARCHY.containsKey(role)) {
            effectiveRoles.addAll(ROLE_HIERARCHY.get(role));
        }

        return effectiveRoles;
    }


    public boolean hasPermission(String role, String path) {
        Set<String> effectiveRoles = getEffectiveRoles(role);

        return effectiveRoles.stream()
                .anyMatch(r -> {
                    List<String> allowedPaths = roleConfiguration.getAllowedPaths("ROLE_" + r);
                    return allowedPaths.stream().anyMatch(allowedPath ->
                            pathMatches(path, allowedPath));
                });
    }


    public String getBestDashboardUrl(Collection<String> userRoles) {
        return userRoles.stream()
                .map(role -> role.replace("ROLE_", ""))
                .filter(role -> roleConfiguration.getConfigs().containsKey(role))
                .max(Comparator.comparing(role ->
                        roleConfiguration.getConfigs().get(role).getPriority()))
                .map(role -> roleConfiguration.getDashboardUrl("ROLE_" + role))
                .orElse("/home");
    }


    public Set<String> getAllAvailableRoles() {
        return roleConfiguration.getConfigs().keySet();
    }

    public Map<String, Object> getRoleInfo(String role) {
        RoleConfiguration.RoleConfig config = roleConfiguration.getConfigs().get(role.replace("ROLE_", ""));
        if (config == null) {
            return Map.of();
        }

        return Map.of(
                "role", role,
                "displayName", config.getDisplayName(),
                "dashboardUrl", config.getDashboardUrl(),
                "allowedPaths", config.getAllowedPaths(),
                "priority", config.getPriority(),
                "effectiveRoles", getEffectiveRoles(role.replace("ROLE_", ""))
        );
    }

    public boolean canAccessDashboard(Collection<String> userRoles, String targetRole) {
        return userRoles.stream()
                .map(role -> role.replace("ROLE_", ""))
                .flatMap(role -> getEffectiveRoles(role).stream())
                .anyMatch(role -> role.equals(targetRole.replace("ROLE_", "")));
    }


    private boolean pathMatches(String actualPath, String pattern) {
        if (pattern.endsWith("/**")) {
            String basePattern = pattern.substring(0, pattern.length() - 3);
            return actualPath.startsWith(basePattern);
        } else if (pattern.endsWith("/*")) {
            String basePattern = pattern.substring(0, pattern.length() - 2);
            return actualPath.startsWith(basePattern) &&
                    actualPath.indexOf('/', basePattern.length() + 1) == -1;
        }
        return actualPath.equals(pattern);
    }


    public static String fromEnum(ROLE role) {
        return "ROLE_" + role.name();
    }
}
