package com.ua.client_accounting.table.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ServiceIdConverter {
    public Set<Long> convertServicesToIds(Object services) {
        Set<Long> serviceIds = new HashSet<>();

        if (services instanceof String) {
            try {
                serviceIds = Arrays.stream(((String) services).split(","))
                        .map(String::trim)
                        .map(Long::parseLong)
                        .collect(Collectors.toSet());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid format in services string: " + serviceIds, e);
            }
        } else if (services instanceof Collection<?>) {
            serviceIds = ((Collection<?>) services).stream()
                    .map(Object::toString)
                    .map(String::trim)
                    .map(Long::parseLong)
                    .collect(Collectors.toSet());
        } else {
            throw new IllegalArgumentException("Unsupported services type: " + services.getClass().getName());
        }
        return serviceIds;
    }
}
