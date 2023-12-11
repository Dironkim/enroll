import actions.FacultyOrEnrolleeActions;
import actions.Misc;
import loaders.LoadCSV;
import loaders.LoadJSON;
import loaders.LoadXML;
import loaders.Loader;
import lombok.val;
import org.apache.commons.lang3.tuple.Pair;
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
import java.util.*;

import static actions.FacultyOrEnrolleeActions.getQualifiedFaculties;

public class ControlPanel {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<Enrollee> enrolleeList = null;
        List<Faculty> facultyList = null;
        String format=null;
        while (format==null) {
            System.out.println("Choose which format to load from:");
            System.out.println("1. Load from CSV");
            System.out.println("2. Load from JSON");
            System.out.println("3. Load from XML");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline
            switch (choice) {
                case 1:
                    format = "csv";
                    break;
                case 2:
                    format = "json";
                    break;
                case 3:
                    format = "xml";
                    break;
                case 4:
                    System.out.println("Exiting program. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 4.");
            }
        }
        val temp = loadData(format);
        enrolleeList=temp.getKey();
        facultyList = temp.getValue();
        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Add Enrollees");
            System.out.println("2. Add Faculties");
            System.out.println("3. Start Distribution");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    enrolleeList.addAll(readEnrolleeNamesFromConsole());
                    saveInfos(enrolleeList);
                    break;
                case 2:
                    facultyList.addAll(readFacultyNamesFromConsole());
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
    public static Map.Entry<List<Enrollee>, List<Faculty>> loadData(String format) {
        List<Enrollee> enrollees = new ArrayList<>();
        List<Faculty> faculties = new ArrayList<>();

        try {
            List<String> fileLines = Files.readAllLines(Path.of("src/main/resources/Enrollees."+format));
            enrollees.addAll(loadEnrolleesFromFile(fileLines, format));
        } catch (IOException e) {
            System.out.println("Error loading enrollees");
            return null;
        }

        try {
            List<String> fileLines = Files.readAllLines(Path.of("src/main/resources/Faculties."+format));
            faculties.addAll(loadFacultiesFromFile(fileLines, format));
        } catch (IOException e) {
            System.out.println("Error loading faculties");
            return null;
        }

        System.out.println("Data loaded successfully");
        return new AbstractMap.SimpleEntry<>(enrollees, faculties);
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

        return enrollees;
    }
    private static List<Enrollee> loadEnrolleesFromFile(List<String> fileLines, String format) {
        Loader loader = getLoaderByFormat(format);
        List<Enrollee> Enrollees = Misc.toChildEnrollee(loader.loadData("src/main/resources/Enrollees."+format));
        return Enrollees;
    }
    private static List<Faculty> loadFacultiesFromFile(List<String> fileLines, String format) {
        Loader loader = getLoaderByFormat(format);
        List<Faculty> faculties = Misc.toChildFaculty(loader.loadData("src/main/resources/Faculties."+format));
        return faculties;
    }
    public static List<Faculty> readFacultyNamesFromConsole() {
        List<Faculty> faculties = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
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

        return faculties;
    }
    private static Loader getLoaderByFormat(String format) {
        return switch (format.toLowerCase()) {
            case "csv" -> new LoadCSV();
            case "json" -> new LoadJSON();
            case "xml" -> new LoadXML();
            default -> throw new UnsupportedOperationException("Unsupported file format");
        };
    }
}
