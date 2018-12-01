EXECUTE ProcAjoutPersonne('Verbinski', 'Gore', DATE '1964-03-16', 'Oak Ridge', 'Bio');

EXECUTE ProcAjoutPersonne('Knightley', 'Keira', DATE '1985-03-26', 'Teddington', 'Bio');

EXECUTE ProcAjoutPersonne('Depp', 'Johnny', DATE '1963-06-09', 'Owensboro', 'Bio');

EXECUTE ProcAjoutFilm('Pirates des Caraïbes : La Malédiction du Black Pearl', 2003, 142, 'Anglais', 'Resume', 'fantastique, aventure', 'Etats-Unis');

EXECUTE ProcAjoutScenariste('Verbinski', 'Gore', 'Pirates des Caraïbes : La Malédiction du Black Pearl');

EXECUTE ProcAjoutActeurFilm('Depp', 'Johnny', 'Pirates des Caraïbes : La Malédiction du Black Pearl', 'Jack Sparrow');

EXECUTE ProcAjoutActeurFilm('Knightley', 'Keira', 'Pirates des Caraïbes : La Malédiction du Black Pearl', 'Elizabeth Swann');



EXECUTE ProcAjoutPersonne('Besson', 'Luc', DATE '1959-03-18', 'Paris', 'Bio');

EXECUTE ProcAjoutPersonne('Johansson', 'Scarlett', DATE '1984-11-22', 'New York', 'Bio');

EXECUTE ProcAjoutPersonne('Freeman', 'Morgan', DATE '1937-06-01', 'Menphis', 'Bio');

EXECUTE ProcAjoutFilm('Lucy', 2014, 90, 'Français', 'Resume', 'Science-fiction', 'France');

EXECUTE ProcAjoutScenariste('Besson', 'Luc', 'Lucy');

EXECUTE ProcAjoutActeurFilm('Johansson', 'Scarlett', 'Lucy', 'Lucy');

EXECUTE ProcAjoutActeurFilm('Freeman', 'Morgan', 'Lucy', 'Proffesseur Norman');



EXECUTE ProcAjoutPersonne('Uthaug', 'Roar', DATE '1973-08-25', 'Lorenskog', 'Bio');

EXECUTE ProcAjoutPersonne('Vikander', 'Alicia', DATE '1988-10-03', 'Goteborg', 'Bio');

EXECUTE ProcAjoutPersonne('John-Kamen', 'Hannah', DATE '1989-09-06', 'Anlaby', 'Bio');

EXECUTE ProcAjoutFilm('Tomb Raider', 2018, 118, 'Anglais', 'Resume', 'aventure', 'Etats-Unis');

EXECUTE ProcAjoutScenariste('Uthaug', 'Roar', 'Tomb Raider');

EXECUTE ProcAjoutActeurFilm('Vikander', 'Alicia', 'Tomb Raider', 'Lara Croft');

EXECUTE ProcAjoutActeurFilm('John-Kamen', 'Hannah', 'Tomb Raider', 'Sophie');



EXECUTE ProcAjoutPersonne('Fleischer', 'Ruben', DATE '1974-10-31', 'Washington DC', 'Bio');

EXECUTE ProcAjoutPersonne('Hardy', 'Tom', DATE '1977-09-15', 'Hammersmith', 'Bio');

EXECUTE ProcAjoutPersonne('Williams', 'Michelle', DATE '1980-09-09', 'Kalispell', 'Bio');

EXECUTE ProcAjoutFilm('Venom', 2018, 112, 'Anglais', 'Resume', 'Super heros', 'Etats-Unis');

EXECUTE ProcAjoutScenariste('Fleischer', 'Ruben', 'Venom');

EXECUTE ProcAjoutActeurFilm('Hardy', 'Tom', 'Venom', 'Venom');

EXECUTE ProcAjoutActeurFilm('Williams', 'Michelle', 'Venom', 'Anne Weying');



EXECUTE ProcAjoutPersonne('Russo', 'Anthony', DATE '1970-02-03', 'Cleveland', 'Bio');

EXECUTE ProcAjoutPersonne('Downey', 'Robert', DATE '1965-04-04', 'New York', 'Bio');

EXECUTE ProcAjoutPersonne('Hemsworth', 'Chris', DATE '1983-08-11', 'Melbourne', 'Bio');

EXECUTE ProcAjoutFilm('Avengers: Infinity War', 2018, 149, 'Anglais', 'Resume', 'Super heros', 'Etats-Unis');

EXECUTE ProcAjoutScenariste('Russo', 'Anthony', 'Avengers: Infinity War');

EXECUTE ProcAjoutActeurFilm('Downey', 'Robert', 'Avengers: Infinity War', 'Jack Sparrow');

EXECUTE ProcAjoutActeurFilm('Hemsworth', 'Chris', 'Avengers: Infinity War', 'Elizabeth Swann');



EXECUTE ProcAjoutForfait('Debutant', 10, 1, 7);

EXECUTE ProcAjoutForfait('Intermediaire', 20, 5, 14);

EXECUTE ProcAjoutForfait('Avance', 30, 10, 9999);



EXECUTE ProcAjoutEmploye(1, 'Nom1', 'Prenom1', 'Courriel1', 0, DATE '2000-01-01', 'password', 'adresse');

EXECUTE ProcAjoutEmploye(2, 'Nom2', 'Prenom2', 'Courriel2', 0, DATE '2000-01-01', 'password', 'adresse');



EXECUTE ProcAjoutClient('Nom3', 'Prenom3', 'Courriel3', 0, DATE '2000-01-01', 'password', 'adresse', 'Visa', 0, DATE '2022-01-01', 'Debutant');

EXECUTE ProcAjoutClient('Nom4', 'Prenom4', 'Courriel4', 0, DATE '2000-01-01', 'password', 'adresse', 'Visa', 0, DATE '2022-01-01', 'Debutant');

EXECUTE ProcAjoutClient('Nom5', 'Prenom5', 'Courriel5', 0, DATE '2000-01-01', 'password', 'adresse', 'Visa', 0, DATE '2022-01-01', 'Debutant');

EXECUTE ProcAjoutClient('Nom6', 'Prenom6', 'Courriel6', 0, DATE '2000-01-01', 'password', 'adresse', 'Visa', 0, DATE '2022-01-01', 'Debutant');

EXECUTE ProcAjoutClient('Nom7', 'Prenom7', 'Courriel7', 0, DATE '2000-01-01', 'password', 'adresse', 'Visa', 0, DATE '2022-01-01', 'Debutant');