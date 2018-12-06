package test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class MatchMatrix {

    private final long id;
    private final String param;

    static List<String> matrix;
    static List<String> file;
    private String mpath;
    private String fpath;
    String response;

    public MatchMatrix(long id, String param) {
        this.id = id;
        this.fpath = param;

        this.mpath = "Матрица.txt";

        matrix = readfile(this.mpath);
        MatchMatrix.file = readfile(this.fpath);

        if (matrixUpdates(MatchMatrix.file)) {
            response = states.OK.toString();
        } else {
            response = states.FAIL.toString();
        }

        this.param = null;
    }

    public String getContent() {
        return param;
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
            String updated = selectStringFromMatrix(row);
            sb.append(fileString);
            sb.append(System.lineSeparator());
        }

        System.out.println(sb);
        return false;
    }

    private String selectStringFromMatrix(String[] frow) {
        int fcoli = 0;
        for (String fcol : frow) {
            if (fcol.equals("")) {
                // todo Поиск значения в файле матрице делается либо по ШК либо по наименованию.
                  for (String matrixTable : matrix) {
                    String[] mrow = matrixTable.split("\t");
                        int mcoli = 0;
                        for (String mcol : mrow) {
                            if (mrow[1].equals(frow[2]) || frow[1].equals(mrow[3])) {
                                // todo Здесь нужно наполнять данными пустой выходной файл, но не ясно какой выбирать столбец

                                frow[fcoli] = mrow[mcoli];

                            }
                        }
                        mcoli++;
                        return mrow[0];
                }
            }
            fcoli++;
        }

            // todo В выходном файле должны быть все поля исходного файла и поля, полученные при сопоставлении (ART_ID, GR*). Также надо заполнить поле type_docking: если совпадение найдено по ШК - 1, по наименованию - 2, не найдено - 0

        return "";
    }

    private void appendToOutFile(String str) {
        System.out.println(str);
    }

    private void updateFileCallback() {}

    class MyExeption extends Exception {
    }
}
