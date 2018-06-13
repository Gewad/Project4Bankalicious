import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class PrinterClass {

    private static String string;

    public PrinterClass(String fName, String lName, String aTM, String amount) {
        LocalDateTime date = LocalDateTime.now(ZoneId.of("GMT+2"));
        String[] dateTime = date.toString().split("T");
        String[] time = dateTime[1].split("\\.");
        string = "       De Obama.cf bank\n            Powered by: \n    Wondel en de Hekkies\n    wondelendehekkies.cf\n \n" +
                "klant: " + fName + " " + lName + "\nEURO: " + amount + "\nATM: " + aTM + "\ndatum: " + dateTime[0] + "\ntijd: " + time[0] + "\n \n ";

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

        pj.setPrintable(new MyPrintable(), pf);
        try {

            pj.print();
        } catch (PrinterException ex) {
            ex.printStackTrace();
        }

    }

    private static double toPPI(double inch) {
        return inch * 300d;
    }


    public static class MyPrintable implements Printable {

        private void drawString(Graphics g, String text, int x, int y) {

            for (String line : text.split("\n")) {
                g.drawString(line, x, y += g.getFontMetrics().getHeight());
            }
        }

        private Image getPicture() {

            BufferedImage picture;
            try {
                picture = ImageIO.read(new File("resource/image.jpg"));
            } catch (IOException e) {
                System.err.println("geen afbeeldig");
                picture = null;
            }
            return picture;
        }

        @Override
        public int print(Graphics graphics, PageFormat pageFormat,
                         int pageIndex) {
            int result = NO_SUCH_PAGE;
            if (pageIndex < 1) {
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate((int) pageFormat.getImageableX(),
                        (int) pageFormat.getImageableY());
                g2d.drawImage(getPicture(), 52, 20, 60, 60, null);
                g2d.setFont(new Font("TimesRoman", Font.PLAIN, 10));
                drawString(g2d, string, 10, 80);
                result = PAGE_EXISTS;
            }
            return result;

        }

    }


}