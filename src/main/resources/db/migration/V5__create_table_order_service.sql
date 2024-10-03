CREATE TABLE order_service (
    id_order UUID REFERENCES orders(id) ON DELETE CASCADE,
    id_service BIGINT REFERENCES service(id) ON DELETE CASCADE,
    PRIMARY KEY (id_order, id_service)
);