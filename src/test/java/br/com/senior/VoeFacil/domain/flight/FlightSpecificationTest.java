package br.com.senior.VoeFacil.domain.flight;

import jakarta.persistence.criteria.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.data.jpa.domain.Specification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FlightSpecificationTest {
    @InjectMocks
    private FlightSpecification flightSpecification;

    @Nested
    class byDepartureAirportTest {
        @Test
        @DisplayName("OK - Deve criar especificação para filtro por aeroporto de partida")
        void shouldCreateSpecificationForDepartureAirport() {
            // Arrange
            String city = "São Paulo";
            CriteriaBuilder builder = mock(CriteriaBuilder.class);
            CriteriaQuery<?> query = mock(CriteriaQuery.class);
            Root<FlightEntity> root = mock(Root.class);

            Path departureAirportPath = mock(Path.class);
            when(root.get("departureAirport")).thenReturn(departureAirportPath);
            when(departureAirportPath.get("city")).thenReturn(mock(Path.class));

            // Act
            Specification<FlightEntity> specification = FlightSpecification.byDepartureAirport(city);
            Predicate predicate = specification.toPredicate(root, query, builder);

            // Assert
            assertNotNull(specification);
            assertNotNull(predicate);
            assertThat(removeAccents(city)).isEqualTo("sao paulo");

            assertThat(predicate).isEqualTo(builder.equal(
                    builder.function("unaccent", String.class, builder.lower(departureAirportPath.get("city"))),
                    removeAccents(city.toLowerCase())));
        }

        private static String removeAccents(String input) {
            return StringUtils.stripAccents(input);
        }
    }
}