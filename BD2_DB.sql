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
	CREATE TABLE IF NOT EXISTS kolory (
		id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
		nazwa varchar(255) NOT NULL UNIQUE
	);

	CREATE TABLE IF NOT EXISTS marki (
		id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
		nazwa varchar(255) NOT NULL UNIQUE
	);

	CREATE TABLE IF NOT EXISTS modele (
		id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
		nazwa varchar(255) NOT NULL UNIQUE,
		marki_id integer NOT NULL REFERENCES marki ON DELETE CASCADE --fk
	);

	CREATE TABLE IF NOT EXISTS producenci (
		id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
		nazwa varchar(255) NOT NULL UNIQUE
	);

	CREATE TABLE IF NOT EXISTS typy (
		id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
		nazwa varchar(255) NOT NULL UNIQUE
	);

	CREATE TABLE IF NOT EXISTS stanowiska (
		id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
		nazwa varchar(255) NOT NULL UNIQUE
	);


	--base tables
	CREATE TABLE IF NOT EXISTS adres (
		id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
		miasto varchar(255) NOT NULL,
		ulica varchar(255) NOT NULL,
		numer_budynku varchar(6) NOT NULL,
		numer_mieszkania integer CHECK (numer_mieszkania > 0), 
		kod_pocztowy varchar(6) NOT NULL 
	);

	CREATE TABLE IF NOT EXISTS klienci (
		id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
		imie varchar(255) NOT NULL,
		nazwisko varchar(255) NOT NULL,
		adres_id integer REFERENCES adres ON DELETE SET NULL --fk
	);

	CREATE TABLE IF NOT EXISTS samochody (
		VIN varchar(255) PRIMARY KEY,
		rok_produkcji integer NOT NULL check(rok_produkcji > 1950),
		klienci_id integer NOT NULL REFERENCES klienci ON DELETE CASCADE, --fk
		modele_id integer NOT NULL REFERENCES modele, --fk
		kolory_id integer NOT NULL REFERENCES  kolory --fk
	);

	CREATE TABLE IF NOT EXISTS pracownicy (
		id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
		imie varchar(255) NOT NULL,
		nazwisko varchar(255) NOT NULL,
		login varchar(255) GENERATED ALWAYS AS (SUBSTRING(imie, 1, 1) || nazwisko) STORED,
		stanowiska_id integer NOT NULL REFERENCES stanowiska --fk
	);

	CREATE TABLE IF NOT EXISTS naprawy (
		id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
		data_rozpoczecia date NOT NULL,
		data_zakonczenia date,
		koszt float,
		samochody_VIN varchar(255) REFERENCES samochody ON DELETE SET NULL, --fk
		pracownicy_id integer REFERENCES pracownicy ON DELETE SET NULL --fk
	);

	CREATE TABLE IF NOT EXISTS czesci (
		id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
		numer_seryjny varchar(255) NOT NULL UNIQUE,
		koszt float NOT NULL,
		modele_id integer NOT NULL REFERENCES modele,
		producenci_id integer NOT NULL REFERENCES producenci, --fk
		typy_id integer NOT NULL REFERENCES typy --fk
	);

	CREATE TABLE IF NOT EXISTS zamowienia (
		id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
		ilosc integer NOT NULL, 
		cena float NOT NULL,
		data date NOT NULL,
		pracownicy_id integer REFERENCES pracownicy ON DELETE SET NULL , --fk
		czesci_id integer NOT NULL REFERENCES czesci ON DELETE CASCADE -- fk
	);

	CREATE TABLE IF NOT EXISTS szablony_napraw (
		id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
		nazwa varchar(255) NOT NULL UNIQUE,
		koszt float NOT NULL
	);


	--non primary key tables
	CREATE TABLE IF NOT EXISTS stan_magazynowy (
		ilosc integer NOT NULL CHECK (ilosc > 0),
		czesci_id integer NOT NULL REFERENCES czesci ON DELETE CASCADE --fk
	);

	CREATE TABLE IF NOT EXISTS uzyte_czesci (
		ilosc integer NOT NULL CHECk(ilosc > 0),
		naprawy_id integer NOT NULL REFERENCES naprawy ON DELETE CASCADE, --fk
		czesci_id integer REFERENCES czesci ON DELETE SET NULL --fk
	);

	CREATE TABLE IF NOT EXISTS wymagane_czesci (
		ilosc integer NOT NULL CHECK(ilosc > 0),
		szablony_napraw_id integer NOT NULL REFERENCES szablony_napraw ON DELETE CASCADE, --fk
		czesci_id integer NOT NULL REFERENCES czesci ON DELETE SET NULL--fk
	);

	--indexes
	CREATE INDEX ON kolory(id);
	CREATE INDEX ON producenci(id);
	CREATE INDEX ON typy(id);
	CREATE INDEX ON marki(id);
	CREATE INDEX ON modele(id);
	CREATE INDEX ON stanowiska(id);

	CREATE INDEX ON adres(id);
	CREATE INDEX ON klienci(id);
	CREATE INDEX ON samochody(VIN);
	CREATE INDEX ON naprawy(id);
	CREATE INDEX ON pracownicy(id);
	CREATE INDEX ON zamowienia(id);
	CREATE INDEX ON czesci(id);
	CREATE INDEX ON szablony_napraw(id);

	CREATE ROLE "mechanik" WITH
		LOGIN
		NOSUPERUSER
		NOCREATEDB
		NOCREATEROLE
		INHERIT
		NOREPLICATION
		CONNECTION LIMIT -1
		PASSWORD 'db2mechanikpassword'; --change to encrypted one

	CREATE ROLE "magazynier" WITH
		LOGIN
		NOSUPERUSER
		NOCREATEDB
		NOCREATEROLE
		INHERIT
		NOREPLICATION
		CONNECTION LIMIT -1
		PASSWORD 'db2magazynierpassword'; --change to encrypted one


	--magazynier privileges
	GRANT SELECT ON czesci TO magazynier;
	GRANT SELECT, DELETE ON zamowienia TO magazynier;
	GRANT UPDATE ON stan_magazynowy TO magazynier;


	--mechanik privileges
	GRANT SELECT, UPDATE, INSERT ON naprawy, klienci, adres TO mechanik;
	GRANT SELECT, INSERT ON samochody TO mechanik;
	GRANT INSERT ON zamowienia TO mechanik;
	GRANT SELECT ON szablony_napraw TO mechanik;