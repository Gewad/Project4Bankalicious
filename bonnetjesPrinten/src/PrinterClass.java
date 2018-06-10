import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class PrinterClass {

    static String string;

    public PrinterClass(String fName, String lName, String aTM, String amount) {
        LocalDateTime date = LocalDateTime.now(ZoneId.of("GMT+2")); //(new ZoneId("ECT"));
        String[] dateTime = date.toString().split("T");
        String[] time = dateTime[1].split("\\.");
        string = "    klant: " + fName + " " + lName + "\n    EURO: " + amount + "\n    ATM: " + aTM + "\n    datum: " + dateTime[0] + "\n    tijd: " + time[0];

        PrinterJob pj = PrinterJob.getPrinterJob();

        PageFormat pf = pj.defaultPage();
        Paper paper = pf.getPaper();
        double width = toPPI(2.25);
        double height = toPPI(4);
        paper.setSize(width, height);
        paper.setImageableArea(
                toPPI(0.0),
                toPPI(0.0),
                width,
                height);
        pf.setOrientation(PageFormat.PORTRAIT);
        pf.setPaper(paper);
        dump(pf);
        PageFormat validatePage = pj.validatePage(pf);
        pj.setPrintable((Printable) new MyPrintable(), pf);
        try {

            pj.print();
        } catch (PrinterException ex) {
            ex.printStackTrace();
        }

    }

    protected static double toPPI(double inch) {
        return inch * 300d;
    }

    protected static String dump(Paper paper) {
        StringBuilder sb = new StringBuilder(64);
        sb.append(paper.getWidth()).append("x").append(paper.getHeight())
                .append("/").append(paper.getImageableX()).append("x").
                append(paper.getImageableY()).append(" - ").append(paper
                .getImageableWidth()).append("x").append(paper.getImageableHeight());
        return sb.toString();
    }

    protected static String dump(PageFormat pf) {
        Paper paper = pf.getPaper();
        return dump(paper);
    }


    public static class MyPrintable implements Printable {

        // maximum of 12 lines
        private void drawString(Graphics g, String text, int x, int y) {

            for (String line : text.split("\n")) {
                g.drawString(line, x, y += g.getFontMetrics().getHeight());
            }
        }

        private Image getPictrue() {


            BufferedImage picture;

            try

            {
                picture = ImageIO.read(new File("resource/image.jpg"));
            } catch (IOException e) {
                System.err.println("geen afbeeldig");
                picture = null;
            }
            return picture;
        }


        @Override
        public int print(Graphics graphics, PageFormat pageFormat,
                         int pageIndex) throws PrinterException {
            int result = NO_SUCH_PAGE;
            if (pageIndex < 1) {
                Graphics2D g2d = (Graphics2D) graphics;
                double width = pageFormat.getImageableWidth();
                double height = pageFormat.getImageableHeight();
                g2d.translate((int) pageFormat.getImageableX(),
                        (int) pageFormat.getImageableY());
                //g2d.draw(new Rectangle2D.Double(1, 1, width - 1, height - 1));

                FontMetrics fm = g2d.getFontMetrics();
                //g2d.setFont(new Font("TimesRoman", Font.PLAIN, 1));
                //drawString(g2d, getObama(), 0, fm.getAscent());

/*
                drawString(g2d, string, 0, fm.getAscent());

                g2d.drawString(string, 0, 0);
*/
                g2d.drawImage(getPictrue(), 20, 20, 60, 60, null);
                int tempfontsize = 2 * g2d.getFontMetrics().getHeight();
                g2d.setFont(new Font("TimesRoman", Font.PLAIN, 10));
                drawString(g2d, string, 0, 100);
                result = PAGE_EXISTS;
            }
            return result;

        }

    }


}
