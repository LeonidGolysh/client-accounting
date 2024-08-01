CREATE TABLE car (
    id UUID PRIMARY KEY,
    id_client UUID NOT NULL,
    car_model VARCHAR(25) NOT NULL,
    car_color VARCHAR(25) NOT NULL,
    car_number_plate VARCHAR(10) NOT NULL,
    FOREIGN KEY (id_client) REFERENCES client(id) ON DELETE CASCADE
);