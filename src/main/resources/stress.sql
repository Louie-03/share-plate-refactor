DELETE from keyword;
DELETE from wish;
DELETE from share_image;
DELETE from activity_notification;
DELETE from notification;
DELETE from hashtag;
DELETE from entry;
DELETE from chat_log;
DELETE from chat;
DELETE from chat_room_member;
DELETE from chat_room;
DELETE from share;
DELETE from members;

LOAD DATA LOCAL INFILE './members.csv'
    INTO TABLE members
    FIELDS TERMINATED BY ','
    LINES TERMINATED BY '\n';

LOAD DATA LOCAL INFILE './share.csv'
    INTO TABLE share
    FIELDS TERMINATED BY ','
    LINES TERMINATED BY '\n'
    (id, @cancel, closed_date_time, created_date_time, description, latitude, location,
     location_guide, @location_negotiation, longitude, original_price, price, @price_negotiation,
     recruitment, title, type, writer_id)
SET
    cancel = cast(@cancel as signed),
    location_negotiation = cast(@location_negotiation as signed),
    price_negotiation = cast(@price_negotiation as signed);

LOAD DATA LOCAL INFILE './share_image.csv'
    INTO TABLE share_image
    FIELDS TERMINATED BY ','
    LINES TERMINATED BY '\n'
    (image_url, share_id);

LOAD DATA LOCAL INFILE './entry.csv'
    INTO TABLE entry
    FIELDS TERMINATED BY ','
    LINES TERMINATED BY '\n'
    (member_id, share_id);

LOAD DATA LOCAL INFILE './chat_room.csv'
    INTO TABLE chat_room
    FIELDS TERMINATED BY ','
    LINES TERMINATED BY '\n'
    (id, share_id, type);

LOAD DATA LOCAL INFILE './chat_room_member.csv'
    INTO TABLE chat_room_member
    FIELDS TERMINATED BY ','
    LINES TERMINATED BY '\n'
    (chat_room_id, member_id);

LOAD DATA LOCAL INFILE './hashtag.csv'
    INTO TABLE hashtag
    FIELDS TERMINATED BY ','
    LINES TERMINATED BY '\n'
    (contents, share_id);
