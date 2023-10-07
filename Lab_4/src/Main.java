import java.sql.*;
import java.util.Scanner;
public class Main {
      public static Scanner input = new Scanner(System.in);
      public static void DBFunctions(Connection conn,String dbname,String table) throws SQLException {

            String adminDatabaseUsername = "";
            String databasePassword = "";

            Functions var = new Functions();
            while (true) {
                  System.out.println("Функционал по работе с БД >>\n" +
                          "(0) Завершение программы\n" +
                          "(1) Создание БД с таблицей Студентов\n" +
                          "(2) Заполнение таблицы значениями\n" +
                          "(3) Поиск Студентов по городу\n" +
                          "(4) Обновление записи таблицы\n" +
                          "(5) Удаление записей по имени\n" +
                          "(6) Очистка таблицы Студентов\n" +
                          "(7) Удаление БД\n" +
                          "(8) Просмотр содержимого таблицы Студентов");
                  int decision = input.nextInt();
                  if (decision == 0) {
                        break;
                  }
                  if (decision == 1) {
                        Functions varToCreate = new Functions();
                        Connection connToCreate = varToCreate.connect_to_DB(dbname, adminDatabaseUsername, databasePassword);
                        Statement stat = connToCreate.createStatement();
                        try {
                              stat.executeQuery("CREATE DATABASE " + dbname);
                        } catch (SQLException e) {
                        }
                        ;
                        System.out.println("|БД была создана|");
                        stat.close();
                        connToCreate.close();

                        conn = var.connect_to_DB(dbname, adminDatabaseUsername, databasePassword);
                        var.createTableFunction(conn, table);//Создание таблицы
                        CallableStatement cs1 = null;
                        cs1 = conn.prepareCall("SELECT createTable()");
                        cs1.executeQuery();
                        System.out.println("|Таблица создана|");
                        cs1.close();
                  }
                  if (decision == 2) {
                        var.insertValuesFunction(conn, table);//Заполнение таблицы значениями
                        CallableStatement cs2 = null;
                        System.out.println("|Добавление записи|" +
                                "Запись в таблице " + table + " содержит следующие значения >>\n" +
                                "|id|Имя|Город|Номер телефона|");
                        System.out.println("Введите id>>");
                        int id = input.nextInt();
                        System.out.println("Введите имя>>");
                        String name = input.next();
                        System.out.println("Введите город>>");
                        String city = input.next();
                        System.out.println("Введите номер>>");
                        String phone_num = input.next();
                        cs2 = conn.prepareCall("Select insertValues(" + Integer.toString(id) + "," +
                                " '" + name + "', '" + city + "', '" + phone_num + "');");
                        cs2.executeQuery();
                        System.out.println("|Данные были добавлены|");
                        cs2.close();
                  }
                  if (decision == 3) {
                        var.searchCityFunction(conn, table);
                        CallableStatement cs3 = null;
                        System.out.println("|Найти запись по Городу|\n" +
                                "Введите Город >>");
                        String city = input.next();
                        cs3 = conn.prepareCall("Select searchCity('" + city + "');");
                        ResultSet res1 = cs3.executeQuery();
                        System.out.println("|Результат поиска|");
                        while (res1.next()) {
                              System.out.println(res1.getString(1));
                        }
                        res1.close();
                        cs3.close();
                  }
                  if (decision == 4) {
                        var.updateTableFunction(conn, table);
                        CallableStatement cs4 = null;
                        System.out.println("|Обновление записи по id|" +
                                "Запись в таблице " + table + " содержит следующие значения >>\n" +
                                "|id|Имя|Город|Номер телефона|");
                        System.out.println("Введите id записи для обновления>>");
                        int id = input.nextInt();
                        System.out.println("Введите новое имя>>");
                        String name = input.next();
                        System.out.println("Введите новой город>>");
                        String city = input.next();
                        System.out.println("Введите новый номер>>");
                        String phone_num = input.next();
                        cs4 = conn.prepareCall("Select updateTable(" + Integer.toString(id) + "," +
                                " '" + name + "', '" + city + "', '" + phone_num + "');");
                        cs4.executeQuery();
                        cs4.close();
                  }
                  if (decision == 5) {
                        var.deleteByNameFunction(conn, table);
                        CallableStatement cs5 = null;
                        System.out.println("Введите имя для удаления совпадений>>");
                        String name = input.next();
                        cs5 = conn.prepareCall("Select deleteByName('" + name + "');");
                        cs5.executeQuery();
                        cs5.close();
                  }
                  if (decision == 6) {
                        var.clearTableFunction(conn, table);
                        CallableStatement cs6 = null;
                        cs6 = conn.prepareCall("Select clearTable();");
                        cs6.executeQuery();
                        System.out.println("|Таблица была очищена|");
                        cs6.close();
                  }
                  if (decision == 7) {
                        conn.close();
                        Functions varToDrop = new Functions();
                        Connection connToDrop = varToDrop.connect_to_DB(dbname, adminDatabaseUsername, databasePassword);
                        Statement stat = connToDrop.createStatement();
                        try {
                              stat.executeQuery("DROP DATABASE IF EXISTS " + dbname);
                        } catch (SQLException e) {
                        }
                        ;
                        System.out.println("|БД была удалена|");
                        stat.close();
                        connToDrop.close();
                  }
                  if (decision == 8) {
                        var.printTableFunction(conn);
                        CallableStatement cs8 = null;
                        cs8 = conn.prepareCall("Select printTable('" + table + "');");
                        ResultSet res1 = cs8.executeQuery();
                        System.out.println("|Результат|");
                        while (res1.next()) {
                              System.out.println(res1.getString(1));
                        }
                        res1.close();
                        cs8.close();
                  }
            }
      }
      public static void main(String[] args) throws SQLException {
            String table = "Students";
            String dbname = "lab2";
            String adminDatabaseUsername = "";
            String databasePassword = "";

            System.out.println("Для регистрации необходимо выбрать режим Доступа." +
                    "(1) - Администратор\n" +
                    "(2) - Гость");
            int reg = input.nextInt();
            if (reg == 1){
                  Functions var1 = new Functions();
                  Connection conn = var1.connect_to_DB(dbname, adminDatabaseUsername, databasePassword);
                  DBFunctions(conn,dbname,table);
            }
            if (reg == 2){
                  Functions var2 = new Functions();
                  Connection createUserConn = var2.connect_to_DB(dbname, adminDatabaseUsername, databasePassword);
                  var2.newReaderFunction(createUserConn);
                  var2.giveRightsFunction(createUserConn);
                  CallableStatement cs = null;
                  String reader_name = "reader";
                  String reader_pass = "1234";
                  cs = createUserConn.prepareCall("SELECT newReader('"+reader_name+"', '"+reader_pass+"');");
                  cs.executeQuery();
                  var2.printTableFunction(createUserConn);
                  var2.searchCityFunction(createUserConn,table);
                  cs = createUserConn.prepareCall("SELECT giveRights('"+reader_name+"');");
                  cs.executeQuery();
                  cs.close();
                  createUserConn.close();

                  Functions var3 = new Functions();
                  Connection conn = var3.connect_to_DB(dbname, reader_name, reader_pass);
                  DBFunctions(conn,dbname,table);
            }
      }
}