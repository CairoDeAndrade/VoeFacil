-- Rename the "name" column to "firstname" and "lastname"
ALTER TABLE "passenger"
RENAME COLUMN "name" TO "first_name";

ALTER TABLE "passenger"
ADD COLUMN "last_name" VARCHAR(255) NOT NULL;

ALTER TABLE "passenger"
ALTER COLUMN "phone" TYPE VARCHAR(20);

ALTER TABLE "passenger"
DROP COLUMN "birth_date";