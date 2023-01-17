package ru.sumenkov.msf.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArgsCheckImpl implements ArgsCheck {
    String[] args;
    public ArgsCheckImpl(String[] args) {
        this.args = args;
    }
    @Override
    public void check() {
        for (String arg: this.args) {
            List<String> list = new ArrayList<>(Arrays.asList("-i", "-s", "-a", "-d"));
            if (arg.contains("-")) {
                if (!list.contains(arg)) {
                    System.out.println("Неизвестный параметр запуска.");
                    Utility.helper();
                }
            }
        }

        int lengthArgs = this.args.length;
        if (lengthArgs < 3) Utility.helper();
        else if (Arrays.toString(this.args).contains("-i") && Arrays.toString(this.args).contains("-s")) Utility.helper();
        else if (Arrays.toString(this.args).contains("-a") && Arrays.toString(this.args).contains("-d")) Utility.helper();
        else if ((Arrays.toString(this.args).contains("-a") || Arrays.toString(this.args).contains("-d")) && lengthArgs < 4) {
            System.out.println("Не указан файл для записи или чтения.");
            Utility.helper();
        }
    }

    @Override
    public String sortDateType() {
        String sortDateType = null;
        if (Arrays.toString(this.args).contains("-i")) sortDateType = "i";
        else if (Arrays.toString(this.args).contains("-s")) sortDateType = "s";
        else Utility.helper();
        return sortDateType;
    }

    @Override
    public String sortingDirection() {
        return Arrays.toString(this.args).contains("-d") ? "d" : "a";
    }

    @Override
    public int startIndex() {
        return Arrays.toString(this.args).contains("-a") || Arrays.toString(this.args).contains("-d") ? 3 : 2;
    }
}
