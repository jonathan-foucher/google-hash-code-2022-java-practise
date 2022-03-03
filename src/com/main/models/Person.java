package com.main.models;

import java.util.List;
import java.util.Objects;

public class Person {
    private String name;
    private List<Skill> skills;
    private Boolean isAvailable;

    public Person(String name, List<Skill> skills) {
        this.name = name;
        this.skills = skills;
        this.isAvailable = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public Boolean hasSkill(String skillName, Integer requiredLevel) {
        return skills.stream().anyMatch(skill -> Objects.equals(skill.getName(), skillName) && skill.getLevel() >= requiredLevel);
    }
}
