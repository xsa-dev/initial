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

    public String getOstr() {
        return ostr;
    }

    public void setOstr(String ostr) {
        this.ostr = ostr;
    }

    private String ostr;

    MatchMatrix(String param) {
        String matrixFile = "Матрица.txt";

        List<String> matrix = upData(matrixFile);
        List<String> file = upData(param);

        StringBuilder ostr = new StringBuilder();
        ostr.append("real_name\tproduct_name\tbarcode\tprice_without_discount\tprice\taccomplishments\ttype_docking\tART_ID\tGR20\tGR21\tGR22\tGR23");
        ostr.append(System.lineSeparator());

        String oro = null;
        for (String fro : file) {
            // Для каждой строки файла будем формировать исходящую строку
            if (fro == null) continue;
            String[] fco = fro.split("\t");

            String key = fco[2];
            if (fco[2].equals("")) {
                key = fco[0];
            }

            String[] qpa = new String[2];
            for (String mro : matrix) {
                if (mro == null) continue;
                String[] mco = mro.split("\t");

                oro = null;
                if (mco[1].equals(key)) {
                    qpa[0] = mro;
                    qpa[1] = fro;
                    oro = selectDb.apply(qpa, 1);
                    if (oro != null) {
                        break;
                    }
                } else if (mco[2].equals(key)) {
                    qpa[0] = mro;
                    qpa[1] = fro;
                    oro = selectDb.apply(qpa, 2);
                    if (oro != null) {
                        break;
                    }
                }
            }
            if (oro == null) {
                qpa[0] = MessageFormat.format("{0}\t{1}\t{2}\t{3}\t{4}\t{5}\t{6}",
                        "-", "-", "-", "-", "-", "-", "-");
                qpa[1] = fro;
                oro = selectDb.apply(qpa, 0);
            }

            ostr.append(this.ostr);
            ostr.append(System.lineSeparator());
            oro = null;
        }

        try {
            toOutFile(ostr.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        response = states.OK.toString();
        this.param = response;
    }

    int sc = 0;
    BiFunction<String[], Integer, String> selectDb = (String[] db, Integer i) -> {
        sc++;
        System.out.println(sc + " generate out string: " + i);

        String[] d = db[0].split("\t");
        String[] q = db[1].split("\t");

        String[] x = Arrays.copyOf(q, 11);

        x[0] = q[0]; // real_name
        x[1] = d[2]; // product_name
        x[2] = d[0]; // barcode

        // bascorde is num
        if (isNumeric(x[2])) {
            x[2] = d[0];
        } else if (x[2].equals("-")) {
            System.out.println("Пустой баркод");
        } else {
            try {
                throw new MyException("Баркод не может быть не цифрой: " + x[2]);
            } catch (MyException e) {
                e.printStackTrace();
            }
        }

        // по умолчанию суммы:
        x[3] = q[3];
        x[4] = q[4];

        // в наименовании не должно быть цены, если такое случилось, перенести её в поле price_without_discont
        Pattern pattern = Pattern.compile("^[0-9]+([,.][0-9]{1,2})?$");
        Matcher matcher = pattern.matcher(x[0]);
        if (matcher.find()) {
            x[3] = q[0]; // price_without_discount
        } else {
            x[3] = q[3]; // price_without_discount
            x[4] = q[4]; // price
        }

        Double price = Double.valueOf(0);
        Double price_without_discount = Double.valueOf(0);

        // если ШК указан в наименовании
        if (x[0].contains(d[2]) && d[2] != "-" && x[2] != "-") {
            // Сверка цен:
            try {
                if (isNumeric(x[3])) {
                    price = Double.valueOf(x[3]);
                } else {
                    System.out.println("Значение не определено.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (x[2].equals("-")) {
                    System.out.println("Пропускаем пустую запись.");
                } else {
                    price_without_discount = Double.valueOf(x[2]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Double onepersent = price * 0.01;


            // если цены отличаются больше чем на один процент
            int retvalmin = Double.compare(price + onepersent, price_without_discount);
            int retvalmax = Double.compare(price - onepersent, price_without_discount);


            if (retvalmin < 1 && retvalmax > 0) {
                // то
                x[4] = "";
                x[5] = "0";
            }

            int first = 0;
            int second = 0;

            try {
                String[] prepPrice = x[4].split("\\.|,");
                try {
                     first = Integer.parseInt(prepPrice[0]);
                } catch (NumberFormatException e) {

                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            try {
                String[] prepPriceWithOutDiscount = x[5].split("\\.|,");
                try {
                     second = Integer.parseInt(prepPriceWithOutDiscount[0]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            if (first == second) {
                if (first < second) {
                    x[4] = String.valueOf(first);
                    x[5] = "";
                } else {
                    x[4] = String.valueOf(second);
                    x[5] = "";
                }
            }
        }


        x[5] = String.valueOf(i);

        x[6] = d[0];
        x[7] = d[3];
        x[8] = d[4];
        x[9] = d[5];
        x[10] = d[6];

        this.ostr = MessageFormat.format
                ("{0}\t{1}\t{2}\t{3}\t{4}\t{5}\t{6}\t{7}\t{8}\t{9}\t{10}",
                        x[0], x[1], x[2], x[3], x[4], x[5], x[6], x[7], x[8], x[9], x[10]);

        return MessageFormat.format
                ("{0}\t{1}\t{2}\t{3}\t{4}\t{5}\t{6}\t{7}\t{8}\t{9}\t{10}",
                        x[0], x[1], x[2], x[3], x[4], x[5], x[6], x[7], x[8], x[9], x[10]);
    };


    private void toOutFile(String outString)
            throws IOException {
        String fileName = "test.csv";
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
