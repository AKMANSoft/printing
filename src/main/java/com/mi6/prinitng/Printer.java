package com.mi6.prinitng;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;

public class Printer {

	private final Logger log = LoggerFactory.getLogger(Printer.class);

	public static final String WATER_MARK = "src/main/resources/com/mi6/img/wm2.png";

	private String dest = "X.pdf";

	private Document doc;

	private float width = convertoPC(10.6f);

	private float height = convertoPC(19.6f);

	public Printer() {
		init();
	}

	public void init() {

		log.info("Initialize writer");
		PdfWriter writer = null;

		log.info("Initialize document");
		PdfDocument pdfDoc = null;

		try {
			writer = new PdfWriter(dest);
			pdfDoc = new PdfDocument(writer);
			//pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, new MyEventHandler());

		} catch (FileNotFoundException e) {
			log.error("Error init PDF Writer or Document ", e.getMessage());
			log.trace("", e.getMessage());
		}

		log.info("Setting Page Size of Width: " + width + " Height: " + height);
		doc = new Document(pdfDoc, new PageSize(width, height));

	}

	public void addX(Box box) {
		float x = box.getX();
		float y = height - box.getY();
		log.info("X: " + x + " Y: " + y);
		doc.add(new Paragraph("X").setFixedPosition(x, y, 8.5f));
	}

	public void close() {
		log.info("Finish Writing PDF Document. ");
		doc.close();
	}

	private static float convertoPC(float v) {
		return (float) (v * (72 / 2.54));
	}
	
	protected class MyEventHandler implements IEventHandler {

        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdfDoc = docEvent.getDocument();
            PdfPage page = docEvent.getPage();
            int pageNumber = pdfDoc.getPageNumber(page);
            Rectangle pageSize = page.getPageSize();
            PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);

            //Add watermark
            try {
				Image watermark = new Image(ImageDataFactory.create(WATER_MARK));
				Canvas canvas = new Canvas(pdfCanvas, page.getPageSize());
	            canvas.showTextAligned(new Paragraph("").add(watermark), 0, 0, TextAlignment.LEFT);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
            

            pdfCanvas.release();
        }
    }

}
