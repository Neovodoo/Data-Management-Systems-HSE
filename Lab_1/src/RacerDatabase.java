import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;



public class RacerDatabase {

    public static HashMap<String, Racer> database;
    static Scanner sc = new Scanner(System.in);


    /**
     * The main method with which the user manipulates the racer database
     *
     *
     */

    public static void main(String args[]) throws IOException {


        System.out.println("Welcome to the manager of Racer Database!!!\n");


        try {
            System.out.println("Enter 1 to use backup file, Enter 0 to use a database file");
            int choice = Integer.parseInt(sc.nextLine());
            if (choice == 1) {
                FileInputStream file1 = new FileInputStream("backup.ser");

                ObjectInputStream inStream = new ObjectInputStream(file1);
                database = (HashMap<String, Racer>) inStream.readObject();
                inStream.close();
                System.out.println("backup.ser was found and loaded.\n");
            } else {
                FileInputStream file = new FileInputStream("database.ser");
                ObjectInputStream inStream = new ObjectInputStream(file);
                database = (HashMap<String, Racer>) inStream.readObject();
                inStream.close();
                System.out.println("database.ser was found and loaded.\n");
            }
        }
        catch(IOException | ClassNotFoundException e){
                //e.printStackTrace();
                database = new HashMap<>();
                System.out.println("No previous data found.\n");
            }


        String option = "";

        while(!option.equalsIgnoreCase("Q") && !option.equalsIgnoreCase("X")) {
            printMenu();
            System.out.print("\nPlease choose an option: ");
            option = sc.nextLine();

            if(option.equalsIgnoreCase("A"))
                add();
            else if(option.equalsIgnoreCase("L"))
                searchPrimaryKey();
            else if(option.equalsIgnoreCase("D"))
                delete();
            else if(option.equalsIgnoreCase("E"))
                edit();
            else if(option.equalsIgnoreCase("R"))
                backup();
            else if(option.equalsIgnoreCase("W"))
                maxSpeedPrint();
            else if(option.equalsIgnoreCase("M"))
                clearDatabase();
        }

        if(option.equalsIgnoreCase("X")) {
            try {
                FileOutputStream file = new FileOutputStream("database.ser");
                ObjectOutputStream outStream = new ObjectOutputStream(file);
                outStream.writeObject(database);
                outStream.close();
                System.out.println("File saved to database.ser");
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                FileOutputStream file = new FileOutputStream("database.ser");
                ObjectOutputStream outStream = new ObjectOutputStream(file);
                database = new HashMap<>();
                outStream.writeObject(database);
                outStream.close();
                File database = new File("/Users/daniil/IdeaProjects/FileDatabase/database.ser");
                database.delete();
                System.out.println("Database has been deleted");
                System.out.println("Goodbye, have a nice day!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //Method for date formate
    public static boolean validDate(String date) {
        char c = ' ';
        for(int i = 0; i < date.length(); i++) {
            c = date.charAt(i);
            if(i < 4 && (c < '0' || c > '9'))
                return false;
            if((c < '0' || c > '9') && c != '-')
                return false;
        }
        return true;
    }


    /**
     * Adds a racer to the database
     */
    public static void add() {
        System.out.print("Please enter name: ");
        String name = sc.nextLine();
        name = name.toUpperCase();

        System.out.print("Please enter date: ");
        String date = sc.nextLine();
        if(!validDate(date)) {
            System.out.println("Invalid date format\n");
            return;
        }

        System.out.print("Please enter position of racer: ");
        int position = sc.nextInt();
        if(position < 0) {
            System.out.println("Invalid position\n");
            return;
        }

        System.out.print("Please enter max speed of racer (km/h): ");
        double maxSpeed = sc.nextDouble();
        if(maxSpeed < 0) {
            System.out.println("Invalid max speed of racer\n");
            return;
        }

        Racer s = new Racer (name, date, position, maxSpeed);
        database.put(name, s);
        System.out.println(s.getName() + " added.\n");
    }



    /**
     * Deletes a racer from the database
     */
    public static void delete() {
        System.out.print("Please enter name: ");
        String name = sc.nextLine();
        name = name.toUpperCase();
        if(database.containsKey(name)) {
            database.remove(name);
            System.out.println("Racer " + name + " has been deleted.\n");
        }
        else
            System.out.println("Racer " + name + " does not exist.\n");
    }

    public static void edit() {
        System.out.print("Please enter name: ");
        String name = sc.nextLine();
        name = name.toUpperCase();
        if(!database.containsKey(name)) {
            System.out.println("Racer " + name + " does not exist.\n");
            return;
        }
        System.out.println("In Edit Mode, press enter without any input to leave data unchanged.");
        System.out.print("Please enter date: ");
        String date = sc.nextLine();
        if(!validDate(date)) {
            System.out.println("Invalid date format\n");
            return;
        }
        System.out.print("Please enter position: ");
        String position = sc.nextLine();
        if(Double.parseDouble(position) < 0) {
            System.out.println("Invalid precipitation\n");
            return;
        }
        System.out.print("Please enter max speed (km/h): ");
        String maxSpeed = sc.nextLine();
        if(Double.parseDouble(maxSpeed) < 0) {
            System.out.println("Invalid max speed\n");
            return;
        }
        if(date != "")
            database.get(name).setDate(date);
        if(position != "")
            database.get(name).setPosition(Integer.parseInt(position));
        if(maxSpeed != "")
            database.get(name).setMaxSpeed(Double.parseDouble(maxSpeed));
        System.out.println(name + " added.\n");
    }

    /**
     * Allows the user to view the details of one specific racer from the database
     */
    public static void searchPrimaryKey() {
        System.out.print("Please enter name: ");
        String name = sc.nextLine();
        name = name.toUpperCase();
        if(database.containsKey(name))
            System.out.println("\n" + database.get(name) + "\n");
        else
            System.out.println("Key not found.\n");
    }

    public static void searchData() {
        System.out.print("Please enter data: ");
        String date = sc.nextLine();
        if(!validDate(date)) {
            System.out.println("Invalid date format\n");
            return;
        }
        if(database.containsKey(date))
            System.out.println("\n" + database.get(date) + "\n");
        else
            System.out.println("Key not found.\n");
    }

    /**
     * Prints the racers database with the racer ordered by max speed
     */
    public static void maxSpeedPrint() {
        System.out.printf("%s%20s%15s%15s\n", "Name", "Maxspeed", "Position", "Date");
        System.out.println("-----------------------------------------------------------");
        Collection<Racer> values = database.values();
        ArrayList<Racer> stormValues = new ArrayList<Racer>(values);
        Collections.sort(stormValues, new SpeedComparator());
        for(int i = 0; i < stormValues.size(); i++)
            stormValues.get(i).print();
    }

    public static void clearDatabase() throws IOException {
        FileOutputStream file = new FileOutputStream("database.ser");
        ObjectOutputStream outStream = new ObjectOutputStream(file);
        database = new HashMap<>();
        outStream.writeObject(database);
        outStream.close();
    }


    public static void backup() throws IOException {
        FileOutputStream file = new FileOutputStream("backup.ser");
        ObjectOutputStream outStream = new ObjectOutputStream(file);
        outStream.writeObject(database);
        outStream.close();
        System.out.println("File saved to backup.ser");
    }

    public static void printMenu() {
        System.out.println("Menu:"
                + "\t\nA) Add A Racer"
                + "\t\nL) Search A Racer"
                + "\t\nD) Delete A Racer"
                + "\t\nE) Edit Racer"
                + "\t\nR) Backup"
                + "\t\nW) Print Racers by max speed (by min to max)"
                + "\t\nM) Clear database"
                + "\t\nX) Save and quit"
                + "\t\nQ) Quit and delete saved data");
    }

















}
