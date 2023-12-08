package actions;

import lombok.val;
import structure.Enrollee;
import structure.Faculty;
import structure.FacultyOrEnrollee;
import structure.Subject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Optional;

public class FacultyOrEnrolleeActions {
    public static List<Faculty> getQualifiedFaculties(Enrollee enrollee, List<Faculty> faculties) {
        List<Faculty> qualifiedFaculties = new ArrayList<>();

        for (Faculty faculty : faculties) {
            if (isEnrolleeQualified(enrollee, faculty)) {
                qualifiedFaculties.add(faculty);
            }
        }

        return qualifiedFaculties;
    }
    public static boolean isEnrolleeQualified(Enrollee enrollee, Faculty faculty) {
        // Проверка на наличие баллов у Enrollee по Математике и Русскому
        int enrolleeMathPoints = getSubjectPoints(enrollee, "Математика");
        int enrolleeRussianPoints = getSubjectPoints(enrollee, "Русский язык");
        int facultyMathPoints = getSubjectPoints(faculty, "Математика");
        int facultyRussianPoints = getSubjectPoints(faculty, "Русский язык");

        int facultyOverallPoints = getSubjectPoints(faculty,"Проходной");
        int enrolleeOverallPoints = enrolleeRussianPoints+enrolleeMathPoints;

        if (enrolleeMathPoints < facultyMathPoints || enrolleeRussianPoints < facultyRussianPoints) {
            // У Enrollee нет баллов по Математике или Русскому, или они меньше минимальных
            return false;
        }

        // Проверка на наличие общих предметов между Enrollee и Faculty
        List<String> facultySubjectNames = getSubjectNames(faculty);
        List<String> enrolleeSubjectNames = getSubjectNames(enrollee);

        facultySubjectNames.removeIf(subject -> subject.equals("Русский язык") || subject.equals("Математика"));
        enrolleeSubjectNames.removeIf(subject -> subject.equals("Русский язык") || subject.equals("Математика"));
        enrolleeSubjectNames.removeIf(subject -> !facultySubjectNames.contains(subject));

        if (enrolleeSubjectNames.isEmpty())
            return false;

        for (String subjectName : enrolleeSubjectNames) {
            int facultyPoints = getSubjectPoints(faculty, subjectName);
            int enrolleePoints = getSubjectPoints(enrollee, subjectName);

            if (enrolleePoints >= facultyPoints && enrolleeOverallPoints+enrolleePoints>=facultyOverallPoints) {
                // У enrollee баллы не ниже, чем у faculty
                return true;
            }
        }
        return false;
    }
    public static List<? extends FacultyOrEnrollee> removeFacultyOrEnrollee(String name, List<? extends FacultyOrEnrollee> foeList) {
        foeList.removeIf(item -> item.getName().equals(name));
        return foeList;
    }

    public static List<FacultyOrEnrollee> addFacultyOrEnrollee(FacultyOrEnrollee facultyOrEnrollee, List<FacultyOrEnrollee> foeList) {
        Optional<FacultyOrEnrollee> existingItem = (Optional<FacultyOrEnrollee>) foeList.stream()
                .filter(item -> item.getName().equals(facultyOrEnrollee.getName()))
                .findFirst();

        if (existingItem.isPresent()) {
            // Заменяем существующий элемент
            int index = foeList.indexOf(existingItem.get());
            foeList.set(index, facultyOrEnrollee);
        } else {
            // Добавляем новый элемент
            foeList.add(facultyOrEnrollee);
        }
        return  foeList;
    }



    public static Enrollee newEnrollee(String newName) {
        List<Subject> subjects = new ArrayList<>();
        generateSubjects(subjects);
        Enrollee enrollee = Enrollee.builder().name(newName).subjectList(subjects).build();
        return enrollee;
    }
    public static Faculty newFaculty(String newName){
        List<Subject> subjects = new ArrayList<>();
        subjects.add(new Subject("Проходной",generateRandomScore(100,300)));
        generateSubjects(subjects);
        Faculty faculty =Faculty.builder().name(newName).subjectList(subjects).build();
        return faculty;
    }

    private static void generateSubjects(List<Subject> subjects) {
        subjects.add(new Subject("Математика",generateRandomScore(20,100)));
        subjects.add(new Subject("Русский язык",generateRandomScore(20,100)));
        List<String> remainingSubjects = new ArrayList<>(Objects.requireNonNull(Misc.loadSubjectOrder()));
        remainingSubjects.remove("Математика");
        remainingSubjects.remove("Русский язык");
        for (int i = 0; i < 2; i++) {
            String randomSubject = getRandomElement(remainingSubjects);
            subjects.add(new Subject(randomSubject, generateRandomScore(20,100)));
            remainingSubjects.remove(randomSubject);
        }
    }

    private static int getSubjectPoints(FacultyOrEnrollee facultyOrEnrollee, String subjectName) {
        return facultyOrEnrollee.getSubjectList().stream()
                .filter(subject -> Objects.equals(subject.name(), subjectName))
                .findFirst()
                .map(Subject::points)
                .orElse(-1); // Возвращаем -1, если предмет не найден
    }

    private static List<String> getSubjectNames(FacultyOrEnrollee facultyOrEnrollee) {
        return facultyOrEnrollee.getSubjectList().stream()
                .map(Subject::name)
                .toList();
    }
    private static int generateRandomScore(int min,int max) {
        // Генерация случайного балла от 20 до 100
        return new Random().nextInt(max-min+1) + min;
    }

    private static String getRandomElement(List<String> list) {
        // Получаем случайный элемент из списка
        val temp=list.get(new Random().nextInt(list.size()));
        return temp;
    }

}
