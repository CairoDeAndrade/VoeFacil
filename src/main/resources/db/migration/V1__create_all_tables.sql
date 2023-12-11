CREATE TABLE aircraft (
    id SERIAL PRIMARY KEY,
    airline VARCHAR(255) NOT NULL,
    capacity INT NOT NULL
);

CREATE TABLE seat (
    id SERIAL PRIMARY KEY,
    seat_number varchar(9) NOT NULL,
    seat_class VARCHAR(55) NOT NULL,
    aircraft_id INT REFERENCES aircraft(id)
);

CREATE TABLE airport (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    cep VARCHAR(9) NOT NULL,
    country VARCHAR(55) NOT NULL,
    city VARCHAR(55) NOT NULL,
    neighborhood VARCHAR(55) NOT NULL,
    street VARCHAR(255) NOT NULL,
    street_number INT NOT NULL
);

CREATE TABLE flight (
    id SERIAL PRIMARY KEY,
    number VARCHAR(55) NOT NULL,
    base_price NUMERIC(18, 2),
    available_seats_amount INT NOT NULL,
    departure_time TIMESTAMP,
    duration_minutes INT NOT NULL,
    status VARCHAR(55) NOT NULL,
    departure_airport_id INT REFERENCES airport(id),
    arrival_airport_id INT REFERENCES airport(id),
    aircraft_id INT REFERENCES aircraft(id)
);

CREATE TABLE passenger (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(13) NOT NULL,
    birth_date TIMESTAMP
);

CREATE TABLE flight_ticket (
    id SERIAL PRIMARY KEY,
    total_price NUMERIC(18, 2),
    ticket_number VARCHAR(55) NOT NULL,
    reservation_date TIMESTAMP,
    canceled BOOLEAN NOT NULL,
    flight_id INT REFERENCES flight(id),
    seat_id INT REFERENCES seat(id),
    passenger_id INT REFERENCES passenger(id)
);