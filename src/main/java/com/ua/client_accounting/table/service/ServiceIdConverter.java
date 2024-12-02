package com.ua.client_accounting.table.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.logging.log4j.ThreadContext.isEmpty;

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

//    public Set<Long> convertServicesToIds(Object services) {
//        if (services == null) {
//            throw new IllegalArgumentException("Services cannot be null");
//        }
//
//        if (services instanceof String) {
//                return parseServiceIdsFromString((String) services);
//        } else if (services instanceof Collection<?>) {
//            return parseServiceIdsFromCollection((Collection<?>) services);
//        } else {
//            throw new IllegalArgumentException("Unsupported services type: " + services.getClass().getName());
//        }
//    }
//
//    private static Set<Long> parseServiceIdsFromString(String services) {
//        try {
//            return Arrays.stream(((String) services).split(","))
//                    .map(String::trim)
////                    .filter(s -> !isEmpty())
//                    .map(Long::parseLong)
//                    .collect(Collectors.toSet());
//        } catch (NumberFormatException e) {
//            throw new IllegalArgumentException("Invalid number format in services string: " + services, e);
//        }
//    }
//
//    private static Set<Long> parseServiceIdsFromCollection(Collection<?> services) {
//        return services.stream()
//                .map(Object::toString)
//                .map(String::trim)
////                .filter(s -> !isEmpty())
//                .map(Long::parseLong)
////                .map(s -> {
////                    try {
////                        return Long.parseLong(s);
////                    } catch (NumberFormatException e) {
////                        throw new IllegalArgumentException("Invalid number format in services collection: " + s, e);
////                    }
////                })
//                .collect(Collectors.toSet());
//    }
}
