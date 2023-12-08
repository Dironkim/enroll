import actions.FacultyOrEnrolleeActions;
import actions.Misc;
import loaders.LoadCSV;
import savers.SaveLetterPDF;
import savers.SaveLetterTXT;
import savers.info.SaveInfoCSV;
import savers.info.SaveInfoJSON;
import savers.info.SaveInfoXML;
import structure.Enrollee;
import structure.Faculty;
import structure.FacultyOrEnrollee;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static actions.FacultyOrEnrolleeActions.getQualifiedFaculties;

public class ControlPanel {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<Enrollee> enrolleeList = null;
        List<Faculty> facultyList = null;

        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Add Enrollees");
            System.out.println("2. Add Faculties");
            System.out.println("3. Start Distribution (empty for now)");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    enrolleeList = readEnrolleeNamesFromConsole();
                    saveInfos(enrolleeList);
                    break;
                case 2:
                    facultyList = readFacultyNamesFromConsole();
                    saveInfos(facultyList);
                    break;
                case 3:
                    startDistribution(enrolleeList, facultyList);
                    break;
                case 4:
                    System.out.println("Exiting program. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 4.");
            }
        }
    }

    public static void startDistribution(List<Enrollee> enrollees, List<Faculty> faculties) {
        for (Enrollee enrollee : enrollees) {
            List<Faculty> qualifiedFaculties = getQualifiedFaculties(enrollee, faculties);
            // Сохраняем письма для абитуриента в различные форматы
            SaveLetterPDF.saveLetter(enrollee, qualifiedFaculties);
            SaveLetterTXT.saveLetter(enrollee, qualifiedFaculties);
        }
    }
    public static void saveInfos(List<? extends FacultyOrEnrollee> data) {
        SaveInfoCSV saverCSV = new SaveInfoCSV();
        saverCSV.saveData("src/main/resources/" + (data.get(0) instanceof Enrollee ? "Enrollees.csv" : "Faculties.csv"), data);
        SaveInfoJSON saverJSON = new SaveInfoJSON();
        saverJSON.saveData("src/main/resources/" + (data.get(0) instanceof Enrollee ? "Enrollees.json" : "Faculties.json"), data);
        SaveInfoXML saverXML = new SaveInfoXML();
        saverXML.saveData("src/main/resources/" + (data.get(0) instanceof Enrollee ? "Enrollees.xml" : "Faculties.xml"), data);

    }
    public static List<Enrollee> readEnrolleeNamesFromConsole() {
        List<Enrollee> enrollees = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        try {
            // Загружаем данные из файла
            List<String> fileLines = Files.readAllLines(Path.of("src/main/resources/Enrollees.csv"));
            // Реализуйте логику загрузки данных из файла и создания объектов FacultyOrEnrollee

            // Добавляем данные из файла в список
            enrollees.addAll(loadEnrolleesFromFile(fileLines));

            System.out.println("Data loaded from file: " + enrollees);

            System.out.println("Enter enrollee names from console (type 'exit' to finish):");

            while (true) {
                System.out.print("Enter enrollee name: ");
                String input = scanner.nextLine();

                if ("exit".equalsIgnoreCase(input.trim())) {
                    break;
                }

                // Создаем нового абитуриента и добавляем в список
                Enrollee enrollee = FacultyOrEnrolleeActions.newEnrollee(input);
                enrollees= Misc.toChildEnrollee( FacultyOrEnrolleeActions.addFacultyOrEnrollee(enrollee, Misc.toAncestor(enrollees)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return enrollees;
    }
    private static List<Enrollee> loadEnrolleesFromFile(List<String> fileLines) {
        LoadCSV loader = new LoadCSV();
        List<Enrollee> Enrollees = Misc.toChildEnrollee(loader.loadFoEsFromCSV("src/main/resources/Enrollees.csv"));
        return Enrollees;
    }
    private static List<Faculty> loadFacultiesFromFile(List<String> fileLines) {
        LoadCSV loader = new LoadCSV();
        List<Faculty> faculties = Misc.toChildFaculty(loader.loadFoEsFromCSV("src/main/resources/Faculties.csv"));
        return faculties;
    }
    public static List<Faculty> readFacultyNamesFromConsole() {
        List<Faculty> faculties = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        try {
            List<String> fileLines = Files.readAllLines(Path.of("src/main/resources/Enrollees.csv"));
            faculties.addAll(loadFacultiesFromFile(fileLines));

            System.out.println("Data loaded from file: " + faculties);

            System.out.println("Enter faculty names from console (type 'exit' to finish):");

            while (true) {
                System.out.print("Enter faculty name: ");
                String input = scanner.nextLine();

                if ("exit".equalsIgnoreCase(input.trim())) {
                    break;
                }

                // Создаем нового абитуриента и добавляем в список
                Faculty faculty = FacultyOrEnrolleeActions.newFaculty(input);
                faculties= Misc.toChildFaculty( FacultyOrEnrolleeActions
                        .addFacultyOrEnrollee(faculty, Misc.toAncestor(faculties)));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return faculties;
    }
}
