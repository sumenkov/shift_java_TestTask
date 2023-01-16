package ru.sumenkov.msf.service;

import java.io.*;

public class SortCheckImpl implements SortCheck {

    @Override
    public boolean isSorted(File file, String sortDateType, String sortingDirection) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file.getName()));
        if (sortDateType.equals("i")) {

            Integer num1 = Integer.valueOf(br.readLine());
            Integer num2 = Integer.valueOf(br.readLine());

            if (sortingDirection.equals("a")) {
                while (true) {
                    if (num1 > num2)
                        return false;

                    num1 = num2;
                    String tmp = br.readLine();
                    if (tmp != null)
                        num2 = Integer.valueOf(tmp);
                    else break;
                }

            } else if (sortingDirection.equals("d")) {
                while (true) {
                    if (num1 < num2)
                        return false;

                    num1 = num2;
                    String tmp = br.readLine();
                    if (tmp != null)
                        num2 = Integer.valueOf(tmp);
                    else break;
                }
            }
        } else if (sortDateType.equals("s")) {
            String str1 = br.readLine();
            String str2 = br.readLine();

            if (sortingDirection.equals("a")) {
                while (str2 != null) {
                    if (str1.compareTo(str2) > 0)
                        return false;

                    str1 = str2;
                    str2 = br.readLine();
                }
            } else if (sortingDirection.equals("d")) {
                while (str2 != null) {
                    if (str1.compareTo(str2) < 0)
                        return false;

                    str1 = str2;
                    str2 = br.readLine();
                }
            }
        }
        br.close();
        return true;
    }
}
