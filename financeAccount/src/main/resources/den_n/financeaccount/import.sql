create or replace function getTransactionsMonthly(
	p_account_name VARCHAR,
    p_start_date TIMESTAMP,
    p_end_date TIMESTAMP
) returns table (
	date TIMESTAMP,
	amount numeric(19, 2)
) as $$
begin

	return query
	select created_at, sum(transactions.amount) from transactions join accounts on transactions.account_id = accounts.id
	where accounts."name" = p_account_name
	and transactions.created_at between p_start_date and p_end_date
	group by transactions.created_at;

end;
$$ LANGUAGE plpgsql;

SELECT * FROM getTransactionsMonthly('1', '2025-12-01', '2025-12-30');