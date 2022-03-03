package com.main.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Project {
    private String name;
    private Integer duration;
    private Integer score;
    private Integer bestBefore;
    private List<Skill> roles;
    private List<Person> contributors;
    private Integer progression = 0;
    private Integer orderId = 0;
    private Boolean isInProgress = false;

    public Project(String name, Integer duration, Integer score, Integer bestBefore, List<Skill> roles) {
        this.name = name;
        this.duration = duration;
        this.score = score;
        this.bestBefore = bestBefore;
        this.roles = roles;
        this.contributors = new ArrayList<>();
        this.progression = 0;
        this.orderId = 0;
        this.isInProgress = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getBestBefore() {
        return bestBefore;
    }

    public void setBestBefore(Integer bestBefore) {
        this.bestBefore = bestBefore;
    }

    public List<Skill> getRoles() {
        return roles;
    }

    public void setRoles(List<Skill> roles) {
        this.roles = roles;
    }

    public List<Person> getContributors() {
        return contributors;
    }

    public void setContributors(List<Person> contributors) {
        this.contributors = contributors;
    }

    public Integer getProgression() {
        return progression;
    }

    public void setProgression(Integer progression) {
        this.progression = progression;
    }

    public void incrementProgression() {
        progression++;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Boolean getInProgress() {
        return isInProgress;
    }

    public void setInProgress(Boolean inProgress) {
        this.isInProgress = inProgress;
    }

    public Boolean isFinished() {
        return Objects.equals(progression, duration);
    }

    public int calculateActualScore(int actualDay) {
        int scorePenality = actualDay + duration - bestBefore;
        int actualScore = scorePenality > 0 ? score - scorePenality : score;
        return Math.max(actualScore, 0);
    }

    public int getCompletedScore(int actualDay) {
        int scorePenality = actualDay - bestBefore;
        int actualScore = scorePenality > 0 ? score - scorePenality : score;
        return Math.max(actualScore, 0);
    }
}
