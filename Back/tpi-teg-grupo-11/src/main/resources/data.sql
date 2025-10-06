-- MAPS
INSERT INTO games_maps (game_map_id,name, image)
VALUES (1,'Mapa Clásico', 'mapa.svg');

-- OBJECTIVES TYPES
INSERT INTO objectives_types (name)
VALUES ('Conquest');
INSERT INTO objectives_types (name)
VALUES ('Destruction');

-- Objetivos General (con tipo 'General')
INSERT INTO objectives (description, objective_type, color,
                        first_region_territories_needed, second_region_territories_needed,
                        third_region_territories_needed, fourth_region_territories_needed,
                        fifth_region_territories_needed,
                        sixth_region_territories_needed)
VALUES
-- 1
('Ocupar Europa, 4 países de Asia y 2 países de América del Sur', 1, NULL, 0, 2, 9, 0, 4, 0),
-- 2
('Ocupar América del Norte, 2 Oceanía y 4 de Asia', 1, NULL, 10, 0, 0, 0, 4, 2),
-- 3
('Ocupar América del Sur, 7 países de Europa y 3 países de África', 1, NULL, 0, 6, 7, 3, 0, 0),
-- 4
('Ocupar Oceanía, América del Norte y 2 países de Europa', 1, NULL, 10, 0, 2, 0, 0, 4),

('Ocupar Asia y 2 países de América del Sur', 1, NULL, 0, 2, 0, 0, 14, 0),
-- 6
('Ocupar América del Sur, África y 4 países de Asia', 1, NULL, 0, 6, 0, 6, 4, 0),
-- 7
('Ocupar 2 países de Oceanía, 2 países de África, 2 países de América del Sur, 3 países de Europa, 4 países de América del Norte y 3 países de Asia', 1, NULL, 4, 2, 3, 2, 3, 2),
-- 8
('Ocupar Oceanía, África y 5 países de América del Norte', 1, NULL, 5, 0, 0, 6, 0, 4),
-- 9
('Ocupar África, 5 países de América del Norte y 4 países de Europa', 1, NULL, 5, 0, 4, 6, 0, 0);


-- Objetivos Destruction (con color asignado)
INSERT INTO objectives (description, objective_type, color,
                        first_region_territories_needed, second_region_territories_needed,
                        third_region_territories_needed, fourth_region_territories_needed,
                        fifth_region_territories_needed,
                        sixth_region_territories_needed)
VALUES
-- 10
('Destruye al ejército Rojo', 2, 'RED', 0, 0, 0, 0, 0, 0),
-- 11
('Destruye al ejército Azul', 2, 'BLUE', 0, 0, 0, 0, 0, 0),
-- 12
('Destruye al ejército Verde', 2, 'GREEN', 0, 0, 0, 0, 0, 0),
-- 13
('Destruye al ejército Amarillo', 2, 'YELLOW', 0, 0, 0, 0, 0, 0),
-- 14
('Destruye al ejército Morado', 2, 'PURPLE', 0, 0, 0, 0, 0, 0),
-- 15
('Destruye al ejército Anaranjado', 2, 'ORANGE', 0, 0, 0, 0, 0, 0);

-- Insertar símbolos para las cartas
INSERT INTO symbols (symbol_id, symbol, image) -- Faltan imagenes todavia
VALUES (1, 'CANNON',null),
       (2, 'SHIP',null),
       (3, 'GLOBE',null),
       (4, 'WILD',null);

-- Insertar regiones
INSERT INTO regions (region_id, game_map_id, name, number_of_territories)
VALUES (1, 1, 'América del Norte', 10),
       (2, 1, 'América del Sur', 6),
       (3, 1, 'Europa', 9),
       (4, 1, 'África', 6),
       (5, 1, 'Asia', 14),
       (6, 1, 'Oceanía', 4);

-- Insertar territorios
-- América del Norte
INSERT INTO territories (territory_id, region_id, name)
VALUES (1, 1, 'Alaska' ),
       (2, 1, 'Yukon' ),
       (3, 1, 'Oregon' ),
       (4, 1, 'California' ),
       (5, 1, 'México'),
       (6, 1, 'Nueva York'),
       (7, 1, 'Terranova'),
       (8, 1, 'Labrador'),
       (9, 1, 'Groenlandia'),
       (10, 1, 'Canadá');

-- América del Sur
INSERT INTO territories (territory_id, region_id, name)
VALUES (11, 2, 'Colombia'),
       (12, 2, 'Perú'),
       (13, 2, 'Brasil'),
       (14, 2, 'Argentina'),
       (15, 2, 'Chile'),
       (16, 2, 'Uruguay');

-- Europa
INSERT INTO territories (territory_id, region_id, name)
VALUES (17, 3, 'Islandia'),
       (18, 3, 'Gran Bretaña'),
       (19, 3, 'Francia'),
       (20, 3, 'España'),
       (21, 3, 'Alemania'),
       (22, 3, 'Polonia'),
       (23, 3, 'Rusia'),
       (24, 3, 'Suecia'),
       (25, 3, 'Italia');

-- África
INSERT INTO territories (territory_id, region_id, name)
VALUES (26, 4, 'Sahara'),
       (27, 4, 'Egipto'),
       (28, 4, 'Etiopía'),
       (29, 4, 'Zaire'),
       (30, 4, 'Sudáfrica'),
       (31, 4, 'Madagascar');

-- Asia
INSERT INTO territories (territory_id, region_id, name)
VALUES (32, 5, 'Tartaria'),
       (33, 5, 'Taymir'),
       (34, 5, 'Kamchatka'),
       (35, 5, 'Aral'),
       (36, 5, 'Siberia'),
       (37, 5, 'Mongolia'),
       (38, 5, 'Gobi'),
       (39, 5, 'China'),
       (40, 5, 'Turquía'),
       (41, 5, 'Israel'),
       (42, 5, 'Arabia'),
       (43, 5, 'India'),
       (44, 5, 'Malasia'),
       (45, 5, 'Japón'),
       (46, 5, 'Irán');

-- Oceanía
INSERT INTO territories (territory_id, region_id, name)
VALUES (47, 6, 'Sumatra'),
       (48, 6, 'Borneo'),
       (49, 6, 'Java'),
       (50, 6, 'Australia');

-- Insertar cartas de territorios
INSERT INTO cards (card_id, territory_id, symbol_id)
VALUES
-- América del Norte
(1, 1, 2),
(2, 2, 3),
(3, 3, 1),
(4, 4, 1),
(5, 5, 1),
(6, 6, 2),
(7, 7, 1),
(8, 8, 1),
(9, 9, 3),
(10, 10, 1),
-- América del Sur
(11, 11, 3),
(12, 12, 2),
(13, 13, 2),
(14, 14, 4),
(15, 15, 3),
(16, 16, 3),
-- Europa
(17, 17, 2),
(18, 18, 2),
(19, 19, 3),
(20, 20, 3),
(21, 21, 2),
(22, 22, 1),
(23, 23, 3),
(24, 24, 2),
(25, 25, 3),
-- África
(26, 26, 1),
(27, 27, 3),
(28, 28, 3),
(29, 29, 2),
(30, 30, 1),
(31, 31, 2),
-- Asia
(32, 32, 1),
(33, 33, 4),
(34, 34, 3),
(35, 35, 1),
(36, 36, 2),
(37, 37, 2),
(38, 38, 3),
(39, 39, 2),
(40, 40, 2),
(41, 41, 2),
(42, 42, 1),
(43, 43, 3),
(44, 44, 1),
(45, 45, 1),
(46,46,3),
-- Oceanía
(47, 47, 3),
(48, 48, 2),
(49, 49, 1),
(50, 50, 1);

-- Insertar fronteras entre territorios
INSERT INTO borders (territory_id, border_id)
VALUES
-- América del Norte - conexiones internas
(1, 2),  -- Alaska con Yukon
(1, 3),  -- Alaska con Oregon
(2, 3),  -- Yukon con Oregon
(2, 10), --Yukon con Canada
(3, 10),  -- Oregon con Canada
(3, 4),   -- Oregon con California
(3, 6),   -- Oregon con Nueva York
(4, 5),  --California con Mexico
(4,6),   -- California con Nueva York
(6, 7),  -- Nueva York con Terranova
(6,9),   --  Nueva York con Groenlandia
(6, 10),  -- Nueva York con Canada
(7, 8),   -- Terranova con Labrador
(7, 10),   -- Terranova con Canada
(8, 9),  -- Labrador con Groenlandia

-- América del Sur - conexiones internas
(11, 12),  -- Colombia con Peru
(11, 13), -- Colombia con Brasil
(12, 13), -- Peru con Brasil
(12, 15),  -- Peru con Chile
(12, 14), -- Perú con Argentina
(13, 14),  -- Brasil con Argentina
(13, 16),  -- Brasil con Uruguay
(14, 15),  -- Argentina con Chile
(14, 16), -- Argentina con Uruguay

-- Europa - conexiones internas
(17, 18),  -- Islandia con Gran Bretaña
(17,24),   -- Islandia con Secia
(18, 20),   -- Gran Bretaña con España
(18, 21),   -- Gran Bretaña con Alemania
(19, 20),   -- Francia con España
(19, 21),   --Francia con ALemania
(19, 25),   -- Francia con Italia
(21, 22),   -- Alemania con Polonia
(21, 25),   -- Alemania con Italia
(22, 23),   -- Polonia con Rusia
(23,24),    -- Rusia Suecia

-- África - conexiones internas
(26, 27),   -- Sahara con Egipto
(26, 28),   -- Sahara con Etiopia
(26,29),    -- Sahara con Zaire
(27, 28),   -- Egipto con Etiopia
(27, 31),   -- Etiopia con Madagascar
(28, 29),   -- Etiopia con Zaire
(28, 30),   -- Etiopia con Sudafrica
(29, 30),   -- Zaire con sudafrica
(29,31),    -- Zaire con Madagascar

-- Asia - conexiones internas
(32, 33),   -- Tartaria con Taymir
(32, 35),   -- Tartaria con Aral
(32, 36),   -- Tartaria con Siberia
(33, 36),   -- Taymir con Siberia
(34, 36),   -- Kamtchatka con Siberia
(34, 39),   -- Kamtchatka con China
(34, 45),   -- Kamtchatka con Japon
(35, 36),   -- Aral con Siberia
(35, 46),   -- Aral con Iran
(36, 37),   -- Siberia con Mongolia
(36, 39),   -- Siberia con China
(37, 38),   -- Mongolia con Gobi
(37, 39),   -- Mongolia con China
(37,46),    -- Mongolia con Iran
(38, 39),   -- Gobi China
(38,46),    -- Gobi con Iran
(39, 43),   -- China con India
(39, 44),   -- China Malasia
(39, 45),   -- China Japon
(39,46),    -- China Iran
(40, 41),   -- Turquia Israel
(40, 42),   -- Turquia Arabia
(40,46),    -- Turquia Iran
(41, 42),   -- Arabia Israel
(43, 44),   -- India Malasia
(43, 46),   -- India Iran

-- Oceanía - conexiones internas
(47, 50),   -- Sumatra Australia
(48, 50),   -- Borneo Australia
(49, 50),   -- Java Australia

--CONEXIONES INTERCONTINENTALES
--America
(5, 11),  -- México con Colombia
(1,34), --Alaska con Kamchatka
(9,17), --Groenlandia con Islandia
(13, 26), --Brasil con Sahara
(15,50), --Chile con Australia

--Europa
(20,26), --España con Sahara
(22, 27), -- Polonia con Egipto
(22,40), --Polonia con Tuquia
(23,35), --Rusia con Aral
(23,40), -- Rusia Turquia
(23,46), --  Rusia con Iran

--Africa
(27,40), -- Egipto Turquia
(27,41), -- Israel
--Asia
(43,47), --India sumatra
(44,48); --Borneo

--     INSERT INTO borders (territory_id, border_id) VALUES
-- -- América del Norte
-- (1, 2), (2, 1), -- Alaska ↔ Yukon
-- (1, 3), (3, 1), -- Alaska ↔ Oregon
-- (2, 3), (3, 2), -- Yukon ↔ Oregon
-- (2, 10), (10, 2), -- Yukon ↔ Canadá
-- (3, 10), (10, 3), -- Oregon ↔ Canadá
-- (3, 4), (4, 3), -- Oregon ↔ California
-- (4, 5), (5, 4), -- California ↔ México
-- (5, 11), (11, 5), -- México ↔ Colombia (intercontinental)
-- (6, 10), (10, 6), -- Nueva York ↔ Canadá
-- (6, 7), (7, 6), -- Nueva York ↔ Terranova
-- (6, 9), (9, 6), -- Nueva York ↔ Groenlandia
-- (7, 8), (8, 7), -- Terranova ↔ Labrador
-- (7, 10), (10, 7), -- Terranova ↔ Canadá
-- (8, 9), (9, 8), -- Labrador ↔ Groenlandia
-- -- América del Sur
-- (11, 12), (12, 11), -- Colombia ↔ Perú
-- (11, 13), (13, 11), -- Colombia ↔ Brasil
-- (12, 13), (13, 12), -- Perú ↔ Brasil
-- (12, 15), (15, 12), -- Perú ↔ Chile
-- (12, 14), (14, 12), -- Perú ↔ Argentina
-- (13, 14), (14, 13), -- Brasil ↔ Argentina
-- (13, 16), (16, 13), -- Brasil ↔ Uruguay
-- (14, 15), (15, 14), -- Argentina ↔ Chile
-- (14, 16), (16, 14), -- Argentina ↔ Uruguay
-- -- Europa
-- (17, 18), (18, 17), -- Islandia ↔ Gran Bretaña
-- (17, 24), (24, 17), -- Islandia ↔ Suecia
-- (18, 19), (19, 18), -- Gran Bretaña ↔ Francia
-- (18, 24), (24, 18), -- Gran Bretaña ↔ Suecia
-- (19, 20), (20, 19), -- Francia ↔ España
-- (19, 21), (21, 19), -- Francia ↔ Alemania
-- (19, 25), (25, 19), -- Francia ↔ Italia
-- (20, 25), (25, 20), -- España ↔ Italia
-- (21, 22), (22, 21), -- Alemania ↔ Polonia
-- (21, 24), (24, 21), -- Alemania ↔ Suecia
-- (22, 23), (23, 22), -- Polonia ↔ Rusia
-- (22, 24), (24, 22), -- Polonia ↔ Suecia
-- (23, 24), (24, 23), -- Rusia ↔ Suecia
-- -- África
-- (26, 27), (27, 26), -- Sahara ↔ Egipto
-- (26, 28), (28, 26), -- Sahara ↔ Etiopía
-- (26, 29), (29, 26), -- Sahara ↔ Zaire
-- (27, 28), (28, 27), -- Egipto ↔ Etiopía
-- (27, 31), (31, 27), -- Egipto ↔ Madagascar
-- (28, 29), (29, 28), -- Etiopía ↔ Zaire
-- (28, 30), (30, 28), -- Etiopía ↔ Sudáfrica
-- (29, 30), (30, 29), -- Zaire ↔ Sudáfrica
-- (29, 31), (31, 29), -- Zaire ↔ Madagascar
-- -- Asia
-- (32, 33), (33, 32), -- Tartaria ↔ Taymir
-- (32, 35), (35, 32), -- Tartaria ↔ Aral
-- (32, 36), (36, 32), -- Tartaria ↔ Siberia
-- (33, 36), (36, 33), -- Taymir ↔ Siberia
-- (34, 36), (36, 34), -- Kamchatka ↔ Siberia
-- (34, 39), (39, 34), -- Kamchatka ↔ China
-- (34, 45), (45, 34), -- Kamchatka ↔ Japón
-- (35, 36), (36, 35), -- Aral ↔ Siberia
-- (35, 46), (46, 35), -- Aral ↔ Irán
-- (36, 37), (37, 36), -- Siberia ↔ Mongolia
-- (37, 38), (38, 37), -- Mongolia ↔ Gobi
-- (38, 39), (39, 38), -- Gobi ↔ China
-- (39, 40), (40, 39), -- China ↔ Turquía
-- (39, 43), (43, 39), -- China ↔ India
-- (39, 44), (44, 39), -- China ↔ Malasia
-- (39, 45), (45, 39), -- China ↔ Japón
-- (40, 23), (23, 40), -- Turquía ↔ Rusia
-- (40, 46), (46, 40), -- Turquía ↔ Irán
-- (41, 40), (40, 41), -- Israel ↔ Turquía
-- (41, 42), (42, 41), -- Israel ↔ Arabia
-- (42, 43), (43, 42), -- Arabia ↔ India
-- (43, 44), (44, 43), -- India ↔ Malasia
-- (44, 47), (47, 44), -- Malasia ↔ Sumatra
-- (45, 39), (39, 45), -- Japón ↔ China
-- -- Oceanía
-- (47, 48), (48, 47), -- Sumatra ↔ Borneo
-- (48, 49), (49, 48), -- Borneo ↔ Java
-- (49, 50), (50, 49), -- Java ↔ Australia
-- -- Conexiones intercontinentales
-- (1, 34), (34, 1), -- Alaska ↔ Kamchatka
-- (5, 11), (11, 5), -- México ↔ Colombia
-- (9, 17), (17, 9), -- Groenlandia ↔ Islandia
-- (13, 26), (26, 13), -- Brasil ↔ Sahara
-- (15, 50), (50, 15), -- Chile ↔ Australia
-- (20, 26), (26, 20), -- España ↔ Sahara
-- (22, 27), (27, 22), -- Polonia ↔ Egipto
-- (22, 40), (40, 22), -- Polonia ↔ Turquía
-- (23, 35), (35, 23), -- Rusia ↔ Aral
-- (23, 40), (40, 23), -- Rusia ↔ Turquía
-- (23, 46), (46, 23), -- Rusia ↔ Irán
-- (27, 40), (40, 27), -- Egipto ↔ Turquía
-- (27, 41), (41, 27), -- Egipto ↔ Israel
-- (43, 47), (47, 43), -- India ↔ Sumatra
-- (44, 48), (48, 44); -- Malasia ↔ Borneo