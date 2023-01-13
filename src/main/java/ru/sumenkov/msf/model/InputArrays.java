package ru.sumenkov.msf.model;

import java.util.ArrayList;
import java.util.List;

public class InputArrays {
    private List<int[]> inArraysI = new ArrayList<>();
    private List<String[]> inArraysS = new ArrayList<>();

    public List<int[]> getInArraysI() {
        return inArraysI;
    }

    public void setInArraysI(List<int[]> inArraysI) {
        this.inArraysI = inArraysI;
    }

    public List<String[]> getInArraysS() {
        return inArraysS;
    }

    public void setInArraysS(List<String[]> inArraysS) {
        this.inArraysS = inArraysS;
    }

    @Override
    public String toString() {
        return "InputArrays{" +
                "inArraysI=" + inArraysI +
                ", inArraysS=" + inArraysS +
                '}';
    }
}
