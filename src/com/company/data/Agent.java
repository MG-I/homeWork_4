package com.company.data;

import java.util.Objects;

public class Agent {

    private String name;
    private boolean busy;

    public Agent() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agent agent = (Agent) o;
        return busy == agent.busy && name.equals(agent.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, busy);
    }

    @Override
    public String toString() {
        return name;
    }
}
