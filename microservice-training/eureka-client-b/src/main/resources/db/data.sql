DELETE FROM orders;

INSERT INTO orders (id, name, price, user_id) VALUES
  (1, 'order1', 18, 1),
  (2, 'order2', 20, 1),
  (3, 'order3', 28, 3),
  (4, 'order4', 21, 3),
  (5, 'order5', 24, 3),
  (6, 'order6', 99, 5);