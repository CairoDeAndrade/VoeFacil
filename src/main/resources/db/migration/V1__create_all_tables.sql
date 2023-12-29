CREATE TABLE "aircraft" (
  "id" UUID PRIMARY KEY,
  "airline" VARCHAR(255) NOT NULL,
  "capacity" INT NOT NULL
);

CREATE TABLE "seat" (
  "id" UUID PRIMARY KEY,
  "seat_number" varchar(9) NOT NULL,
  "seat_class" VARCHAR(55) NOT NULL,
  "aircraft_id" UUID
);

CREATE TABLE "airport" (
  "id" UUID PRIMARY KEY,
  "name" VARCHAR(255) NOT NULL,
  "code" VARCHAR(10) NOT NULL,
  "country" VARCHAR(55) NOT NULL,
  "city" VARCHAR(55) NOT NULL
);

CREATE TABLE "flight" (
  "id" UUID PRIMARY KEY,
  "number" VARCHAR(55) NOT NULL,
  "base_price" NUMERIC(18,2),
  "available_seats_amount" INT NOT NULL,
  "departure_time" TIMESTAMP,
  "duration_minutes" INT NOT NULL,
  "status" VARCHAR(55) NOT NULL,
  "delayed" BOOLEAN NOT NULL,
  "deal" BOOLEAN NOT NULL,
  "departure_airport_id" UUID,
  "arrival_airport_id" UUID
);

CREATE TABLE "flight_has_seat" (
  "seat_id" UUID,
  "flight_id" UUID
);

CREATE TABLE "passenger" (
  "id" UUID PRIMARY KEY,
  "name" VARCHAR(255) NOT NULL,
  "email" VARCHAR(255) NOT NULL,
  "phone" VARCHAR(13) NOT NULL,
  "birth_date" TIMESTAMP
);

CREATE TABLE "flight_ticket" (
  "id" UUID PRIMARY KEY,
  "total_price" NUMERIC(18,2),
  "ticket_number" VARCHAR(55) NOT NULL,
  "reservation_date" TIMESTAMP,
  "canceled" BOOLEAN NOT NULL,
  "flight_id" UUID,
  "seat_id" UUID,
  "passenger_id" UUID
);

ALTER TABLE "seat" ADD FOREIGN KEY ("aircraft_id") REFERENCES "aircraft" ("id");

ALTER TABLE "flight_has_seat" ADD FOREIGN KEY ("flight_id") REFERENCES "flight" ("id");

ALTER TABLE "flight_has_seat" ADD FOREIGN KEY ("seat_id") REFERENCES "seat" ("id");

ALTER TABLE "flight" ADD FOREIGN KEY ("departure_airport_id") REFERENCES "airport" ("id");

ALTER TABLE "flight" ADD FOREIGN KEY ("arrival_airport_id") REFERENCES "airport" ("id");

ALTER TABLE "flight_ticket" ADD FOREIGN KEY ("flight_id") REFERENCES "flight" ("id");

ALTER TABLE "flight_ticket" ADD FOREIGN KEY ("seat_id") REFERENCES "seat" ("id");

ALTER TABLE "flight_ticket" ADD FOREIGN KEY ("passenger_id") REFERENCES "passenger" ("id");
