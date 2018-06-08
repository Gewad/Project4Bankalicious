import java.awt.*;
import java.awt.print.*;

public class TestPrinting01 {

    public static void main(String[] args) {

        PrinterJob pj = PrinterJob.getPrinterJob();
        if (pj.printDialog()) {
            PageFormat pf = pj.defaultPage();
            Paper paper = pf.getPaper();
            double width = 2.25 * 600d;
            double height =  4* 300d;
            double margin = 0;
            paper.setSize(width, height);
            paper.setImageableArea(
                    margin,
                    margin,
                    width - (margin * 2),
                    height - (margin * 2));
            System.out.println("Before- " + dump(paper));
            pf.setOrientation(PageFormat.LANDSCAPE);
            pf.setPaper(paper);
            System.out.println("After- " + dump(paper));
            System.out.println("After- " + dump(pf));
            dump(pf);
            PageFormat validatePage = pj.validatePage(pf);
            System.out.println("Valid- " + dump(validatePage));

            Book pBook = new Book();
            pBook.append(new Page(), pf);
            pj.setPageable(pBook);

            try {
                pj.print();
            } catch (PrinterException ex) {
                ex.printStackTrace();
            }
        }
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

    public static class Page implements Printable {

        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
            if (pageIndex >= 1) {
                return Printable.NO_SUCH_PAGE;
            }

            Graphics2D g2d = (Graphics2D) graphics;
            // Be careful of clips...
            g2d.translate((int) pageFormat.getImageableX(), (int) pageFormat.getImageableY());

            double width = pageFormat.getImageableWidth();
            double height = pageFormat.getImageableHeight();

            g2d.drawRect(0, 0, (int)pageFormat.getImageableWidth() - 1, (int)pageFormat.getImageableHeight() - 1);
            FontMetrics fm = g2d.getFontMetrics();
            String text = "top";
            g2d.drawString(text, 0, fm.getAscent());

            text = "bottom";
            double x = width - fm.stringWidth(text);
            double y = (height - fm.getHeight()) + fm.getAscent();
            g2d.drawString(text, (int)x, (int)y);

            return Printable.PAGE_EXISTS;
        }
    }
}