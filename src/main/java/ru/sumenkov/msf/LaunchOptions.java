package ru.sumenkov.msf;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;

public class LaunchOptions {
    /**
     * Обрабатываем аргументы запуска программы
     *
     * @return параметры запуска
     */
    public Options launchOptions() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(new Option("s",false, "обязательно: Сортировка строк"));
        optionGroup.addOption(new Option("i",false, "обязательно: Сортировка целых чисел"));

        Options options = new Options();
        options.addOptionGroup(optionGroup);
        options.addOption(Option.builder("a")
                .desc("опционально: Сортировка по возростанию")
                .build());
        options.addOption(Option.builder("d")
                .desc("опционально: Сортировка по убыванию")
                .build());

        return options;
    }

    public static void helper(Options options){
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("MergeSortFiles", options, true);
        System.exit(0);
    }
}
