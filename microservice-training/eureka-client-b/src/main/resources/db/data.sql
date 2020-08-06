DELETE FROM orders;

INSERT INTO orders (name, price,count,order_time, user_id) VALUES
  ('order1', 18,2, CURRENT_TIME,1),
  ('order2', 20, 3,CURRENT_TIME,1),
  ('order3', 28, 1,CURRENT_TIME,3),
  ('order4', 21, 2,CURRENT_TIME,3),
  ('order5', 24, 10,CURRENT_TIME,3),
  ('order6', 99, 15,CURRENT_TIME,5);