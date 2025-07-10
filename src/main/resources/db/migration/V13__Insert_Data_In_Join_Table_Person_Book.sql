INSERT INTO person_book (person_id, book_id)
SELECT 
    p.id AS person_id, 
    b.id AS book_id
FROM 
    (SELECT id FROM person WHERE id <= 12) p
CROSS JOIN
    (SELECT id FROM book ORDER BY RAND() LIMIT 20) b;

-- Associar 3 livros às demais pessoas
INSERT INTO person_book (person_id, book_id)
SELECT
    p.id AS person_id,
    b.id AS book_id
FROM
    (SELECT id FROM person WHERE id > 12) p
CROSS JOIN
    (SELECT id FROM book ORDER BY RAND() LIMIT 3) b;