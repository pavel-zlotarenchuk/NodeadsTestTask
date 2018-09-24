DROP TABLE IF EXISTS favorites_table

CREATE TABLE IF NOT EXISTS favorites_table (
    _id text UNIQUE,
    firstname text,
    lastname text,
    placeOfWork text,
    position text,
    linkPDF text,
    comment text
)