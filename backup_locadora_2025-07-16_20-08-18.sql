-- Backup da Locadora
-- Data: 2025-07-16 20:08:22
-- Arquivo: backup_locadora_2025-07-16_20-08-18.sql

SET FOREIGN_KEY_CHECKS = 0;

-- Estrutura da tabela clientes
DROP TABLE IF EXISTS `clientes`;
CREATE TABLE `clientes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `telefone` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `data_cadastro` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `idx_nome` (`nome`),
  KEY `idx_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dados da tabela clientes
INSERT INTO `clientes` VALUES (1, 'João Silva', 'joao.silva@email.com', '(11) 98765-4321', '2025-07-10 19:14:22.0');
INSERT INTO `clientes` VALUES (2, 'Maria Santos', 'maria.santos@email.com', '(11) 87654-3210', '2025-07-10 19:14:22.0');
INSERT INTO `clientes` VALUES (3, 'Pedro Oliveira', 'pedro.oliveira@email.com', '(11) 76543-2109', '2025-07-10 19:14:22.0');
INSERT INTO `clientes` VALUES (4, 'Ana Costa', 'ana.costa@email.com', '(11) 65432-1098', '2025-07-10 19:14:22.0');
INSERT INTO `clientes` VALUES (5, 'Carlos Ferreira', 'carlos.ferreira@email.com', '(11) 54321-0987', '2025-07-10 19:14:22.0');

-- Estrutura da tabela filmes
DROP TABLE IF EXISTS `filmes`;
CREATE TABLE `filmes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `titulo` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `genero` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ano` int NOT NULL,
  `sinopse` text COLLATE utf8mb4_unicode_ci,
  `caminho_imagem` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `data_cadastro` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_titulo` (`titulo`),
  KEY `idx_genero` (`genero`),
  KEY `idx_ano` (`ano`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dados da tabela filmes
INSERT INTO `filmes` VALUES (1, 'O Poderoso Chefão', 'Drama', 1972, 'A saga de uma família mafiosa italiana-americana que luta para manter o controle de seu império criminoso na América dos anos 1940', 'imagens\filmes\o_poderoso_chef_o_1752189319212.jpg', '2025-07-10 19:14:17.0');
INSERT INTO `filmes` VALUES (2, 'Pulp Fiction', 'Crime', 1994, 'Histórias interligadas de crime e redenção em Los Angeles, contadas de forma não linear', 'imagens\filmes\pulp_fiction_1752189365750.jpg', '2025-07-10 19:14:17.0');
INSERT INTO `filmes` VALUES (3, 'O Senhor dos Anéis: A Sociedade do Anel', 'Fantasia', 2001, 'Um hobbit embarca em uma jornada épica para destruir um anel mágico e salvar a Terra Média', 'imagens\filmes\o_senhor_dos_an_is__a_sociedade_do_anel_1752189331557.jpg', '2025-07-10 19:14:17.0');
INSERT INTO `filmes` VALUES (4, 'Matrix', 'Ficção Científica', 1999, 'Um programador descobre que a realidade é uma simulação computacional e se junta à resistência', 'imagens\filmes\matrix_1752189306869.jpg', '2025-07-10 19:14:17.0');
INSERT INTO `filmes` VALUES (5, 'Cidade de Deus', 'Drama', 2002, 'A história da criminalidade na favela Cidade de Deus, no Rio de Janeiro, durante as décadas de 60, 70 e 80', 'imagens\filmes\cidade_de_deus_1752189269406.jpg', '2025-07-10 19:14:17.0');
INSERT INTO `filmes` VALUES (6, 'Interestelar', 'Ficção Científica', 2014, 'Em um futuro próximo, um grupo de exploradores espaciais usa um buraco de minhoca para viajar através do espaço', 'imagens\filmes\interestelar_1752189294270.jpg', '2025-07-10 19:14:17.0');
INSERT INTO `filmes` VALUES (7, 'Parasita', 'Suspense', 2019, 'Uma família pobre se infiltra na vida de uma família rica, levando a consequências inesperadas', 'imagens\filmes\parasita_1752189353037.jpg', '2025-07-10 19:14:17.0');
INSERT INTO `filmes` VALUES (8, 'Coringa', 'Drama', 2019, 'A origem sombria do icônico vilão do Batman, mostrando sua transformação de comediante fracassado em criminoso', 'imagens\filmes\coringa_1752189281814.jpg', '2025-07-10 19:14:17.0');
INSERT INTO `filmes` VALUES (9, 'Star Wars: Episódio III – A Vingança dos Sith', 'ação sci-fi', 2005, '', 'imagens\filmes\star_wars__epis_dio_iii___a_vingan_a_dos_sith_1752618394661.jpg', '2025-07-10 20:22:05.0');
INSERT INTO `filmes` VALUES (10, 'Kill Bill', 'Ação', 2003, 'Depois de despertar de um coma de quatro anos, uma antiga assassina busca vingança contra o grupo de assassinos que a traiu.', 'imagens\filmes\kill_bill_1752529155206.jpg', '2025-07-14 18:39:15.0');
INSERT INTO `filmes` VALUES (11, 'teste10', 'suspense', 2025, '', 'imagens\filmes\teste10_1752707278203.jpg', '2025-07-16 20:07:36.0');

SET FOREIGN_KEY_CHECKS = 1;

-- Backup concluído!
