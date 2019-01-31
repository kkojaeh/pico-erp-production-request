create table prr_production_request (
	id binary(16) not null,
	accepted_date datetime,
	accepter_id varchar(50),
	asap bit not null,
	canceled_date datetime,
	canceler_id varchar(50),
	code varchar(20),
	committed_date datetime,
	committer_id varchar(50),
	completed_date datetime,
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	due_date datetime,
	item_id binary(16),
	last_modified_by_id varchar(50),
	last_modified_by_name varchar(50),
	last_modified_date datetime,
	plan_id binary(16),
	project_id binary(16),
	quantity decimal(19,2),
	requester_id varchar(50),
	spare_quantity decimal(19,2),
	status varchar(20),
	primary key (id)
) engine=InnoDB;

create index IDX9oab398ibuwdr5dvv0k3kh95b
	on prr_production_request (plan_id);
