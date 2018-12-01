DROP TABLE Scenaristes;
DROP TABLE RolesActeurs;
DROP TABLE Copies;
DROP SEQUENCE seqCopies;
DROP TABLE Clients;
DROP TABLE Employes;
DROP TABLE Forfaits;
DROP TABLE Films;
DROP TABLE Personnes;
DROP TABLE Utilisateurs;

CREATE TABLE Utilisateurs
	(PRIMARY KEY(Nom,Prenom),
	Nom				VARCHAR2(30)	NOT NULL,
	Prenom			VARCHAR2(30)	NOT NULL,
	Courriel		VARCHAR2(50)	UNIQUE NOT NULL,
	Telephone		VARCHAR2(16)	NOT NULL,
	Naissance		DATE			NOT NULL,
	MotDePasse		VARCHAR2(20)	NOT NULL
		CHECK(LENGTH(MotDePasse) >= 5),
	Adresse			VARCHAR2(150)	NOT NULL
	);
	
CREATE TABLE Personnes
	(PRIMARY KEY(Nom,Prenom),
	Nom				VARCHAR2(30)	NOT NULL,
	Prenom			VARCHAR2(30)	NOT NULL,
	Naissance		DATE			NOT	NULL,
	LieuNaissance	VARCHAR2(20)	NOT NULL,
	Biographie		VARCHAR2(1000)	NOT NULL
	);
	
CREATE TABLE Films
	(PRIMARY KEY(Titre),
	Titre			VARCHAR2(100)	NOT NULL,
	Annee			NUMBER(6)		NOT NULL,
	Duree			NUMBER(4)		NOT NULL,
	Langue			VARCHAR2(20)	NOT NULL,
	Resume			VARCHAR2(1000)	NOT NULL,
	Genres			VARCHAR2(200)	NOT NULL,
	PaysProduction	VARCHAR2(200)	NOT NULL
	);

CREATE TABLE Forfaits
	(PRIMARY KEY(TypeForfait),
	TypeForfait		VARCHAR2(20)	NOT NULL,
	Cout			REAL			NOT NULL,
	LocationMax		NUMBER(2)		NOT NULL,
	DureeMax		NUMBER(4)		NOT NULL
	);
	
CREATE TABLE Employes
	(PRIMARY KEY(Matricule),
	Matricule		NUMBER(8)		NOT NULL,
	Nom				VARCHAR2(30)	NOT NULL,
	Prenom			VARCHAR2(30)	NOT NULL,
	FOREIGN KEY(Nom,Prenom) REFERENCES Utilisateurs(Nom,Prenom)
	);

CREATE TABLE Clients
	(PRIMARY KEY(Nom,Prenom),
	Nom				VARCHAR2(30)	NOT NULL,
	Prenom			VARCHAR2(30)	NOT NULL,
	TypeCarte		VARCHAR2(16)	NOT NULL
		CHECK (TypeCarte IN('Visa','MasterCard','Amex')),
	NumeroCarte		NUMBER(16)		NOT NULL, 
	DateExpiration	DATE			NOT NULL,
	TypeForfait		VARCHAR2(20)	NOT NULL,
	FOREIGN KEY(Nom,Prenom) REFERENCES Utilisateurs(Nom,Prenom),
	FOREIGN KEY(TypeForfait) REFERENCES Forfaits(TypeForfait)
	);
	
CREATE SEQUENCE seqCopies;
	
CREATE TABLE Copies
	(PRIMARY KEY(NumeroCopie),
	NumeroCopie		NUMBER(6)		NOT NULL,
	NomClient		VARCHAR2(30),
	PrenomClient	VARCHAR2(30),
	TitreFilm		VARCHAR2(100)	NOT NULL,
	DateLocation	DATE,
	FOREIGN KEY(NomClient,PrenomClient) REFERENCES Clients(Nom,Prenom),
	FOREIGN KEY(TitreFilm) REFERENCES Films(Titre)
	);

CREATE TABLE RolesActeurs
	(PRIMARY KEY(Nom,Prenom,TitreFilm),
	Nom				VARCHAR2(30)	NOT NULL,
	Prenom			VARCHAR2(30)	NOT NULL,
	TitreFilm		VARCHAR2(100)	NOT NULL,
	NomPersonnage	VARCHAR2(40)	NOT NULL,
	FOREIGN KEY(Nom,Prenom) REFERENCES Personnes(Nom,Prenom),
	FOREIGN KEY(TitreFilm) REFERENCES Films(Titre)
	);

CREATE TABLE Scenaristes
	(PRIMARY KEY(Nom,Prenom,TitreFilm),
	Nom				VARCHAR2(30)	NOT NULL,
	Prenom			VARCHAR2(30)	NOT NULL,
	TitreFilm		VARCHAR2(100)	NOT NULL,
	FOREIGN KEY(Nom,Prenom) REFERENCES Personnes(Nom,Prenom),
	FOREIGN KEY(TitreFilm) REFERENCES Films(Titre)
	);
 
 
 
 
 
 