CREATE TABLE 'CLOTH_ITEM' ('_id' INTEGER PRIMARY KEY, 'NAME' TEXT, 'TYPE'  TEXT, 'IMAGE_URL' TEXT, 'IS_FAVORITE' INTEGER);
CREATE TABLE 'FAV_PAIR' ('_id' INTEGER PRIMARY KEY, 'UPPER_ID' INTEGER, LOWER_ID INTEGER);