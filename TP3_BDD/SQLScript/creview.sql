-- Vue permettant d'afficher toutes les informations relatives aux clients
--	Nom | Prenom | Courriel | Téléphone | Date de naissance | Adresse | Nombre de copies en cours de location
CREATE OR REPLACE VIEW InformationsClients AS
	SELECT u.Nom, u.Prenom, Courriel, Telephone, Naissance, Adresse, NbCopieLouees
	FROM Utilisateurs u
	LEFT JOIN	(SELECT Nom, Prenom, TypeForfait, TypeCarte, COALESCE(NB, 0) AS NbCopieLouees
				FROM Clients c
				LEFT JOIN (	SELECT NomClient, PrenomClient, COUNT(*) AS NB
							FROM Copies
							GROUP BY (NomClient, PrenomClient)) n
				ON c.Nom = n.NomClient AND c.Prenom = n.PrenomClient) nc
	ON u.Prenom = nc.Prenom AND u.Nom = nc.Nom
/
	