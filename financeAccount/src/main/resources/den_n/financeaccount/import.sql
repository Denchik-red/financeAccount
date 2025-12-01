CREATE OR REPLACE FUNCTION get_transaction_report(
    p_account_name VARCHAR,
    p_start_date TIMESTAMP,
    p_end_date TIMESTAMP
)
RETURNS TABLE (
    category_name VARCHAR,
    total_amount NUMERIC(19,2)
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        COALESCE(c.name, 'Без категории')::VARCHAR,
        SUM(t.amount)
    FROM transactions t
    JOIN accounts a ON t.account_id = a.id
    LEFT JOIN categories c ON t.category_id = c.id
    WHERE a.name = p_account_name
      AND t.created_at BETWEEN p_start_date AND p_end_date
    GROUP BY c.name
    ORDER BY total_amount DESC;
END;
$$ LANGUAGE plpgsql;