--	Vérification de l'age d'un utilisateur via comparaison de la date actuelle et la date de naissance
CREATE OR REPLACE TRIGGER VerifierAgeUtilisateur
BEFORE INSERT ON Utilisateurs
REFERENCING
	NEW AS ligneApres
FOR EACH ROW
DECLARE
	age REAL;
BEGIN
	SELECT (sysdate - :ligneApres.Naissance)/365.25 
	INTO age 
	FROM dual;
	
	IF age < 18 THEN
		raise_application_error(-20100, 'Le client doit avoir au moins 18 ans');
	END IF;
END;
/

-- Vérification de la date d'expiration de la carte du client via comparaison avec date actuelle
CREATE OR REPLACE TRIGGER VerifierDateExpirationClient
BEFORE INSERT ON Clients
REFERENCING
	NEW AS ligneApres
FOR EACH ROW
DECLARE
	dateActuelle DATE;
BEGIN
	SELECT sysdate
	INTO dateActuelle
	FROM dual;

	IF :ligneApres.DateExpiration < dateActuelle THEN
		raise_application_error(-20101, 'la date d expiration de la carte est depassee');
	END IF;
END;
/

-- On ne peut supprimer un film de la base de donnée que si toutes ses copies sont retournées
CREATE OR REPLACE TRIGGER SuppressionFilm
BEFORE DELETE ON Films
REFERENCING
	OLD AS ligneAvant
FOR EACH ROW
DECLARE
	nbCopiesNonRetournees INTEGER;
BEGIN
	SELECT COUNT(*)
	INTO nbCopiesNonRetournees
	FROM Copies
	WHERE TitreFilm = :ligneAvant.Titre AND NomClient = NULL AND PrenomClient = NULL;
	
	IF nbCopiesNonRetournees > 0 THEN
		raise_application_error(-20102, 'impossible de supprimer ce film, il reste des copies en circulation');
	END IF;
END;
/

-- On ne peut supprimer un client que s'il a retourné toutes ses copies empruntées
CREATE OR REPLACE TRIGGER SuppressionClient
BEFORE DELETE ON Clients
REFERENCING
	OLD AS ligneAvant
FOR EACH ROW
DECLARE
	nbCopiesNonRetournees INTEGER;
BEGIN
	SELECT COUNT(*)
	INTO nbCopiesNonRetournees
	FROM Copies
	WHERE NomClient = :ligneAvant.Nom AND PrenomClient = :ligneAvant.Prenom;
	
	IF nbCopiesNonRetournees > 0 THEN
		raise_application_error(-20103, 'impossible de supprimer ce client, il n a pas retourné toutes ses copies empruntées');
	END IF;
END;
/
















