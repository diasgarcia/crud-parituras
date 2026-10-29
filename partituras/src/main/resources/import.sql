-- Compositores
INSERT INTO compositores (id, nome) VALUES (1, 'Ludwig van Beethoven');
INSERT INTO compositores (id, nome) VALUES (2, 'Wolfgang Amadeus Mozart');
INSERT INTO compositores (id, nome) VALUES (3, 'Johann Sebastian Bach');
INSERT INTO compositores (id, nome) VALUES (4, 'Frédéric Chopin');
INSERT INTO compositores (id, nome) VALUES (5, 'Heitor Villa-Lobos');

-- Partituras
INSERT INTO partituras (id, titulo, instrumento, dificuldade, status, caminho_pdf, compositor_id) VALUES (1, 'Für Elise', 'Piano', 'Intermediária', 'Em andamento', NULL, 1);
INSERT INTO partituras (id, titulo, instrumento, dificuldade, status, caminho_pdf, compositor_id) VALUES (2, 'Marcha Turca', 'Piano', 'Avançada', 'Em andamento', NULL, 2);
INSERT INTO partituras (id, titulo, instrumento, dificuldade, status, caminho_pdf, compositor_id) VALUES (3, 'Prelúdio em Dó Maior', 'Piano', 'Iniciante', 'Concluída', NULL, 3);
INSERT INTO partituras (id, titulo, instrumento, dificuldade, status, caminho_pdf, compositor_id) VALUES (4, 'Prelúdio em Mi Menor', 'Piano', 'Intermediária', 'Não iniciada', NULL, 4);
INSERT INTO partituras (id, titulo, instrumento, dificuldade, status, caminho_pdf, compositor_id) VALUES (5, 'O Trenzinho do Caipira', 'Orquestra', 'Avançada', 'Não iniciada', NULL, 5);
