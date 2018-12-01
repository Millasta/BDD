-- Ajout d'un utilisateur
CREATE OR REPLACE PROCEDURE ProcAjoutUtilisateur(
		pNom			Utilisateurs.Nom%TYPE,
		pPrenom			Utilisateurs.Prenom%TYPE,
		pCourriel		Utilisateurs.Courriel%TYPE,
		pTelephone		Utilisateurs.Telephone%TYPE,
		pNaissance		Utilisateurs.Naissance%TYPE,
		pMotDePasse		Utilisateurs.MotDePasse%TYPE,
		pAdresse		Utilisateurs.Adresse%TYPE) IS
BEGIN
	INSERT INTO Utilisateurs(Nom, Prenom, Courriel, Telephone, Naissance, MotDePasse, Adresse)
	VALUES (pNom, pPrenom, pCourriel, pTelephone, pNaissance, pMotDePasse, pAdresse);
END ProcAjoutUtilisateur;
/

-- Ajout d'un client
CREATE OR REPLACE PROCEDURE ProcAjoutClient(
		pNom			Utilisateurs.Nom%TYPE,
		pPrenom			Utilisateurs.Prenom%TYPE,
		pCourriel		Utilisateurs.Courriel%TYPE,
		pTelephone		Utilisateurs.Telephone%TYPE,
		pNaissance		Utilisateurs.Naissance%TYPE,
		pMotDePasse		Utilisateurs.MotDePasse%TYPE,
		pAdresse		Utilisateurs.Adresse%TYPE,
		pTypeCarte		Clients.TypeCarte%TYPE,
		pNumeroCarte 	Clients.NumeroCarte%TYPE,
		pDateExpiration	Clients.DateExpiration%TYPE,
		pTypeForfait	Clients.TypeForfait%TYPE) IS
BEGIN
	ProcAjoutUtilisateur(pNom, pPrenom, pCourriel, pTelephone, pNaissance, pMotDePasse, pAdresse);
	
	INSERT INTO Clients(Nom, Prenom, TypeCarte, NumeroCarte, DateExpiration, TypeForfait)
	VALUES (pNom, pPrenom, pTypeCarte, pNumeroCarte, pDateExpiration, pTypeForfait);
		
END ProcAjoutClient;
/

-- Ajout d'un employé
CREATE OR REPLACE PROCEDURE ProcAjoutEmploye(
		pMatricule		Employes.Matricule%TYPE,
		pNom			Utilisateurs.Nom%TYPE,
		pPrenom			Utilisateurs.Prenom%TYPE,
		pCourriel		Utilisateurs.Courriel%TYPE,
		pTelephone		Utilisateurs.Telephone%TYPE,
		pNaissance		Utilisateurs.Naissance%TYPE,
		pMotDePasse		Utilisateurs.MotDePasse%TYPE,
		pAdresse		Utilisateurs.Adresse%TYPE) IS
BEGIN
	ProcAjoutUtilisateur(pNom, pPrenom, pCourriel, pTelephone, pNaissance, pMotDePasse, pAdresse);
	
	INSERT INTO Employes(Matricule, Nom, Prenom)
	VALUES (pMatricule, pNom, pPrenom);
		
END ProcAjoutEmploye;
/

-- Ajout d'une personne
CREATE OR REPLACE PROCEDURE ProcAjoutPersonne(
		pNom			Personnes.Nom%TYPE,
		pPrenom			Personnes.Prenom%TYPE,
		pNaissance		Personnes.Naissance%TYPE,
		pLieuNaissance	Personnes.LieuNaissance%TYPE,
		pBiographie		Personnes.Biographie%TYPE) IS
BEGIN
	INSERT INTO Personnes(Nom, Prenom, Naissance, LieuNaissance, Biographie)
	VALUES (pNom, pPrenom, pNaissance, pLieuNaissance, pBiographie);	
END ProcAjoutPersonne;
/

-- Ajout d'un scenariste
CREATE OR REPLACE PROCEDURE ProcAjoutScenariste(
		pNom			Scenaristes.Nom%TYPE,
		pPrenom			Scenaristes.Prenom%TYPE,
		pTitreFilm		Scenaristes.TitreFilm%TYPE) IS
		
		personneExiste INTEGER;
		filmExiste INTEGER;
BEGIN
	SELECT COUNT(*)
	INTO personneExiste
	FROM Personnes
	WHERE pNom = Nom AND pPrenom = Prenom;
	
	IF personneExiste = 0 THEN
		raise_application_error(-20105, 'cette personne n existe pas dans la base');
	END IF;
	
	SELECT COUNT(*)
	INTO filmExiste
	FROM Films
	WHERE pTitreFilm = Titre;
	
	IF filmExiste = 0 THEN
		raise_application_error(-20106, 'ce film n existe pas dans la base');
	END IF;

	
	IF filmExiste > 0 AND personneExiste > 0 THEN
		INSERT INTO Scenaristes(Nom, Prenom, TitreFilm)
		VALUES (pNom, pPrenom, pTitreFilm);
	END IF;
END ProcAjoutScenariste;
/

-- Ajout d'un acteur pour un film
CREATE OR REPLACE PROCEDURE ProcAjoutActeurFilm(
		pNom			RolesActeurs.Nom%TYPE,
		pPrenom			RolesActeurs.Prenom%TYPE,
		pTitreFilm		RolesActeurs.TitreFilm%TYPE,
		pNomPersonnage	RolesActeurs.NomPersonnage%TYPE) IS
		
		personneExiste INTEGER;
		filmExiste INTEGER;
BEGIN
	SELECT COUNT(*)
	INTO personneExiste
	FROM Personnes
	WHERE pNom = Nom AND pPrenom = Prenom;
	
	IF personneExiste = 0 THEN
		raise_application_error(-20107, 'cette personne n existe pas dans la base');
	END IF;
	
	SELECT COUNT(*)
	INTO filmExiste
	FROM Films
	WHERE pTitreFilm = Titre;
	
	IF filmExiste = 0 THEN
		raise_application_error(-20108, 'ce film n existe pas dans la base');
	END IF;

	
	IF filmExiste > 0 AND personneExiste > 0 THEN
		INSERT INTO RolesActeurs(Nom, Prenom, TitreFilm, NomPersonnage)
		VALUES (pNom, pPrenom, pTitreFilm, pNomPersonnage);
	END IF;
END ProcAjoutActeurFilm;
/

-- Ajout d'une copie
CREATE OR REPLACE PROCEDURE ProcAjoutCopie(
	pTitre	Copies.TitreFilm%TYPE) IS
	
	noCopie Copies.NumeroCopie%TYPE;
BEGIN
	SELECT seqCopies.nextVal 
	INTO noCopie
	FROM dual;
	
	INSERT INTO Copies(NumeroCopie, TitreFilm)
	Values (noCopie, pTitre);
END ProcAjoutCopie;
/

-- Ajout d'un film, ajoute automatiquement une copie
CREATE OR REPLACE PROCEDURE ProcAjoutFilm(
	pTitre			Films.Titre%TYPE,
	pAnnee			Films.Annee%TYPE,
	pDuree			Films.Duree%TYPE,
	pLangue			Films.Langue%TYPE,
	pResume			Films.Resume%TYPE,
	pGenres			Films.Genres%TYPE,
	pPaysProduction	Films.PaysProduction%TYPE) IS
BEGIN
	INSERT INTO Films(Titre, Annee, Duree, Langue, Resume, Genres, PaysProduction)
	VALUES (pTitre, pAnnee, pDuree, pLangue, pResume, pGenres, pPaysProduction);
	
	ProcAjoutCopie(pTitre);
END ProcAjoutFilm;
/
	
-- Ajout d'un forfait
CREATE OR REPLACE PROCEDURE ProcAjoutForfait(
	pTypeForfait	Forfaits.TypeForfait%TYPE,
	pCout			Forfaits.Cout%TYPE,
	pLocationMax	Forfaits.LocationMax%TYPE,
	pDureeMax		Forfaits.DureeMax%TYPE) IS
BEGIN
	INSERT INTO Forfaits(TypeForfait, Cout, LocationMax, DureeMax)
	Values (pTypeForfait, pCout, pLocationMax, pDureeMax);
END ProcAjoutForfait;
/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
