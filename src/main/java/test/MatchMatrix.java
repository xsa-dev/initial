package test;

import org.springframework.util.StringUtils;

import java.io.*;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MatchMatrix {

    private final long id;
    private final String param;

    static List<String> mfile;
    static List<String> file;
    private String mpath;
    private String fpath;
    String response;

    public MatchMatrix(long id, String param) {
        this.id = id;
        this.fpath = param;

        this.mpath = "Матрица.txt";

        mfile = readfile(this.mpath);
        file = readfile(this.fpath);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("real_name\tproduct_name\tbarcode\tprice_without_discount\tprice\taccomplishments\ttype_docking\tART_ID\tGR20\tGR21\tGR22\tGR23");
        stringBuilder.append(System.lineSeparator());

        String orow = null;
        for (String frow : file) {
            // Для каждой строки файла будем формировать исходящую строку
            if (frow == null) continue;
            String[] fcols = frow.split("\t");

            String key;
            key = fcols[2];
            if (fcols[2].equals("")) {
                key = fcols[0];
            }

            String[] qparams = new String[2];
            for (String mrow : mfile) {
                if (mrow == null) continue;
                String[] mcols = mrow.split("\t");
                if (mcols[1].equals(key)) {
                    qparams[0] = mrow;
                    qparams[1] = frow;
                    orow = selectDb.apply(qparams, 2);
                } else if (mcols[2].equals(key)) {
                    qparams[0] = mrow;
                    qparams[1] = frow;
                    orow = selectDb.apply(qparams, 1);
                } else {
                    continue;
                }
            }

            stringBuilder.append(orow);
            stringBuilder.append(System.lineSeparator());
        }

        try {
            toOutFile("test.out", stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        response = states.OK.toString();
        this.param = null;
    }

    // with multiple statements
    BiFunction<String[], Integer, String> selectDb = (String[] db, Integer i) -> {
        System.out.println("Detected!");

        String[] d = db[0].split("\t");
        String[] q = db[1].split("\t");

        String[] x = Arrays.copyOf(q, 11);

        x[0] = q[0]; // real_name
        x[1] = d[2]; // product_name
        x[2] = d[1]; // barcode

        if (!isNumeric(x[2])) {
            try {
                throw new MyException("В баркоде должен быть числовое значение!");
            } catch (MyException e) {
                e.printStackTrace();
            }
        }

        Pattern pattern = Pattern.compile("\\d{1,2}.\\d{2}");
        Matcher matcher = pattern.matcher(q[0]);
        if (matcher.find()){
            x[3] = q[0]; // price_without_discount
            // i think ....
            //x[0] = "";
        } else {
            x[3] = "0"; // price_without_discount
        }

        // todo this
        if (q[0].contains(d[2])) {
            x[1] = q[0];
            // i think ....
            //q[0] = "";
        }

        x[3] = q[3]; // price
        x[4] = q[4]; // accomplishments

        x[5] = String.valueOf(i);

        x[6] = d[0]; // ART_ID
        x[7] = d[3]; // GR20
        x[8] = d[4]; // GR21
        x[9] = d[5]; // GR22
        x[10] = d[6]; // GR24

        String str = MessageFormat.format
                ("{0}\t{1}\t{2},\t{3}\t{4}\t{5}\t{6}\t{7}\t{8}\t{9}\t{10}",
                        x[0], x[1], x[2], x[3], x[4], x[5], x[6], x[7], x[8], x[9], x[10]);

        return str;

    };

    public void toOutFile(String fileName, String outString)
            throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(outString);
        writer.close();
    }

    public String getParam() {
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

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
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

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private String selectStringFromMatrix(String[] frow) {
        return "";
    }

    private void updateFileCallback() {
    }

    class MyException extends Exception {
        public MyException(String msg) {
            super(msg);
        }
    }
}
