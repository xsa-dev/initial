package test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class matchMatrix {

    private final long id;
    private final String content;
    static List<String> matrix;
    static List<String> file;
    private String mpath;
    private String fpath;
    String response;

    public matchMatrix(long id, String content) {
        this.id = id;
        this.content = content;

        mpath = "Матрица.txt";
        fpath = "тест.txt";

        matrix = readfile(this.mpath);
        file = readfile(this.fpath);

        if (matrixUpdates(file)) {
            response = states.OK.toString();
        } else {
            response = states.FAIL.toString();
        }

    }

    public String getContent() {
        return content;
    }

    public String getResponse() {
        return "OK";
    }

    enum states {
        OK,
        FAIL
    }

    private List<String> readfile(String path) {
        List<String> strings = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(path))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                line = br.readLine();
                strings.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return strings;
    }

    private boolean matrixUpdates(List<String> file) {
        // todo здесь мы будем выполнять условия обновления файла
        /*
        Поля входного файла:
            0 real_name - необработанное название товара
            1 product_name - обработанное название товара
            2 barcode - штрих-код товара (ШК)
            3 price_without_discount - цена
            4 price - цена со скидкой
            5 accomplishments - акционность
            type_docking - результат поиска соответствия
         */

        int i = 0;
        StringBuilder sb = new StringBuilder();

        for (String fileString : file) {
            if (fileString == null) {
                continue;
            }

            String[] row = fileString.split("\t");

            // здесь каждая строка
            for (String column : row) {
                // тут уже каждая колонка
            }

            sb.append(fileString);
            sb.append(System.lineSeparator());
        }

        System.out.println(sb);
        return false;
    }

    private void selectStringFromMatrix() {}

    private void appendToOutFile(String str) {
        System.out.println(str);
    }

    private void updateFileCallback() {}
}
