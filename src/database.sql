create database productmanager;

use productmanager;

create table product(
    id int auto_increment primary key,
    name nvarchar(100) not null ,
    price double not null ,
    quantity int not null ,
    color nvarchar(50) not null ,
    description nvarchar(200) not null ,
    category nvarchar(50) not null
);

insert into product (name, price, quantity, color, description, category)
    VALUE ('Iphone XS', 2000, 200, 'Black', 'Made In China', 'Phone');

insert into product (name, price, quantity, color, description, category)
    VALUE ('Iphone XS Max', 2500, 100, 'Black', 'Made In VietNam', 'Phone');

insert into product (name, price, quantity, color, description, category)
    VALUE ('Iphone XS', 2200, 250, 'Gold', 'Made In China', 'Phone');

insert into product (name, price, quantity, color, description, category)
    VALUE ('Galaxy S20 Ultra', 3000, 500, 'Black', 'Made In VietNam', 'Phone');

insert into product (name, price, quantity, color, description, category)
    VALUE ('Galaxy S20', 2000, 300, 'Blue', 'Made In VietNam', 'Phone');

insert into product (name, price, quantity, color, description, category)
    VALUE ('Galaxy S20', 2000, 400, 'Black', 'Made In VietNam', 'Phone');

create table category(
    id int auto_increment primary key,
    name nvarchar(100) not null
);

insert into category(name)
values ('Phone');

insert into category(name)
values ('Television');

insert into category(name)
values ('Speaker');