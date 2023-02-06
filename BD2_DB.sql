	DROP TABLE IF EXISTS kolory CASCADE;
	DROP TABLE IF EXISTS modele CASCADE;
	DROP TABLE IF EXISTS producenci CASCADE;
	DROP TABLE IF EXISTS typy CASCADE;
	DROP TABLE IF EXISTS marki CASCADE;
	DROP TABLE IF EXISTS stanowiska CASCADE;
	DROP TABLE IF EXISTS adres CASCADE;
	DROP TABLE IF EXISTS klienci CASCADE;
	DROP TABLE IF EXISTS samochody CASCADE;
	DROP TABLE IF EXISTS pracownicy CASCADE;
	DROP TABLE IF EXISTS zamowienia CASCADE;
	DROP TABLE IF EXISTS czesci CASCADE;
	DROP TABLE IF EXISTS naprawy CASCADE;
	DROP TABLE IF EXISTS szablony_napraw CASCADE;
	DROP TABLE IF EXISTS stan_magazynowy CASCADE;
	DROP TABLE IF EXISTS uzyte_czesci CASCADE;
	DROP TABLE IF EXISTS wymagane_czesci CASCADE;

	DROP ROLE IF EXISTS application;
	DROP ROLE IF EXISTS mechanik;
	DROP ROLE IF EXISTS magazynier;

	--enum tables
	CREATE TABLE IF NOT EXISTS colors (
		id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
		name varchar(255) NOT NULL UNIQUE
	);

	CREATE TABLE IF NOT EXISTS car_brands (
		id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
		name varchar(255) NOT NULL UNIQUE
	);

	CREATE TABLE IF NOT EXISTS car_models (
		id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
		name varchar(255) NOT NULL UNIQUE,
		brand_id integer NOT NULL REFERENCES car_brands ON DELETE CASCADE --fk
	);

	CREATE TABLE IF NOT EXISTS part_manufacturers (
		id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
		name varchar(255) NOT NULL UNIQUE
	);

	CREATE TABLE IF NOT EXISTS part_types (
		id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
		name varchar(255) NOT NULL UNIQUE
	);

	CREATE TABLE IF NOT EXISTS employee_positions (
		id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
		name varchar(255) NOT NULL UNIQUE
	);


	--base tables
	CREATE TABLE IF NOT EXISTS client_addresses (
		id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
		city varchar(255) NOT NULL,
		street varchar(255) NOT NULL,
		building varchar(6) NOT NULL,
		apartment integer CHECK (apartment > 0),
		postal_code varchar(6) NOT NULL
	);

	CREATE TABLE IF NOT EXISTS clients (
		id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
		name varchar(255) NOT NULL,
		surname varchar(255) NOT NULL,
		address_id integer REFERENCES client_addresses ON DELETE SET NULL --fk
	);
	CREATE TABLE IF NOT EXISTS cars (
		vin varchar(255) PRIMARY KEY,
		production_year integer NOT NULL check(production_year > 1950),
		client_id integer NOT NULL REFERENCES clients ON DELETE CASCADE, --fk
		model_id integer NOT NULL REFERENCES car_models, --fk
		color_id integer NOT NULL REFERENCES  colors --fk
	);

	CREATE TABLE IF NOT EXISTS employees (
		id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
		name varchar(255) NOT NULL,
		surname varchar(255) NOT NULL,
		login varchar(255) GENERATED ALWAYS AS (SUBSTRING(name, 1, 1) || surname) STORED,
		position_id integer NOT NULL REFERENCES employee_positions --fk
	);

	CREATE TABLE IF NOT EXISTS repairs (
		id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
		start_date date NOT NULL,
		end_date date,
		cost float,
		car_vin varchar(255) REFERENCES cars ON DELETE SET NULL, --fk
		employee_id integer REFERENCES employees ON DELETE SET NULL --fk
	);

	CREATE TABLE IF NOT EXISTS parts (
		id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
		serial_number varchar(255) NOT NULL UNIQUE,
		price float NOT NULL,
		model_id integer NOT NULL REFERENCES car_models,
		manufacturer_id integer NOT NULL REFERENCES part_manufacturers, --fk
		type_id integer NOT NULL REFERENCES part_types --fk
	);

	CREATE TABLE IF NOT EXISTS part_orders (
		id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
		quantity integer NOT NULL,
		cost float NOT NULL,
		date date NOT NULL,
		employee_id integer REFERENCES employees ON DELETE SET NULL , --fk
		part_id integer NOT NULL REFERENCES parts ON DELETE CASCADE -- fk
	);

	CREATE TABLE IF NOT EXISTS repair_templates (
		id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
		name varchar(255) NOT NULL UNIQUE
	);

	--non primary key tables
	CREATE TABLE IF NOT EXISTS part_stock (
		stock integer NOT NULL CHECK (stock > 0),
		part_id integer NOT NULL REFERENCES parts ON DELETE CASCADE --fk
	);

	CREATE TABLE IF NOT EXISTS used_parts (
		quantity integer NOT NULL CHECk(quantity > 0),
		repair_id integer NOT NULL REFERENCES repairs ON DELETE CASCADE, --fk
		part_id integer REFERENCES parts ON DELETE SET NULL --fk
	);

	CREATE TABLE IF NOT EXISTS required_parts (
        quantity integer NOT NULL CHECK(quantity > 0),
		template_id integer NOT NULL REFERENCES repair_templates ON DELETE CASCADE, --fk
		part_id integer NOT NULL REFERENCES parts ON DELETE SET NULL--fk
	);

	--indexes
	CREATE INDEX ON colors(id);
	CREATE INDEX ON part_manufacturers(id);
	CREATE INDEX ON part_types(id);
	CREATE INDEX ON car_brands(id);
	CREATE INDEX ON car_models(id);
	CREATE INDEX ON employee_positions(id);

	CREATE INDEX ON client_addresses(id);
	CREATE INDEX ON clients(id);
	CREATE INDEX ON cars(VIN);
	CREATE INDEX ON repairs(id);
	CREATE INDEX ON employees(id);
	CREATE INDEX ON part_orders(id);
	CREATE INDEX ON parts(id);
	CREATE INDEX ON repair_templates(id);

	CREATE ROLE "mechanic" WITH
		LOGIN
		NOSUPERUSER
		NOCREATEDB
		NOCREATEROLE
		INHERIT
		NOREPLICATION
		CONNECTION LIMIT -1
		PASSWORD 'm-password'; --change to encrypted one

	CREATE ROLE "warehouseman" WITH
		LOGIN
		NOSUPERUSER
		NOCREATEDB
		NOCREATEROLE
		INHERIT
		NOREPLICATION
		CONNECTION LIMIT -1
		PASSWORD 'w-password'; --change to encrypted one


	--warehouseman privileges TODO
	--mechanik privileges TODO