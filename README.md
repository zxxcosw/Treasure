# SAT301 FYP A web-based platform for food exchange --  Treasure

## How does it work?
1. Download zip file. The zip file includes a sql file(treasure.sql) and the souce code(Treasure.zip).
2. Create database using the sql file. The sql file have included several data for UI presentation.
3. Import the source code into Android Studio. Change the database connection information and your ip address in JDBCUtils.java
4. When you run this application, the data loading my be slow. Please do your actions after the page is fully loaded, otherwise the force termination may happen.