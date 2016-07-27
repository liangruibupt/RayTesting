package com.rui.liang.aspose;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.aspose.slides.IPPImage;
import com.aspose.slides.ISlide;
import com.aspose.slides.ISlideCollection;
import com.aspose.slides.License;
import com.aspose.slides.PictureFrame;
import com.aspose.slides.Presentation;
import com.aspose.slides.SaveFormat;
import com.aspose.slides.Shape;
import com.aspose.slides.ShapeType;
import com.aspose.slides.Slide;

/**
 * TODO: Provide a class description.
 *
 * @author liangrui
 */
public class AsposeCreateSlide {
    
    private Map<String, Slide> slides = new HashMap<String, Slide>();
   
    private static String dataPath = "C:/Users/IBM_ADMIN/Downloads/"; 
    
    static {
        License license = new License();
        try {
            //String licenseFile = ReadFile(dataPath + "Aspose.Slides.lic");
            license.setLicense(dataPath + "Aspose.Slides.lic");
        } catch (Exception e) {
            throw new RuntimeException("Error registering license file for Aspose.Slides", e);
        }
    }
    
    public static void main(String[] args) throws Exception {
        AsposeCreateSlide instance = new AsposeCreateSlide();
      //Instantiate Presentation class that represents a presentation file
        
        Presentation pres = instance.openTemplate("PowerPointTemplate.pptx");

        ISlideCollection slds = pres.getSlides();
        
        slds.insertClone(pres.getFirstSlideNumber(), pres.getSlides().get_Item(2));

        // Title Slide
        Slide titleSlide = (Slide) pres.getSlides().get_Item(0);
        AsposeSlideUtil.setTitleSlideText("title", titleSlide.getShapes(), "liangrui@cn.ibm.com 流程入门 (ID: 100f7f)", true);
        AsposeSlideUtil.setTitleSlideText("subtitle", titleSlide.getShapes(), "The Process Blueprints included in this space have been discovered, documented, and analyzed in enough detail to illustrate many of the process documentation capabilities. ", true);
        AsposeSlideUtil.setTitleSlideText("dateLabel", titleSlide.getShapes(), "Last modified on May 18, 2016 4:43 PM", true);
        
        // bullet Slide
        Slide bulletSlide = (Slide) pres.getSlides().get_Item(2);
        AsposeSlideUtil.createBullet(bulletSlide);
        
        // Diagram Slide
        Slide diagramSlide = (Slide) pres.getSlides().get_Item(5);
        AsposeSlideUtil.setText("title", diagramSlide.getShapes(), "Ray Process", true);        
        IPPImage picture = pres.getImages().addImage(new FileInputStream(new File("C:/Users/Public/Pictures/Sample Pictures/1358511487582.jpg")));        
        PictureFrame pictureFrame = (PictureFrame) diagramSlide.getShapes().addPictureFrame(ShapeType.Rectangle, 0,  0, picture.getWidth(), picture.getHeight(), picture);
        //Calculating slide width and height
        float slideWidth = (float) pres.getSlideSize().getSize().getWidth();
        float slideHeight = (float) pres.getSlideSize().getSize().getHeight();

        float scale = Math.min((slideWidth - 1000)/ picture.getWidth(),  (slideHeight - 1000) / picture.getHeight());
        scale = Math.abs(scale);
        int scaledWidth = (int) (scale > 0 ? scale * picture.getWidth() : picture.getWidth());
        int scaledHeight = (int) (scale > 0 ? scale * picture.getHeight() : picture.getHeight()); 
        
        pictureFrame.setX(slideWidth / 2 - scaledWidth / 2);
        pictureFrame.setY(slideHeight / 2 - scaledHeight / 2);
        pictureFrame.setWidth(scaledWidth > slideWidth ? slideWidth : scaledWidth);
        pictureFrame.setHeight(scaledHeight > slideHeight ? slideHeight : scaledHeight);
        
        
        // Activity Slide
        ISlide activitySlide = pres.getSlides().get_Item(6);
        AsposeSlideUtil.setText("title", activitySlide.getShapes(), "activity step1", true);
        AsposeSlideUtil.setText("description-label", activitySlide.getShapes(), "My description", false);
        String description = AsposeSlideUtil.ReadFile(dataPath + "sampleActivity.html");
        AsposeSlideUtil.setHtml("description", description, activitySlide.getShapes());
        
        AsposeSlideUtil.handleDocumentProperties(pres);
        //Write the modified presentation to disk
        pres.save(dataPath + "helloworld_clonedPost.pptx", SaveFormat.Pptx);
        
        System.out.println("Done.");
    }
    
    private Presentation openTemplate(String filePath) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = getClass().getResourceAsStream(filePath);
            if (inputStream == null) {
                throw new FileNotFoundException("PowerPointTemplate.pptx");
            }
            return new Presentation(inputStream);
        } finally {
            closeQuietly(inputStream);
        }
    }
    
    private void closeQuietly(InputStream input) {
        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }
    
    private void createAppendixTitleSlide(Presentation presentation) {
        Slide titleSlide = getSlide("title");
        Slide insertBeforeSlide = getSlide("activity");

        Slide newSlide = AsposeSlideUtil.createCloneSlide(presentation, titleSlide, insertBeforeSlide.getSlideNumber());
        slides.put("appendix", newSlide);

        AsposeSlideUtil.setText("title", newSlide.getShapes(), "Appendix", true);
        AsposeSlideUtil.findShape("subtitle", newSlide.getShapes(), true);
        AsposeSlideUtil.findShape("dateLabel", newSlide.getShapes(), true);
        AsposeSlideUtil.findShape("linkLabel", newSlide.getShapes(), true);
    }
    
    private Slide getSlide(String id) {
        Slide slide = slides.get(id);
        if (slide == null) {
            throw new IllegalStateException("Failed to find slide with id " + id);
        }
        return slide;
    }
    

    private void loadSlides(Presentation presentation) {
        for (ISlide iSlide : presentation.getSlides()) {
            Slide slide = (Slide)iSlide;
            if (slide != null) {
                Shape idShape = AsposeSlideUtil.findShape("slide-id", slide.getShapes(), true);
                String text = AsposeSlideUtil.getText(idShape);
                String slideId = text.substring(text.indexOf(":") + 1).trim();
                slides.put(slideId, slide);
            }
        }
    }
}
