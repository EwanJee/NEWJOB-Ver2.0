INSERT INTO member (id, name, phone_number)
VALUES ('1', 'John Doe', '1234567890')
ON CONFLICT (id) DO NOTHING;

SELECT 1;