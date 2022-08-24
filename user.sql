create database spring_mybatis;
use spring_mybatis;
create table userinfo(
	user_id int auto_increment primary key,
    user_name varchar(50),
    user_pwd varchar(50)
);

insert into userinfo values(null,"张三","123456");