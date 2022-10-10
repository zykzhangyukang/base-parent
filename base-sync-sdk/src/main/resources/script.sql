create table pub_mq_message(
 mq_message_id int(11) PRIMARY KEY not null auto_increment,
 uuid varchar(32) not null,
 mid varchar(64) DEFAULT null,
 msg_content varchar(8192) default null,
 src_project varchar(16) not null,
 dest_project varchar(16) not null,
 create_time datetime not null,
 send_time datetime default null,
 ack_time datetime default null,
 send_status varchar(16) not null,
 deal_count int(11) not null,
 deal_status varchar(16) not null
)