set pgpassword=password
set str=%TIME%
set str=%str::=_%
set str=%str:,=_%
pg_dump --username=postgres likontrotechcrm>"likontrotechcrm%DATE%_%str%.sql"
