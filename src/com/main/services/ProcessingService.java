package com.main.services;

import com.main.Main;
import com.main.models.Person;
import com.main.models.Project;
import com.main.models.Skill;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public abstract class ProcessingService {
    private static int score = 0;
    private static List<Person> availablePersons;
    private static List<Project> projects;
    private static int actualDay = 0;
    private static int projectOrderId = 1;

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static List<String> process(List<String> data) {
        reset();
        dataToObjects(data);
        simulate();
        LOGGER.info("score : " + String.valueOf(score));
        return formatResults();
    }

    private static void dataToObjects(List<String> data) {
        List<String> firstLine = List.of(data.get(0).split(" "));
        int numberOfContributors = parseInt(firstLine.get(0));
        int numberOfProjects = parseInt(firstLine.get(1));

        int actualRow = 0;
        for (int contributor = 0; contributor < numberOfContributors; contributor++) {
            actualRow++;
            List<String> personLine = List.of(data.get(actualRow).split(" "));
            int personNbOfSkills = parseInt(personLine.get(1));
            List<Skill> personSkills = new ArrayList<>();
            actualRow = extractSkills(data, actualRow, personNbOfSkills, personSkills);
            availablePersons.add(new Person(personLine.get(0), personSkills));
        }

        for (int project = 0; project < numberOfProjects; project++) {
            actualRow++;
            List<String> projectLine = List.of(data.get(actualRow).split(" "));
            List<Skill> roles = new ArrayList<>();
            int projectNbOfRole = parseInt(projectLine.get(4));
            actualRow = extractSkills(data, actualRow, projectNbOfRole, roles);
            projects.add(new Project(projectLine.get(0), parseInt(projectLine.get(1)), parseInt(projectLine.get(2)), parseInt(projectLine.get(3)), roles));
        }
    }

    private static int extractSkills(List<String> data, int actualRow, int personNbOfSkills, List<Skill> personSkills) {
        for (int skill = 0; skill < personNbOfSkills; skill++) {
            actualRow++;
            List<String> skillLine = List.of(data.get(actualRow).split(" "));
            personSkills.add(new Skill(skillLine.get(0), parseInt(skillLine.get(1))));
        }
        return actualRow;
    }

    private static void simulate() {
        do {
            projects.stream()
                    .filter(Project::getInProgress)
                    .forEach(project -> {
                        project.incrementProgression();
                        if (project.isFinished()) {
                            project.setInProgress(false);
                            score += project.getCompletedScore(actualDay);

                            AtomicInteger contributorIndex = new AtomicInteger(0);
                            project.getContributors()
                                    .forEach(contributor -> {
                                                availablePersons.add(contributor);
                                                contributor.setAvailable(Boolean.TRUE);
                                                Skill role = project.getRoles().get(contributorIndex.getAndIncrement());
                                                Skill skill = contributor.getSkills()
                                                        .stream()
                                                        .filter(s -> Objects.equals(s.getName(), role.getName()))
                                                        .findFirst()
                                                        .get();
                                                if (role.getLevel() >= skill.getLevel()) {
                                                    skill.incrementLevel();
                                                }
                                            }
                                    );
                        }
                    });

            projects.forEach(project -> {
                        if (project.getContributors().isEmpty() && project.calculateActualScore(actualDay) > 0) {
                            List<Person> availablePersonsCopy = new ArrayList<>(availablePersons);
                            boolean isDoable = project.getRoles().stream().allMatch(role -> {
                                int i = 0;
                                while (i < availablePersonsCopy.size()) {
                                    if (availablePersonsCopy.get(i).hasSkill(role.getName(), role.getLevel())) {
                                        break;
                                    }
                                    i++;
                                }

                                if (i < availablePersonsCopy.size()) {
                                    availablePersonsCopy.remove(i);
                                    return true;
                                }
                                return false;
                            });

                            if (isDoable) {
                                project.getRoles().forEach(role -> {
                                    int i = 0;
                                    while (i < availablePersons.size()) {
                                        if (availablePersons.get(i).hasSkill(role.getName(), role.getLevel())) {
                                            break;
                                        }
                                        i++;
                                    }

                                    Person contributor = availablePersons.remove(i);
                                    contributor.setAvailable(Boolean.FALSE);
                                    project.getContributors().add(contributor);
                                });

                                project.setInProgress(Boolean.TRUE);
                                project.setOrderId(projectOrderId++);
                            }
                        }
                    }
            );

            actualDay++;
            if (projects.stream().noneMatch(project ->
                    (project.getContributors().isEmpty() && project.calculateActualScore(actualDay) > 0) || project.getInProgress())) {
                break;
            }
        } while (true);
    }

    private static List<String> formatResults() {
        List<Project> plannedProjects = new ArrayList<>(projects.stream()
                .filter(project -> project.getContributors().size() > 0)
                .toList());
        plannedProjects.sort(Comparator.comparingInt(Project::getOrderId));

        List<String> results = new ArrayList<>();
        results.add(String.valueOf(plannedProjects.size()));
        plannedProjects.forEach(project -> {
            results.add(project.getName());

            String resultLine = project.getContributors()
                    .stream()
                    .map(Person::getName)
                    .collect(Collectors.joining(" "));
            results.add(resultLine);
        });
        return results;
    }

    private static void reset() {
        score = 0;
        availablePersons = new ArrayList<>();
        projects = new ArrayList<>();
        actualDay = 0;
        projectOrderId = 1;
    }
}
