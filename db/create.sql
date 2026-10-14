-- =====================================================================
--  TrainStation (ALTN72 2026-27) - Creation de la base et donnees de demo
-- ---------------------------------------------------------------------
--  Recree entierement la base en quelques secondes (cf. sujet 7.6.4).
--  A executer sur une instance MariaDB locale, par ex. :
--      mysql -u root < db/create.sql
--
--  Les dates d'ouverture/resolution sont RELATIVES a NOW() (cf. 4 "donnees
--  de demonstration") afin que l'indicateur d'urgence (RG3), calcule a
--  partir de l'heure courante, couvre bien les cas limites 47h / 48h / 72h.
-- =====================================================================

DROP DATABASE IF EXISTS Base_ALTN72;
CREATE DATABASE Base_ALTN72
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_general_ci;
USE Base_ALTN72;

-- ------------------------- Schema -------------------------

CREATE TABLE station (
    id_station INT AUTO_INCREMENT PRIMARY KEY,
    nom        VARCHAR(100) NOT NULL
);

CREATE TABLE prestataire (
    id_prestataire INT AUTO_INCREMENT PRIMARY KEY,
    nom            VARCHAR(100) NOT NULL,
    contact        VARCHAR(150) NOT NULL
);

CREATE TABLE equipement (
    id_equipement INT AUTO_INCREMENT PRIMARY KEY,
    reference     VARCHAR(50)  NOT NULL UNIQUE,          -- RG9 : reference unique
    libelle       VARCHAR(150) NOT NULL,
    type          VARCHAR(50)  NOT NULL,
    etat          VARCHAR(20)  NOT NULL,                 -- EN_SERVICE / EN_PANNE / HORS_SERVICE
    id_station    INT          NOT NULL,
    CONSTRAINT fk_equipement_station FOREIGN KEY (id_station) REFERENCES station (id_station)
);

CREATE TABLE incident (
    id_incident     INT AUTO_INCREMENT PRIMARY KEY,
    id_equipement   INT          NOT NULL,
    description     VARCHAR(1000) NOT NULL,
    date_ouverture  DATETIME     NOT NULL,
    statut          VARCHAR(20)  NOT NULL,               -- NON_AFFECTE / AFFECTE / RESOLU (RG2)
    id_prestataire  INT          NULL,                   -- NULL tant que non affecte (RG1)
    date_resolution DATETIME     NULL,                   -- NULL tant que non resolu (RG5)
    CONSTRAINT fk_incident_equipement  FOREIGN KEY (id_equipement)  REFERENCES equipement (id_equipement),
    CONSTRAINT fk_incident_prestataire FOREIGN KEY (id_prestataire) REFERENCES prestataire (id_prestataire)
);

CREATE TABLE evaluation (
    id_evaluation   INT AUTO_INCREMENT PRIMARY KEY,
    id_incident     INT          NOT NULL UNIQUE,        -- RG8 : une seule evaluation par incident
    note            INT          NOT NULL,               -- 1 a 5 (RG8)
    commentaire     VARCHAR(1000) NULL,
    date_evaluation DATETIME     NOT NULL,
    CONSTRAINT fk_evaluation_incident FOREIGN KEY (id_incident) REFERENCES incident (id_incident)
);

CREATE TABLE utilisateur (
    id_utilisateur     INT AUTO_INCREMENT PRIMARY KEY,
    login              VARCHAR(50)  NOT NULL UNIQUE,
    mot_de_passe_hache VARCHAR(100) NOT NULL,            -- bcrypt, jamais en clair (7.7)
    prenom             VARCHAR(100) NOT NULL,
    profil             VARCHAR(30)  NOT NULL             -- RESPONSABLE_EXPLOITATION / AGENT
);

-- ------------------------- Donnees de demo -------------------------

INSERT INTO station (nom) VALUES
    ('Chatelet'),           -- 1
    ('Gare de Lyon'),       -- 2
    ('Nation');             -- 3

INSERT INTO prestataire (nom, contact) VALUES
    ('Ascenso Maintenance', 'contact@ascenso.fr'),   -- 1
    ('ElecTrain Services',  'support@electrain.fr'), -- 2
    ('PortiQ',              'sav@portiq.fr');         -- 3

INSERT INTO equipement (reference, libelle, type, etat, id_station) VALUES
    ('ASC-CHA-01', 'Ascenseur quai direction La Defense', 'ASCENSEUR',           'EN_PANNE',    1), -- 1
    ('ESC-CHA-02', 'Escalier mecanique sortie Rivoli',    'ESCALIER_MECANIQUE',  'EN_SERVICE',  1), -- 2
    ('POR-GDL-01', 'Portique d acces hall 1',             'PORTIQUE',            'EN_PANNE',    2), -- 3
    ('DIS-GDL-02', 'Distributeur de titres hall 2',       'DISTRIBUTEUR',        'EN_SERVICE',  2), -- 4
    ('ECR-NAT-01', 'Ecran d information quai A',          'ECRAN',               'HORS_SERVICE',3), -- 5
    ('ASC-NAT-02', 'Ascenseur acces PMR',                 'ASCENSEUR',           'EN_PANNE',    3); -- 6

-- Incidents : dates relatives a NOW() pour couvrir les bornes de RG3.
INSERT INTO incident (id_equipement, description, date_ouverture, statut, id_prestataire, date_resolution) VALUES
    -- Non affectes, non resolus : indicateur calcule a l'affichage (RG3)
    (2, 'Marches bloquees, arret d urgence declenche', NOW() - INTERVAL 10 HOUR, 'NON_AFFECTE', NULL, NULL), -- vert (10h)
    (1, 'Cabine immobilisee entre deux niveaux',       NOW() - INTERVAL 47 HOUR, 'NON_AFFECTE', NULL, NULL), -- vert (limite 47h)
    (3, 'Lecteur de badge hors service',               NOW() - INTERVAL 48 HOUR, 'NON_AFFECTE', NULL, NULL), -- orange (limite 48h)
    (5, 'Ecran noir, aucune information affichee',     NOW() - INTERVAL 72 HOUR, 'NON_AFFECTE', NULL, NULL), -- rouge (limite 72h)
    (6, 'Porte ne s ouvre plus, acces PMR impossible', NOW() - INTERVAL 100 HOUR,'NON_AFFECTE', NULL, NULL), -- rouge (100h)
    -- Affecte, non resolu : presentation distincte, pas d'indicateur vert/orange/rouge (RG4)
    (1, 'Bruit anormal signale par un agent',          NOW() - INTERVAL 60 HOUR, 'AFFECTE', 1, NULL),
    -- Resolus : historique, du plus recent au plus ancien par date de resolution (RG6/RG7)
    (4, 'Bourrage papier recurrent',                   NOW() - INTERVAL 200 HOUR,'RESOLU', 2, NOW() - INTERVAL 150 HOUR),
    (3, 'Barriere qui ne se referme pas',              NOW() - INTERVAL 120 HOUR,'RESOLU', 3, NOW() - INTERVAL 24 HOUR);

-- Evaluation d'un incident resolu et affecte (RG8), note entiere 1..5.
INSERT INTO evaluation (id_incident, note, commentaire, date_evaluation) VALUES
    (7, 4, 'Intervention rapide et proprement realisee.', NOW() - INTERVAL 149 HOUR);

-- Utilisateurs : mots de passe bcrypt (jamais en clair). Comptes de test -> journal.md (README).
-- Les deux comptes ci-dessous utilisent le meme mot de passe de demonstration.
INSERT INTO utilisateur (login, mot_de_passe_hache, prenom, profil) VALUES
    ('resp',  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Florian',  'RESPONSABLE_EXPLOITATION'),
    ('agent', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Alexandre',  'AGENT');
