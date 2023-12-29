package br.com.senior.VoeFacil.domain.flight;

import br.com.senior.VoeFacil.domain.flightseat.FlightSeatEntity;
import br.com.senior.VoeFacil.domain.seat.SeatEntity;
import br.com.senior.VoeFacil.domain.seat.SeatTypeEnum;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class FlightSpecification {

    public static Specification<FlightEntity> byDepartureAirport(String city) {
        return (root, query, builder) ->
                builder.and(
                        builder.equal(builder.lower(root.get("departureAirport").get("city")), city.toLowerCase())
                );
    }

    public static Specification<FlightEntity> byArrivalAirport(String city) {
        return (root, query, builder) ->
                builder.and(
                        builder.equal(builder.lower(root.get("arrivalAirport").get("city")), city.toLowerCase())
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

    public static Specification<FlightEntity> bySeatType(SeatTypeEnum seatType) {
        return (root, query, builder) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<FlightEntity> subRoot = subquery.from(FlightEntity.class);

            Join<FlightEntity, FlightSeatEntity> flightSeatsjoin = subRoot.join("flightSeats", JoinType.INNER);
            Join<FlightSeatEntity, SeatEntity> seatJoin = flightSeatsjoin.join("seat", JoinType.INNER);

            subquery.select(builder.literal(1L));
            subquery.where(
                    builder.and(
                            builder.equal(subRoot, root),
                            builder.equal(flightSeatsjoin.get("seatAvailability"), true),
                            builder.equal(seatJoin.get("seatClass"), seatType)
                    )
            );

            return builder.exists(subquery);
        };
    }

    public static Specification<FlightEntity> byStatus(FlightStatus status) {
        return (root, query, builder) ->
                builder.and(
                        builder.equal(root.get("status"), status)
                );
    }
}
