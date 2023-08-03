-- Insert data into 'users' table
INSERT INTO users (surname, lastname, email, login, password, phone_number, country, city)
VALUES ('TestUserSurname', 'TestUserLastName', 'testUser@example.com', 'testUser', '$2a$12$5XwRjrAcTE/Dm67wEYZJgOYFOrn0ShQrRUmljQTsvkizJRYLcGIem', '1234567890', 'TestCountry', 'TestCity');

-- Insert data into 'user_roles' table
INSERT INTO user_roles (user_id, user_role)
VALUES (1, 'USER');

-- Insert data into 'users' table
INSERT INTO users (surname, lastname, email, login, password, phone_number, country, city)
VALUES ('TestAdminUserSurname', 'TestAdminUserLastName', 'testAdminUser@example.com', 'testAdminUser', '$2a$12$5XwRjrAcTE/Dm67wEYZJgOYFOrn0ShQrRUmljQTsvkizJRYLcGIem', '1234567890', 'TestCountry', 'TestCity');

-- Insert data into 'user_roles' table
INSERT INTO user_roles (user_id, user_role)
VALUES (2, 'ADMIN');
