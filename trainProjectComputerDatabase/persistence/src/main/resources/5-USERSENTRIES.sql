INSERT INTO users(username,password,enabled)
VALUES ('user','password1234', TRUE);
INSERT INTO users(username,password,enabled)
VALUES ('admincdb','qwerty1234', TRUE);
 
INSERT INTO authorities (username, authority)
VALUES ('admincdb', 'ROLE_USER');
INSERT INTO authorities (username, authority)
VALUES ('admincdb', 'ROLE_ADMIN');
INSERT INTO authorities (username, authority)
VALUES ('user', 'ROLE_USER');