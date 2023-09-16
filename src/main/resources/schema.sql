CREATE TABLE authentication.user (
                                     username varchar(50) NOT NULL,
                                     password TEXT NULL,
                                     PRIMARY KEY (username)
);

CREATE TABLE authentication.otp (
                                    username varchar(50) NOT NULL,
                                    code varchar(45) NULL,
                                    PRIMARY KEY (username)
);
