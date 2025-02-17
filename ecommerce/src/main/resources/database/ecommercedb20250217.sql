create schema `ecommercedb` default character set utf8mb4 collate utf8mb4_unicode_ci ;

create table `ecommercedb`.`user`(
`id` bigint auto_increment,
`login` varchar(155) not null,
`password` varchar(155) not null,
`role` enum('ADMIN', 'USER') not null,
primary key(`id`)
)
engine = InnoDB default character set utf8mb4 collate utf8mb4_unicode_ci;

create table `ecommercedb`.`user_info`(
`user_info_id` bigint,
`name` varchar(100) not null,
`surname` varchar(255) not null,
`phone` varchar(255) not null,
`email` varchar(255) not null,
primary key(`user_info_id`),
constraint `fk1` foreign key(`user_info_id`) references `ecommercedb`.`user`(`id`)
on update no action on delete cascade
)
engine = InnoDB default character set utf8mb4 collate utf8mb4_unicode_ci;

create table `ecommercedb`.`orders`(
`id` bigint auto_increment,
`user_id` bigint not null,
`order_status` enum('Đã thanh toán', 'Đã hủy', 'Đang chờ xử lý', 'Đã hoàn thành') not null,
`shopping_type` int not null,
`city` varchar(155) null default null,
`address` varchar(155) null default null,
`total_price` float null default null,
primary key(`id`),
constraint `fk2` foreign key(`user_id`) references `ecommercedb`.`user`(`id`)
on update no action on delete no action
)
engine = InnoDB default character set utf8mb4 collate utf8mb4_unicode_ci;

create table `ecommercedb`.`delivery`(
`orders_id` bigint,
`status` enum('Đang xử lý', 'Đã gửi', 'Hủy', 'Đã giao') not null,
primary key(`orders_id`),
constraint `fk3` foreign key(`orders_id`) references `ecommercedb`.`orders`(`id`)
on update cascade on delete cascade
)
engine = InnoDB default character set utf8mb4 collate utf8mb4_unicode_ci;

create table `ecommercedb`.`vendor`(
`id` bigint auto_increment,
`title` varchar(255) not null,
primary key(`id`)
)
engine = InnoDB default character set utf8mb4 collate utf8mb4_unicode_ci;

create table `ecommercedb`.`category`(
`id` bigint auto_increment,
`title` varchar(155) null default null unique,
`alias` varchar(255) not null unique,
`image` varchar(255) not null,
`enabled` tinyint not null,
`parent_id` bigint null default null,
`all_parent_ids` varchar(255) null default null,
primary key(`id`),
constraint `fk4` foreign key(`parent_id`) references `ecommercedb`.`category`(`id`)
on update no action on delete no action
)
engine = InnoDB default character set utf8mb4 collate utf8mb4_unicode_ci;

create table `ecommercedb`.`product`(
`id` bigint auto_increment,
`title` varchar(155) not null,
`alias` varchar(255) null default null,
`description` longtext null default null,
`price` int not null,
`image` varchar(255) null default null,
`vendor_id` bigint not null,
`category_id` bigint not null,
primary key(`id`),
constraint `fk5` foreign key(`vendor_id`) references `ecommercedb`.`vendor`(`id`)
on update cascade on delete cascade,
constraint `fk6` foreign key(`category_id`) references `ecommercedb`.`category`(`id`)
on update cascade on delete cascade
)
engine = InnoDB default character set utf8mb4 collate utf8mb4_unicode_ci;

create table `ecommercedb`.`order_basket`(
`id` bigint auto_increment,
`product_id` bigint not null,
`user_id` bigint not null,
`quantity` int not null,
primary key(`id`),
constraint `fk7` foreign key(`product_id`) references `ecommercedb`.`product`(`id`)
on update no action on delete no action,
constraint `fk8`foreign key(`user_id`) references `ecommercedb`.`user`(`id`)
on update no action on delete no action
)
engine = InnoDB default character set utf8mb4 collate utf8mb4_unicode_ci;

insert into `ecommercedb`.`vendor`(`title`) values ('Apple');
insert into `ecommercedb`.`vendor`(`title`) values ('Samsung');
insert into `ecommercedb`.`vendor`(`title`) values ('Sandy');
insert into `ecommercedb`.`vendor`(`title`) values ('LG');

insert into `ecommercedb`.`category`(`title`, `alias`, `image`, `enabled`) values ('Phones', 'phones', 'phone.png', 1);
insert into `ecommercedb`.`category`(`title`, `alias`, `image`, `enabled`) values ('Clothes', 'clotes', 'clothes.png', 1);
insert into `ecommercedb`.`category`(`title`, `alias`, `image`, `enabled`) values ('TV', 'tv', 'tv.png', 1);
insert into `ecommercedb`.`category`(`title`, `alias`, `image`, `enabled`) values ('Watches', 'watches', 'wathches.png', 1);