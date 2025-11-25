 1)alter table  order_items modify column total_item_price decimal(12,2) 
 as (quantity* unit_price) STORED;
 
 2) update orders o left join( 
 select order_id, COALESCE(SUM(total_item_price),0) as calc_total
 from order_items group by order_id
 ) t on o.order_id = t.order_id SET o.total_amount = t.calc_total;
 
 3)Alter table payments  add column gateway_ref varchar(127) default null,
 add column upi_vpa varchar(127) default null,
 add column response_payload text default null;
 
 4)alter table reviews modify review_text text not null;
 
 5)alter table products add column brand  varchar(30) not null;
 6) alter table products add column description text;
 7) alter table products modify description text  not null;
 
 8) alter table orders modify status enum('PENDING','PROCESSSING','SHIPPED','DELIVERED','CANCELLED','REFUNDED');
 9) alter table payments modify status enum('Pending','PAID','Failed','Refunded');
 
 10) alter table orders add column orderStatus enum('PENDING','PAID','PROCESSSING','SHIPPED','DELIVERED','CANCELLED','REFUNDED') not null;
 11) alter table orders drop column status;