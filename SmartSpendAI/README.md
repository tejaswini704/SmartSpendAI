# SmartSpend AI — Indian Expense Tracker

A beginner-friendly full-stack Java/Spring Boot expense tracker starter with Indian Rupee display, transaction management, charts, and a simple trend-based next-month expense estimate.

## Requirements
- Java JDK 21
- Maven 3.9+
- Internet connection for the first Maven build and CDN assets

## Run on Windows
1. Install JDK 21 and Maven if not already installed.
2. Open Command Prompt in this folder.
3. Check: `java -version` and `mvn -version`
4. Start: `mvn spring-boot:run`
5. Open `http://localhost:8080`

Local development uses an H2 file database stored under `./data`, so transaction records persist locally across restarts.

## Included in this starter
- Dashboard with income, expenses, and net balance for the current month
- Add and delete income/expense transactions
- Indian payment methods and categories
- Indian Rupee display and grouped currency formatting
- Category chart using Chart.js
- Linear trend estimate based on six calendar months of expense totals
- Basic validation

## Important limitations
This is the first working starter stage, not the final production-ready app. Login/registration, user-specific access control, budgets, savings goals, CSV import/export, and automated tests still need to be implemented before public deployment. Do not expose this starter publicly with personal financial data before security is added.

## PostgreSQL / Render
The application includes the PostgreSQL driver. Before deploying, configure the production database URL and credentials as environment variables, and verify Render's current service and database pricing/limits. Do not commit credentials to GitHub.

Environment variables supported:
- `DATABASE_URL` — JDBC URL, for example `jdbc:postgresql://HOST:5432/DBNAME`
- `DB_DRIVER` — `org.postgresql.Driver`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JPA_DIALECT` — `org.hibernate.dialect.PostgreSQLDialect`
- `PORT` — supplied by hosting platform

For local H2, leave these variables unset.
