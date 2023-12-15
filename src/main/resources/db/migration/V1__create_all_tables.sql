CREATE TABLE aircraft (
    id UUID PRIMARY KEY,
    airline VARCHAR(255) NOT NULL,
    capacity INT NOT NULL
);

CREATE TABLE seat (
    id UUID PRIMARY KEY,
    seat_number varchar(9) NOT NULL,
    seat_class VARCHAR(55) NOT NULL,
    aircraft_id UUID REFERENCES aircraft(id)
);

CREATE TABLE airport (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(10) NOT NULL,
    country VARCHAR(55) NOT NULL,
    city VARCHAR(55) NOT NULL
);

CREATE TABLE flight (
    id UUID PRIMARY KEY,
    number VARCHAR(55) NOT NULL,
    base_price NUMERIC(18, 2),
    available_seats_amount INT NOT NULL,
    departure_time TIMESTAMP,
    duration_minutes INT NOT NULL,
    status VARCHAR(55) NOT NULL,
    delayed BOOLEAN NOT NULL,
    departure_airport_id UUID REFERENCES airport(id),
    arrival_airport_id UUID REFERENCES airport(id),
    aircraft_id UUID REFERENCES aircraft(id)
);

CREATE TABLE passenger (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(13) NOT NULL,
    birth_date TIMESTAMP
);

CREATE TABLE flight_ticket (
    id UUID PRIMARY KEY,
    total_price NUMERIC(18, 2),
    ticket_number VARCHAR(55) NOT NULL,
    reservation_date TIMESTAMP,
    canceled BOOLEAN NOT NULL,
    flight_id UUID REFERENCES flight(id),
    seat_id UUID REFERENCES seat(id),
    passenger_id UUID REFERENCES passenger(id)
);