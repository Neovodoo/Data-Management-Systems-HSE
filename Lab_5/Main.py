import psycopg2
from psycopg2 import extensions

# Variables
# Name of using database
admin_database_name = ""
db_user = ""
db_password = ""
db_name = "lab3_db"
table_name = "students"
host = "127.0.0.1"
port = "5433"


# Functions for override in database
def createTableFunction(table_name, cursor):
    cursor.execute("CREATE OR REPLACE FUNCTION createTable()\r\n" +
                   "RETURNS void\r\n" +
                   "AS\r\n" +
                   "$$\r\n" +
                   "BEGIN\r\n" +
                   "EXECUTE 'CREATE TABLE IF NOT EXISTS " + table_name + "(\r\n" +
                   "id int PRIMARY KEY CHECK (id > 0),\r\n" +
                   "name VARCHAR(50) NOT NULL,\r\n" +
                   "city VARCHAR(50) NOT NULL,\r\n" +
                   "phone_number VARCHAR(15) UNIQUE\r\n" +
                   ")';\r\n" +
                   "END;\r\n" +
                   "$$ LANGUAGE plpgsql;")

def insertValuesFunction(table_name, cursor):
    cursor.execute(
        "CREATE OR REPLACE FUNCTION insertValues(col1 int, col2 VARCHAR(50), col3 VARCHAR(50), col4 VARCHAR(15))\r\n" +
        "RETURNS void\r\n" +
        "AS\r\n" +
        "$$\r\n" +
        "BEGIN\r\n" +
        "INSERT INTO " + table_name + " (id, name, city, phone_number)\r\n" +
        "VALUES (col1, col2, col3, col4\r\n" +
        ");\r\n" +
        "END;\r\n" +
        "$$ LANGUAGE plpgsql;")

def searchCityFunction(table_name, cursor):
    cursor.execute("CREATE OR REPLACE FUNCTION searchCity(searched_city VARCHAR(50))\r\n" +
                   "RETURNS TABLE (st_id INTEGER, st_name VARCHAR(50), st_city VARCHAR(50), st_phone_number VARCHAR (15))\r\n" +
                   "AS\r\n" +
                   "$$\r\n" +
                   "BEGIN\r\n" +
                   "RETURN QUERY SELECT id, name, city, phone_number FROM " + table_name + " \r\n" +
                   "WHERE city ILIKE '%' || searched_city || '%' ;\r\n" +
                   "END;\r\n" +
                   "$$ LANGUAGE plpgsql;")

def updateTableFunction(table_name, cursor):
    cursor.execute("CREATE OR REPLACE FUNCTION updateTable(up_id INTEGER, nw_name VARCHAR(50), nw_city VARCHAR(50)," +
                   " nw_phone_number VARCHAR(15) )\r\n" +
                   "RETURNS VOID \r\n" +
                   "AS\r\n" +
                   "$$\r\n" +
                   "BEGIN\r\n" +
                   "UPDATE " + table_name + " SET name = nw_name, city = nw_city, phone_number = nw_phone_number\r\n" +
                   "WHERE up_id = id ;\r\n" +
                   "END;\r\n" +
                   "$$ LANGUAGE plpgsql;")

def deleteByNameFunction(table_name, cursor):
    cursor.execute("CREATE OR REPLACE FUNCTION deleteByName(del_name VARCHAR(50))\r\n" +
                   "RETURNS VOID \r\n" +
                   "AS\r\n" +
                   "$$\r\n" +
                   "BEGIN\r\n" +
                   "DELETE FROM " + table_name + " \r\n" +
                   "WHERE del_name = name ;\r\n" +
                   "END;\r\n" +
                   "$$ LANGUAGE plpgsql;")

def clearTableFunction(table_name, cursor):
    cursor.execute("CREATE OR REPLACE FUNCTION clearTable()\r\n" +
                   "RETURNS VOID \r\n" +
                   "AS\r\n" +
                   "$$\r\n" +
                   "BEGIN\r\n" +
                   "DELETE FROM " + table_name + "; \r\n" +
                   "END;\r\n" +
                   "$$ LANGUAGE plpgsql;")

def printTableFunction(cursor):
    cursor.execute("CREATE OR REPLACE FUNCTION printTable(table_name text)\r\n" +
                   "RETURNS TABLE (st_id INTEGER, st_name VARCHAR(50), st_city VARCHAR(50), st_phone_number VARCHAR (15)) \r\n" +
                   "AS\r\n" +
                   "$$\r\n" +
                   "BEGIN\r\n" +
                   "RETURN QUERY EXECUTE 'SELECT id, name, city, phone_number FROM ' || table_name; \r\n" +
                   "END;\r\n" +
                   "$$ LANGUAGE plpgsql;")

def newReaderFunction(cursor):
    cursor.execute("CREATE OR REPLACE FUNCTION newReader(username text, password text)\r\n" +
                   "RETURNS VOID \r\n" +
                   "AS\r\n" +
                   "$$\r\n" +
                   "BEGIN\r\n" +
                   "IF EXISTS (SELECT FROM pg_roles WHERE rolname = username) THEN\r\n" +
                   "    EXECUTE 'REVOKE ALL PRIVILEGES ON ALL TABLES IN SCHEMA public FROM ' || username || ';';\r\n" +
                   "    EXECUTE 'REVOKE ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public FROM ' || username || ';';\r\n" +
                   "    EXECUTE 'REVOKE ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public FROM ' || username || ';';\r\n" +
                   "    EXECUTE 'DROP OWNED BY ' || username || ';';\r\n" +
                   "    EXECUTE 'DROP USER ' || username || ';';\r\n" +
                   "END IF;\r\n" +
                   "EXECUTE 'CREATE USER ' || username || ' WITH PASSWORD ''' || password || ''';';\r\n" +
                   "END;\r\n" +
                   "$$ LANGUAGE plpgsql;")

def giveRightsFunction(cursor):
    cursor.execute("CREATE OR REPLACE FUNCTION giveRights(username VARCHAR(50))\r\n" +
                   "RETURNS VOID \r\n" +
                   "AS\r\n" +
                   "$$\r\n" +
                   "BEGIN\r\n" +
                   "EXECUTE 'GRANT SELECT ON Students TO ' || username || ';';\r\n" +
                   "EXECUTE 'ALTER FUNCTION printTable(text) OWNER TO ' || username || ';';\r\n" +
                   "EXECUTE 'ALTER FUNCTION searchCity(VARCHAR(50)) OWNER TO ' || username || ';';\r\n" +
                   "END;\r\n" +
                   "$$ LANGUAGE plpgsql;")


# Function, which override functions in database
def overrideFunctions(table_name, cursor):
    createTableFunction(table_name, cursor)
    insertValuesFunction(table_name, cursor)
    searchCityFunction(table_name, cursor)
    updateTableFunction(table_name, cursor)
    deleteByNameFunction(table_name, cursor)
    clearTableFunction(table_name, cursor)
    printTableFunction(cursor)
    newReaderFunction(cursor)
    giveRightsFunction(cursor)


# Function for console menu off application
def show_menu():
        print("Функционал по работе с БД >>\n" +
              "(0) Завершение программы\n" +
              "(1) Создание БД с таблицей Студентов\n" +
              "(2) Заполнение таблицы значениями\n" +
              "(3) Поиск Студентов по городу\n" +
              "(4) Обновление записи таблицы\n" +
              "(5) Удаление записей по имени\n" +
              "(6) Очистка таблицы Студентов\n" +
              "(7) Удаление БД\n" +
              "(8) Просмотр содержимого таблицы Студентов")


# Init connection to database
is_database_exist = False
try:
    # Create connection to existing database
     conn = psycopg2.connect(dbname=db_name, user=db_user,
                                       password=db_password, host=host, port=port)
     is_database_exist = True
     cursor = conn.cursor()
     autocommit = extensions.ISOLATION_LEVEL_AUTOCOMMIT
     conn.set_isolation_level(autocommit)

except Exception:
    # Create connection to admin of all databases
    conn = psycopg2.connect(dbname=admin_database_name, user=db_user,
                        password=db_password, host=host, port=port)
    print("База данных не существует. Режим пользователя недоступен. Сначала создайте базу данных и обновите приложение.")
    cursor = conn.cursor()
    autocommit = extensions.ISOLATION_LEVEL_AUTOCOMMIT
    conn.set_isolation_level(autocommit)

choice = "1"
# Choice of application usage mode
if is_database_exist == True:
    print("Выберите режим доступа: \n"+
                    "(1) - Администратор\n" +
                    "(2) - Гость")
    choice = input()
else:
    print("Вы находитесь в режиме администратора базы данных")


#Check and create reader account in database
if choice == "2":
    reader_name = "reader2";
    reader_pass = "1234";

    #Checking of existing account
    try:
        #Return exception if account exists
        cursor.execute(f"SELECT newReader('{reader_name}', '{reader_pass}');")
        # Create account and give rights if it does not exist
        cursor.execute(f"SELECT giveRights('{reader_name}');")
        cursor.close()
        conn.close()
        # Create connection to new reader account
        conn = psycopg2.connect(dbname=db_name, user=reader_name,
                                password=reader_pass, host=host, port=port)
        cursor = conn.cursor()
        autocommit = extensions.ISOLATION_LEVEL_AUTOCOMMIT
        conn.set_isolation_level(autocommit)
        print("Гостевой аккаунт cоздан и доступен к использованию")
    except Exception:
        # Give rights to existing account
        try:
            # Will return exception if account already have rights
            cursor.execute(f"SELECT giveRights('{reader_name}');")
        except Exception:
            # Close admin connection
            cursor.close()
            conn.close()
        # Create connection to reader account if it exists
        conn = psycopg2.connect(dbname=db_name, user=reader_name,
                                password=reader_pass, host=host, port=port)
        cursor = conn.cursor()
        autocommit = extensions.ISOLATION_LEVEL_AUTOCOMMIT
        conn.set_isolation_level(autocommit)
        print("Гостевой аккаунт доступен")
else:
    print("Вы находитесь в режиме администратора базы данных")




# Menu functionality
exit = False
while exit == False:
        show_menu()
        decision = input()
        if decision == '0':
                exit=True
        if decision == '1'and is_database_exist == False:
            # Create connection to admin of all databases
            conn = psycopg2.connect(dbname=admin_database_name, user=db_user,
                                    password=db_password, host=host, port=port)
            cursor = conn.cursor()
            autocommit = extensions.ISOLATION_LEVEL_AUTOCOMMIT
            conn.set_isolation_level(autocommit)
            cursor.execute(f"CREATE DATABASE {db_name};")
            print("База данных создана успешно")

            # Create connection to existing database
            conn = psycopg2.connect(dbname=db_name, user=db_user,
                                    password=db_password, host=host, port=port)
            cursor = conn.cursor()
            autocommit = extensions.ISOLATION_LEVEL_AUTOCOMMIT
            conn.set_isolation_level(autocommit)
            #Overriding functions in new database
            overrideFunctions(table_name, cursor)
            #Create table in new database
            cursor.execute("Select createTable();")
        if decision == '1' and is_database_exist == True:
            print("База данных уже существует либо вы находитесь в режиме читателя")
        if decision == '2':
                id = input("Введите id >>")
                name = input("Введите имя Студента >>")
                city = input("Введите Город >>")
                phone_num = input("Введите номер телефона >>")
                cursor.execute(f"Select insertValues({id},'{name}', '{city}', '{phone_num}');")
                print("|Запись была добавлена!|")
        if decision == '3':
                city = input("Введите Город >>")
                cursor.execute(f"Select searchCity('{city}');")
                for row in cursor:
                        print(row)
        if decision == '4':
                print("|Обновление записи по id|" +
                      "Запись в таблице " + table_name + " содержит следующие значения >>\n" +
                      "|id|Имя|Город|Номер телефона|")
                id = input("Введите id записи для обновления>>")
                name = input("Введите новое имя Студента >>")
                city = input("Введите новый Город >>")
                phone_num = input("Введите новый номер телефона >>")
                cursor.execute(f"Select updateTable({id},'{name}', '{city}', '{phone_num}');")
                print("|Запись была добавлена!|")
        if decision == '5':
                name = input("Введите имя Студента для удаления>>")
                cursor.execute(f"Select deleteByName('{name}');")
                print("|Все совпадения по введенному имени были удалены!|")
        if decision == '6':
                cursor.execute(f"Select clearTable();")
        # Try to delete table by reader account
        if decision == '7' and choice =="2":
                conn = psycopg2.connect(dbname=db_name, user=reader_name,
                                        password=reader_pass, host=host, port=port)
                cursor = conn.cursor()
                autocommit = extensions.ISOLATION_LEVEL_AUTOCOMMIT
                conn.set_isolation_level(autocommit)
                cursor.execute(f"DROP DATABASE IF EXISTS {db_name};")
                exit = True
        #Delete table by admin account
        if decision == '7' and choice == "1":
            # Create connection to admin of all databases
            conn = psycopg2.connect(dbname=admin_database_name, user=db_user,
                                    password=db_password, host=host, port=port)
            cursor = conn.cursor()
            autocommit = extensions.ISOLATION_LEVEL_AUTOCOMMIT
            conn.set_isolation_level(autocommit)
            print("Соединение установлено")
            cursor.execute(f"DROP DATABASE IF EXISTS {db_name};")
            print("БД удалена успешно")
            exit = True
        if decision == '8':
            cursor.execute(f"Select printTable('{table_name}');")
            print("_______________________")
            for row in cursor:
                print(row)
            print("________________________")
cursor.close()
conn.close()






