package test;


import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class MatchMatrix {

    private final String param;
    String response;

    MatchMatrix(String param) {
        String matrixFile = "Матрица.txt";

        List<String> matrix = upData(matrixFile);
        List<String> file = upData(param);

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
            for (String mrow : matrix) {
                if (mrow == null) continue;
                String[] mcols = mrow.split("\t");

                BiFunction<String[], Integer, String> selectDb = (String[] db, Integer i) -> {
                    System.out.println("Detected!");

                    String[] d = db[0].split("\t");
                    String[] q = db[1].split("\t");

                    String[] x = Arrays.copyOf(q, 11);

                    x[0] = q[0];
                    x[1] = d[2];
                    x[2] = d[1];

                    if (!isNumeric(x[2])) {
                        try {
                            throw new MyException("В баркоде должно быть числовое значение!");
                        } catch (MyException e) {
                            e.printStackTrace();
                        }
                    }

                    Pattern pattern = Pattern.compile("\\d{1,2}.\\d{2}");
                    Matcher matcher = pattern.matcher(q[0]);
                    if (matcher.find()) {
                        x[3] = q[0];

                    } else {
                        x[3] = "0";
                    }

                    // todo this
                    if (q[0].contains(d[2])) {
                        x[1] = q[0];
                    }

                    x[3] = q[3];
                    x[4] = q[4];

                    x[5] = String.valueOf(i);

                    x[6] = d[0];
                    x[7] = d[3];
                    x[8] = d[4];
                    x[9] = d[5];
                    x[10] = d[6];

                    return MessageFormat.format
                            ("{0}\t{1}\t{2},\t{3}\t{4}\t{5}\t{6}\t{7}\t{8}\t{9}\t{10}",
                                    x[0], x[1], x[2], x[3], x[4], x[5], x[6], x[7], x[8], x[9], x[10]);
                };

                if (mcols[1].equals(key)) {
                    qparams[0] = mrow;
                    qparams[1] = frow;
                    orow = selectDb.apply(qparams, 2);
                } else if (mcols[2].equals(key)) {
                    qparams[0] = mrow;
                    qparams[1] = frow;
                    orow = selectDb.apply(qparams, 1);
                }
            }

            stringBuilder.append(orow);
            stringBuilder.append(System.lineSeparator());
        }

        try {
            toOutFile(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        response = states.OK.toString();
        this.param = response;
    }

    private void toOutFile(String outString)
            throws IOException {
            String fileName = "test.out";
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(outString);
        writer.close();
    }

    public String getParam() {
        return this.param;
    }

    enum states {
        OK,
        FAIL
    }

    private List<String> upData(String path) {
        List<String> strings = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            StringBuilder sb;
            sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                line = br.readLine();
                strings.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return strings;
    }

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    class MyException extends Exception {
        MyException(String msg) {
            super(msg);
        }
    }
}
