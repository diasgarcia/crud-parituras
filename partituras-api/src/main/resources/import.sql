-- Compositores
INSERT INTO compositores (id, nome) VALUES (1, 'Ludwig van Beethoven');
INSERT INTO compositores (id, nome) VALUES (2, 'Wolfgang Amadeus Mozart');
INSERT INTO compositores (id, nome) VALUES (3, 'Johann Sebastian Bach');
INSERT INTO compositores (id, nome) VALUES (4, 'Frédéric Chopin');
INSERT INTO compositores (id, nome) VALUES (5, 'Heitor Villa-Lobos');

-- Partituras (links oficiais de download do IMSLP; podem exigir JavaScript no navegador)
INSERT INTO partituras (id, titulo, instrumento, dificuldade, status, caminho_pdf, compositor_id) VALUES (1, 'Für Elise', 'Piano', 'Intermediária', 'Em andamento', 'https://imslp.org/wiki/Special:ImagefromIndex/318757', 1);
INSERT INTO partituras (id, titulo, instrumento, dificuldade, status, caminho_pdf, compositor_id) VALUES (2, 'Marcha Turca', 'Piano', 'Avançada', 'Em andamento', 'https://imslp.org/wiki/Special:ImagefromIndex/56321', 2);
INSERT INTO partituras (id, titulo, instrumento, dificuldade, status, caminho_pdf, compositor_id) VALUES (3, 'Prelúdio em Dó Maior', 'Piano', 'Iniciante', 'Concluída', 'https://imslp.org/wiki/Special:ImagefromIndex/425317', 3);
INSERT INTO partituras (id, titulo, instrumento, dificuldade, status, caminho_pdf, compositor_id) VALUES (4, 'Prelúdio em Mi Menor', 'Piano', 'Intermediária', 'Não iniciada', 'https://imslp.org/wiki/Special:ImagefromIndex/254000', 4);
INSERT INTO partituras (id, titulo, instrumento, dificuldade, status, caminho_pdf, compositor_id) VALUES (5, 'O Trenzinho do Caipira', 'Orquestra', 'Avançada', 'Não iniciada', 'https://imslp.org/wiki/Special:ImagefromIndex/40957', 5);

-- Sessões de estudo (as partituras 4 e 5 ainda não têm estudos)
INSERT INTO sessoes_estudo (id, partitura_id, data, minutos, observacoes) VALUES (1, 1, '2026-10-20', 25, 'Estudo da introdução com as mãos separadas.');
INSERT INTO sessoes_estudo (id, partitura_id, data, minutos, observacoes) VALUES (2, 1, '2026-10-21', 30, 'Prática da primeira seção com metrônomo.');
INSERT INTO sessoes_estudo (id, partitura_id, data, minutos, observacoes) VALUES (3, 2, '2026-10-22', 40, 'Treino lento das passagens rápidas.');
INSERT INTO sessoes_estudo (id, partitura_id, data, minutos, observacoes) VALUES (4, 3, '2026-10-23', 20, 'Revisão dos arpejos e das mudanças de acorde.');
INSERT INTO sessoes_estudo (id, partitura_id, data, minutos, observacoes) VALUES (5, 3, '2026-10-24', 15, 'Execução completa para revisar a peça.');
