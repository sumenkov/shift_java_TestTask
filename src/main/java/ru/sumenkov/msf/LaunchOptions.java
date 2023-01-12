package ru.sumenkov.msf;

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
        optionGroup.addOption(new Option("s", "string",false, "обязательно: Сортировка строк"));
        optionGroup.addOption(new Option("i", "integer",false, "обязательно: Сортировка целых чисел"));

        Options options = new Options();
        options.addOptionGroup(optionGroup);
        options.addOption(Option.builder("a")
                .longOpt("ascending")
                .desc("опционально: Сортировка по возростанию")
                .build());
        options.addOption(Option.builder("d")
                .longOpt("descending")
                .desc("опционально: Сортировка по убыванию")
                .build());

        return options;
    }
}
