package br.com.senior.VoeFacil.domain.flight;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class FlightSpecification {

    public static Specification<FlightEntity> byDepartureAirport(UUID departureAirportId) {
        return (root, query, builder) ->
                builder.and(
                        builder.equal(root.get("departureAirport").get("id"), departureAirportId)
                );
    }

    public static Specification<FlightEntity> byArrivalAirport(UUID arrivalAirportId) {
        return (root, query, builder) ->
                builder.and(
                        builder.equal(root.get("arrivalAirport").get("id"), arrivalAirportId)
                );
    }

    public static Specification<FlightEntity> byNotCanceled() {
        return (root, query, builder) ->
                builder.and(
                        builder.notEqual(root.get("status"), FlightStatus.CANCELED)
                );
    }

    public static Specification<FlightEntity> byDepartureDate(LocalDate date) {
        return (root, query, builder) -> {
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);

            return builder.between(root.get("departureTime"), startOfDay, endOfDay);
        };
    }
}
