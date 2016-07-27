package com.rui.liang.aspose;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.aspose.slides.AutoShape;
import com.aspose.slides.BulletType;
import com.aspose.slides.FillType;
import com.aspose.slides.FontData;
import com.aspose.slides.IAutoShape;
import com.aspose.slides.IDocumentProperties;
import com.aspose.slides.IShapeCollection;
import com.aspose.slides.ISlide;
import com.aspose.slides.ITextFrame;
import com.aspose.slides.NullableBool;
import com.aspose.slides.NumberedBulletStyle;
import com.aspose.slides.Paragraph;
import com.aspose.slides.ParagraphCollection;
import com.aspose.slides.Portion;
import com.aspose.slides.PortionCollection;
import com.aspose.slides.PortionFormat;
import com.aspose.slides.Presentation;
import com.aspose.slides.Shape;
import com.aspose.slides.ShapeType;
import com.aspose.slides.Slide;
import com.aspose.slides.TextAlignment;
import com.aspose.slides.TextAutofitType;
import com.aspose.slides.TextFrame;

/**
 * TODO: Provide a class description.
 *
 * @author liangrui
 */
public class AsposeSlideUtil {
    
    /**
     * The minimum font size allowed.
     */
    private static final short MINIMUM_FONT = 7;

    public static Portion setText(Shape shape, String text, boolean fitTextToShape, int alignment) {
        //Accessing the text frame
        ITextFrame txtFrame =  ((AutoShape)shape).getTextFrame();
        
        Paragraph paragraph = clear(getParagraphs(shape));
        if (alignment != TextAlignment.NotDefined) {
            paragraph.getParagraphFormat().setAlignment(alignment);
        }
        
        Portion portion = (Portion) paragraph.getPortions().get_Item(0);
        // Aspose doesn't like \r\n in text. It throws an exception.
        // com.aspose.slides.PptEditException: Can't assign string which contains paragraph break character
        String initialText = text == null ? "" : text.replaceAll("\r?\n"," ");
        
        if (isEmpty(text) || !fitTextToShape) {
            portion.setText(initialText);
            return portion;
        }
        
        setFitTextToShape(shape, txtFrame, portion, initialText);
        return portion;
    }

    /**
     * Make the text content auto fit as per the size of the TextFrame of a AutoShape
     */
    public static void setFitTextToShape(Shape shape, ITextFrame txtFrame, Portion portion, String initialText) {
        // The Aspose TextAutofitType.Normal can only work on pptx format, we have to handle the plain text fonts by ourself.
        // Once the issue fixed, we should using txtFrame.getTextFrameFormat().setAutofitType(TextAutofitType.Normal)
        int initialY = (int) shape.getY();
        int initialHeight = getHeight(shape);
        int start = 0;
        int end = initialText.length();
        int nextPoint = (start + end) / 2;
        String nextText = "";
        
        boolean needReduce = false;
        while (nextPoint != end) {
            txtFrame.getTextFrameFormat().setAutofitType(TextAutofitType.Shape);
            needReduce = false;
            nextText = initialText.substring(0, nextPoint);
            portion.setText(nextText);

            if (getHeight(shape) > initialHeight) {
                needReduce = reduceFonts(portion);
                txtFrame.getTextFrameFormat().setAutofitType(TextAutofitType.Normal);
                setHeight(shape, initialHeight);
                shape.setY(initialY);
                if (!needReduce) {
                    break;
                }
            } else {
                start = nextPoint;
                nextPoint = (int) (Math.ceil((start + end) / 2.0f) > end ? end : Math.ceil((start + end) / 2.0f));
            }
        }
        
        portion.setText(initialText);
        
        txtFrame.getTextFrameFormat().setAutofitType(TextAutofitType.Normal);
        setHeight(shape, initialHeight);
        shape.setY(initialY);
    }
    
    /**
     * Reduce the fonts size of portion.
     */
    public static boolean reduceFonts(Portion portion) {
        boolean canShrinkMore = false;
        short fontSize = (short) (Float.isNaN(portion.getPortionFormat().getFontHeight())
                ? portion.createPortionFormatEffective().getFontHeight() : portion.getPortionFormat().getFontHeight());
        if (fontSize > MINIMUM_FONT) {
            fontSize -= 4;
            if (fontSize <= MINIMUM_FONT) {
                fontSize = MINIMUM_FONT;
            } else if (fontSize > MINIMUM_FONT) {
                canShrinkMore = true;
            }

            portion.getPortionFormat().setFontHeight(fontSize);
        }
        return canShrinkMore;
    }
    
    public static Paragraph clear(ParagraphCollection paragraphs) {
        Paragraph paragraph;
        if (paragraphs.getCount() == 0) {
            paragraph = new Paragraph();
            paragraphs.add(paragraph);
        } else {
            paragraph = (Paragraph) paragraphs.get_Item(0);
            for (int index = paragraphs.getCount() - 1; index >= 1; index--) {
                paragraphs.removeAt(index);
            }
        }

        PortionCollection portions = (PortionCollection) paragraph.getPortions();
        if (portions.getCount() == 0) {
            portions.add(new Portion());
        } else {
            for (int index = portions.getCount() - 1; index >= 1; index--) {
                portions.removeAt(index);
            }
        }

        return paragraph;
    }
    
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }
    
    /**
     * Returns the width of the given shape, taking into account if it's a Placeholder.
     */
    public static int getWidth(Shape shape) {
        return (int) shape.getWidth();
    }

    /**
     * Returns the height of the given shape, taking into account if it's a Placeholder.
     */
    public static int getHeight(Shape shape) {
        return (int) shape.getHeight();
    }

    /**
     * Sets the width of the given shape, taking into account if it's a Placeholder.
     */
    public static void setWidth(Shape shape, int width) {
        shape.setWidth(width);
    }

    /**
     * Sets the height of the given shape, taking into account if it's a Placeholder.
     */
    public static void setHeight(Shape shape, int height) {
        shape.setHeight(height);
    }
    
    public static ParagraphCollection getParagraphs(Shape shape) {
        TextFrame textFrame = getTextFrame(shape);
        if (textFrame != null) {
            return (ParagraphCollection) textFrame.getParagraphs();
        } else {
            return null;
        }
    }
    
    public static TextFrame getTextFrame(Shape shape) {
        if (shape instanceof AutoShape) {
            return (TextFrame) ((AutoShape)shape).getTextFrame();
        }
        return null;
    }
    
    public static void setHtml(String shapeId, String html, IShapeCollection shapes){
        Shape shape = findShape(shapeId, shapes, false);
        AutoShape newShape = (AutoShape)shapes.addAutoShape(ShapeType.Rectangle, shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
        newShape.getFillFormat().setFillType(FillType.NoFill);
        newShape.getLineFormat().getFillFormat().setFillType(FillType.NoFill);
        
        // Clearing all paragraphs in added text frame
        newShape.getTextFrame().getParagraphs().clear();
        // Adding text from HTML stream reader in text frame
        newShape.getTextFrame().getParagraphs().addFromHtml(html);
        shapes.remove(shape);
        
        newShape.getTextFrame().getTextFrameFormat().setAutofitType(TextAutofitType.Normal);
    }
    
    public static void setHtml(String html, IShapeCollection shapes){
        AutoShape newShape = (AutoShape)shapes.addAutoShape(ShapeType.Rectangle, 0, 0, 100, 75);
        newShape.getFillFormat().setFillType(FillType.NoFill);
        newShape.getLineFormat().getFillFormat().setFillType(FillType.NoFill);
        
        // Clearing all paragraphs in added text frame
        newShape.getTextFrame().getParagraphs().clear();
        // Adding text from HTML stream reader in text frame
        newShape.getTextFrame().getParagraphs().addFromHtml(html);
        
        newShape.getTextFrame().getTextFrameFormat().setAutofitType(TextAutofitType.Normal);
    }
    
    public static String ReadFile(String FileName) throws Exception {

        File file = new File(FileName);
        StringBuilder contents = new StringBuilder();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file));
            String text = null;

            // repeat until all lines is read
            while ((text = reader.readLine()) != null) {
                contents.append(text).append(System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        return contents.toString();
    }       
    
    public static void setTitleSlideText(String shapeId, IShapeCollection shapes, String text, boolean fitTextToShape) {
        Shape shape = findShape(shapeId, shapes, false);
        Paragraph paragraph = clear(getParagraphs(shape));
        Portion portion = (Portion) paragraph.getPortions().get_Item(0);
        PortionFormat portionFormat = (PortionFormat) portion.getPortionFormat();
        
        // The old shape will filled with white color even we set the FillType.NoFill, so we have to create a new shape.
        AutoShape newShape = (AutoShape)shapes.addAutoShape(ShapeType.Rectangle, shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
        newShape.getFillFormat().setFillType(FillType.NoFill);
        newShape.getLineFormat().getFillFormat().setFillType(FillType.NoFill);

        // Apply the text style.
        TextFrame tf = (TextFrame)newShape.getTextFrame();
        tf.getParagraphs().get_Item(0).getParagraphFormat().setAlignment(TextAlignment.Left);
        Portion port = (Portion)tf.getParagraphs().get_Item(0).getPortions().get_Item(0);
        port.getPortionFormat().setLatinFont(portionFormat.getLatinFont() != null ? portionFormat.getLatinFont() : new FontData("Arial"));
        port.getPortionFormat().setFontBold(portionFormat.getFontBold() != NullableBool.NotDefined ? portionFormat.getFontBold() : NullableBool.False);
        if ("subtitle".equals(shapeId)) {
            port.getPortionFormat().setFontItalic(portionFormat.getFontItalic() != NullableBool.NotDefined ? portionFormat.getFontItalic() : NullableBool.True);
        } else {
            port.getPortionFormat().setFontItalic(portionFormat.getFontItalic() != NullableBool.NotDefined ? portionFormat.getFontItalic() : NullableBool.False);
        }
        port.getPortionFormat().setFontUnderline(portionFormat.getFontUnderline() != NullableBool.NotDefined ? portionFormat.getFontUnderline() : NullableBool.False);
        if ("title".equals(shapeId)) {
            port.getPortionFormat().setFontHeight(portionFormat.getFontHeight() >= 0.0f ? portionFormat.getFontHeight() : 29.0f);
        } else if ("subtitle".equals(shapeId)) {
            port.getPortionFormat().setFontHeight(portionFormat.getFontHeight() >= 0.0f ? portionFormat.getFontHeight()  : 20.0f);
        } else {
            port.getPortionFormat().setFontHeight(portionFormat.getFontHeight() >= 0.0f ? portionFormat.getFontHeight()  : 14.0f);
        }
        port.getPortionFormat().getFillFormat().setFillType(portionFormat.getFillFormat().getFillType() != FillType.NotDefined ? portionFormat.getFillFormat().getFillType() : FillType.Solid);
        port.getPortionFormat().getFillFormat().getSolidFillColor().setColor(Color.WHITE);
        shapes.remove(shape);
        
        if (fitTextToShape) {
            setFitTextToShape(newShape, tf, port, text);
        }
    }
    
    public static void createBullet(Slide slide){
      //Adding and accessing Autoshape
        IAutoShape aShp = slide.getShapes().addAutoShape(ShapeType.Rectangle, 200, 200, 400, 200);

        //Accessing the text frame of created autoshape
        ITextFrame txtFrm = aShp.getTextFrame();
        txtFrm.getTextFrameFormat().setAutofitType(TextAutofitType.Shape);

        //Removing the default exisiting paragraph
        txtFrm.getParagraphs().removeAt(0);

        Paragraph paraS1 = createSymbolBullet("Jan", 10, 40, (short)1);
        Paragraph paraS2 = createSymbolBullet("Feb", 10, 40, (short)2);
        Paragraph paraS3 = createSymbolBullet("Mar", 10, 40, (short)3);
        Paragraph paraS4 = createSymbolBullet("April", 10, 40, (short)0);

        //Adding Paragraph to text frame
        txtFrm.getParagraphs().add(paraS1);
        txtFrm.getParagraphs().add(paraS2);
        txtFrm.getParagraphs().add(paraS3);
        txtFrm.getParagraphs().add(paraS4);

        Paragraph paraN1 = createNumberBullet("Me", 10, 30, (short)0);
        Paragraph paraN2 = createNumberBullet("He", 10, 30, (short)1);
        Paragraph paraN3 = createNumberBullet("She", 10, 30, (short)2);
        Paragraph paraN4 = createNumberBullet("They", 10, 30, (short)4);

        //Adding Paragraph to text frame
        txtFrm.getParagraphs().add(paraN1);
        txtFrm.getParagraphs().add(paraN2);
        txtFrm.getParagraphs().add(paraN3);
        txtFrm.getParagraphs().add(paraN4);

        Paragraph paraOnce = new Paragraph();
        paraOnce.setText("Once we have settled on the right candidate, we need to update our recruiters so that they know the position is filled. This is also the trigger for them generating an invoice to us for their placement services. For this reason, it is essential that all communication with Recruiters is managed by the Human Resources team. We have agreed with all of our Recruiters that we will notify them at the end of each week.");
        paraOnce.getParagraphFormat().setAlignment(TextAlignment.Left);
        paraOnce.getParagraphFormat().setMarginLeft(40);
        paraOnce.getParagraphFormat().setHangingPunctuation(NullableBool.False);
        txtFrm.getParagraphs().add(paraOnce);
        
        Paragraph paraNote = new Paragraph();
        paraNote.setText("Note we have settled on the right candidate, we need to update our recruiters so that they know the position is filled. This is also the trigger for them generating an invoice to us for their placement services. ");
        paraNote.getParagraphFormat().setAlignment(TextAlignment.Right);
        paraNote.getParagraphFormat().setMarginRight(80);
        paraNote.getParagraphFormat().setHangingPunctuation(NullableBool.True);
        txtFrm.getParagraphs().add(paraNote);
        
        Paragraph paraBack1 = new Paragraph();
        paraBack1.setText("Backwards Indent");
        paraBack1.getParagraphFormat().setAlignment(TextAlignment.Right);
        paraBack1.getParagraphFormat().setMarginRight(80);
        paraBack1.getParagraphFormat().setDepth((short) 1);
        txtFrm.getParagraphs().add(paraBack1);
        
        Paragraph paraBack2 = new Paragraph();
        paraBack2.setText("Backwards Indent2");
        paraBack2.getParagraphFormat().setAlignment(TextAlignment.Right);
        paraBack2.getParagraphFormat().setMarginRight(160);
        paraBack2.getParagraphFormat().setDepth((short) 2);
        txtFrm.getParagraphs().add(paraBack2);
    }
    
    public static void setMargin(ISlide slide) {
        // Adding and accessing Autoshape
        IAutoShape aShp = slide.getShapes().addAutoShape(ShapeType.Rectangle, 200, 200, 400, 200);

        // Accessing the text frame of created autoshape
        ITextFrame txtFrm = aShp.getTextFrame();
        txtFrm.getTextFrameFormat().setAutofitType(TextAutofitType.Shape);

        // Removing the default exisiting paragraph
        txtFrm.getParagraphs().clear();

        Paragraph paraLeft1 = new Paragraph();
        paraLeft1.setText("Left Indent 1");
        paraLeft1.getParagraphFormat().setAlignment(TextAlignment.Left);
        paraLeft1.getParagraphFormat().setDepth((short) 1);
        paraLeft1.getParagraphFormat().setMarginLeft(40);
        txtFrm.getParagraphs().add(paraLeft1);

        Paragraph paraLeft2 = new Paragraph();
        paraLeft2.setText("Left Indent 2");
        paraLeft2.getParagraphFormat().setAlignment(TextAlignment.Left);
        paraLeft2.getParagraphFormat().setDepth((short) 2);
        paraLeft2.getParagraphFormat().setMarginRight(80);
        txtFrm.getParagraphs().add(paraLeft2);

        Paragraph paraRight1 = new Paragraph();
        paraRight1.setText("Right Indent 1");
        paraRight1.getParagraphFormat().setAlignment(TextAlignment.Right);
        paraRight1.getParagraphFormat().setDepth((short) 1);
        paraRight1.getParagraphFormat().setMarginRight(40);
        txtFrm.getParagraphs().add(paraRight1);

        Paragraph paraRight2 = new Paragraph();
        paraRight2.setText("Right Indent 2");
        paraRight2.getParagraphFormat().setAlignment(TextAlignment.Right);
        paraRight2.getParagraphFormat().setDepth((short) 2);
        paraRight2.getParagraphFormat().setMarginRight(80);
        txtFrm.getParagraphs().add(paraRight2);
    }
    
    public static Paragraph createSymbolBullet(String text, int indent, int offset, short depth){
      //Creating a paragraph
        Paragraph para = new Paragraph();

        //Setting paragraph bullet style and symbol
        para.getParagraphFormat().getBullet().setType(BulletType.Symbol);
        para.getParagraphFormat().getBullet().setChar((char) 8226);

        //Setting paragraph text
        para.setText(text);

        //Setting bullet and text indent
        para.getParagraphFormat().setDepth(depth);
        para.getParagraphFormat().setMarginLeft(offset + 40 * depth);
        para.getParagraphFormat().setIndent(indent);
        return para;
    }
    
    public static Paragraph createNumberBullet(String text, int indent, int offset, short depth){
      //Creating second paragraph
        Paragraph para2 = new Paragraph();

        //Setting paragraph bullet type and style
        para2.getParagraphFormat().getBullet().setType(BulletType.Numbered);
        para2.getParagraphFormat().getBullet().setNumberedBulletStyle(NumberedBulletStyle.BulletArabicPeriod);

        //Adding paragraph text
        para2.setText(text);

        //Setting bullet indent
        para2.getParagraphFormat().setDepth(depth);
        para2.getParagraphFormat().setMarginLeft(offset + 40 * depth);
        para2.getParagraphFormat().setIndent(indent);
        return para2;
    }
    
    
    
    public static Shape findShape(String id, IShapeCollection shapes, boolean remove) {
        for (int index = 0, size = shapes.size(); index < size; index++) {
            Shape shape = (Shape) shapes.get_Item(index);
            if (id.equals(shape.getAlternativeText())) {
                if (remove) {
                    shapes.removeAt(index);
                }
                return shape;
            }
        }

        throw new IllegalStateException("Failed to find shape with id " + id);
    }
    
    /**
     * Returns the text of the given shape.
     */
    public static String getText(Shape shape) {
        TextFrame textFrame = getTextFrame(shape);
        if (textFrame != null) {
            return textFrame.getText();
        } else {
            return null;
        }
    }
    
    public static Slide createCloneSlide(Presentation presentation, Slide sourceSlide, int position) {
        Slide newSlide = (Slide) presentation.getSlides().insertClone(position, sourceSlide);
        return newSlide;
    }
    
    public static void setText(String shapeId, IShapeCollection shapes, String text, boolean fitTextToShape) {
        Shape shape = findShape(shapeId, shapes, false);
        setText(shape, text, fitTextToShape, TextAlignment.NotDefined);
    }
    
    /**
     * Handle document properties
     */
    public static void handleDocumentProperties(Presentation presentation) {
        //Create a reference to IDocumentProperties object associated with Presentation
        IDocumentProperties documentProperties = presentation.getDocumentProperties();

        // Set the built in properties
        documentProperties.setTitle ("IBM BlueworksLive Blueprint Presentation");
        documentProperties.setHyperlinkBase("http://www.blueworkslive.com");
        //documentProperties.set_Item("Hyperlink base", "http://www.blueworkslive.com");
        documentProperties.setComments ( "Generated by IBM BlueworksLive");
        documentProperties.setCompany("IBM");
    }
}
