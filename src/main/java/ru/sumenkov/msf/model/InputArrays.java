package ru.sumenkov.msf.model;

import java.util.ArrayList;
import java.util.List;

public class InputArrays {
    private final List<int[]> inArraysI = new ArrayList<>();
    private final List<String[]> inArraysS = new ArrayList<>();

    public List<int[]> getInArraysI() {
        return inArraysI;
    }

    public List<String[]> getInArraysS() {
        return inArraysS;
    }

    @Override
    public String toString() {
        return "InputArrays{" +
                "inArraysI=" + inArraysI +
                ", inArraysS=" + inArraysS +
                '}';
    }
}
