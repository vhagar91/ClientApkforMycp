INSERT INTO province(idRef, name, phone_code) VALUES(1, 'Artemisa', '47');
INSERT INTO province(idRef, name, phone_code) VALUES(2, 'Camagüey', '32');
INSERT INTO province(idRef, name, phone_code) VALUES(3, 'Ciego de Ávila', '33');
INSERT INTO province(idRef, name, phone_code) VALUES(4, 'Cienfuegos', '43');
INSERT INTO province(idRef, name, phone_code) VALUES(5, 'Guantánamo', '21');
INSERT INTO province(idRef, name, phone_code) VALUES(6, 'Holguín', '24');
INSERT INTO province(idRef, name, phone_code) VALUES(7, 'Isla de la Juventud', '46');
INSERT INTO province(idRef, name, phone_code) VALUES(8, 'La Habana', '7');
INSERT INTO province(idRef, name, phone_code) VALUES(9, 'Las Tunas', '31');
INSERT INTO province(idRef, name, phone_code) VALUES(10, 'Matanzas', '45');
INSERT INTO province(idRef, name, phone_code) VALUES(11, 'Pinar del Rio', '48');
INSERT INTO province(idRef, name, phone_code) VALUES(12, 'Sancti Spiritus', '41');
INSERT INTO province(idRef, name, phone_code) VALUES(13, 'Santiago de Cuba', '22');
INSERT INTO province(idRef, name, phone_code) VALUES(14, 'Villa Clara', '42');
INSERT INTO province(idRef, name, phone_code) VALUES(16, 'Granma', '23');
INSERT INTO province(idRef, name, phone_code) VALUES(17, 'Mayabeque', '47');

INSERT INTO municipality(idRef, id_province, name) VALUES(10, 11, 'Viñales');
INSERT INTO municipality(idRef, id_province, name) VALUES(11, 5, 'Baracoa');
INSERT INTO municipality(idRef, id_province, name) VALUES(12, 2, 'Camagüey');
INSERT INTO municipality(idRef, id_province, name) VALUES(13, 12, 'Trinidad');
INSERT INTO municipality(idRef, id_province, name) VALUES(14, 13, 'Santiago de Cuba');
INSERT INTO municipality(idRef, id_province, name) VALUES(15, 8, 'Centro Habana');
INSERT INTO municipality(idRef, id_province, name) VALUES(16, 8, 'Habana Vieja');
INSERT INTO municipality(idRef, id_province, name) VALUES(18, 4, 'Ciudad de Cienfuegos');
INSERT INTO municipality(idRef, id_province, name) VALUES(19, 14, 'Santa Clara');
INSERT INTO municipality(idRef, id_province, name) VALUES(20, 8, 'Playa');
INSERT INTO municipality(idRef, id_province, name) VALUES(22, 10, 'Matanzas');
INSERT INTO municipality(idRef, id_province, name) VALUES(23, 14, 'Remedios');
INSERT INTO municipality(idRef, id_province, name) VALUES(24, 3, 'Moron');
INSERT INTO municipality(idRef, id_province, name) VALUES(25, 12, 'Sancti Spíritus');
INSERT INTO municipality(idRef, id_province, name) VALUES(26, 10, 'Varadero');
INSERT INTO municipality(idRef, id_province, name) VALUES(27, 10, 'Ciénaga de Zapata');
INSERT INTO municipality(idRef, id_province, name) VALUES(28, 11, 'Pinar del Río');
INSERT INTO municipality(idRef, id_province, name) VALUES(29, 8, 'Plaza de la Revolución');
INSERT INTO municipality(idRef, id_province, name) VALUES(30, 16, 'Bayamo');
INSERT INTO municipality(idRef, id_province, name) VALUES(31, 6, 'Holguín');
INSERT INTO municipality(idRef, id_province, name) VALUES(32, 8, 'Habana del Este');
INSERT INTO municipality(idRef, id_province, name) VALUES(33, 9, 'Tunas');
INSERT INTO municipality(idRef, id_province, name) VALUES(34, 1, 'Soroa');
INSERT INTO municipality(idRef, id_province, name) VALUES(35, 1, 'Bahía Honda');
INSERT INTO municipality(idRef, id_province, name) VALUES(36, 8, 'Diez de Octubre');
INSERT INTO municipality(idRef, id_province, name) VALUES(37, 8, 'Boyeros');
INSERT INTO municipality(idRef, id_province, name) VALUES(41, 6, 'Guardalavaca');
INSERT INTO municipality(idRef, id_province, name) VALUES(42, 6, 'Gibara');
INSERT INTO municipality(idRef, id_province, name) VALUES(43, 7, 'Nueva Gerona');
INSERT INTO municipality(idRef, id_province, name) VALUES(44, 6, 'Banes');

INSERT INTO bathroomType(name) VALUES('Interior privado');
INSERT INTO bathroomType(name) VALUES('Compartido');
INSERT INTO bathroomType(name) VALUES('Exterior privado');

INSERT INTO audiovisualType(name) VALUES('TV cable');
INSERT INTO audiovisualType(name) VALUES('TV');
INSERT INTO audiovisualType(name) VALUES('TV+DVD / Video');
INSERT INTO audiovisualType(name) VALUES('No');
INSERT INTO audiovisualType(name) VALUES('Ninguno');

INSERT INTO climateType(name) VALUES('Aire acondicionado');
INSERT INTO climateType(name) VALUES('Aire acondicionado / Ventilador');
INSERT INTO climateType(name) VALUES('Natural');
INSERT INTO climateType(name) VALUES('Ventilador');

INSERT INTO roomType(name) VALUES('Habitación Triple');
INSERT INTO roomType(name) VALUES('Habitación doble (Dos camas)');
INSERT INTO roomType(name) VALUES('Habitación doble');
INSERT INTO roomType(name) VALUES('Habitación individual');

INSERT INTO reservationStatus(name) VALUES('cancelada');
INSERT INTO reservationStatus(name) VALUES('reservada');

