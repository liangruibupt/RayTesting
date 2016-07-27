package com.rui.liang.aspose;

import com.aspose.slides.ISlide;
import com.aspose.slides.Presentation;
import com.aspose.slides.SaveFormat;

/**
 * TODO: Provide a class description.
 *
 * @author liangrui
 */
public class SlideShapAutoFit {
    
    private static String dataPath = "C:/Users/IBM_ADMIN/Downloads/"; 
    
//    static {
//        License license = new License();
//        try {
//            //String licenseFile = ReadFile(dataPath + "Aspose.Slides.lic");
//            license.setLicense(dataPath + "Aspose.Slides.lic");
//        } catch (Exception e) {
//            throw new RuntimeException("Error registering license file for Aspose.Slides", e);
//        }
//    }

    public static void main(String[] args) throws Exception {
        SlideShapAutoFit instance = new SlideShapAutoFit();
        //Create an instance of Presentation class
        Presentation pres = new Presentation();

        //Access the first slide 
        ISlide slide = pres.getSlides().get_Item(0);

//        //Add an AutoShape of Rectangle type
//        AutoShape ashp = (AutoShape) slide.getShapes().addAutoShape(ShapeType.Rectangle, 150, 75, 100, 100);
//
//        //Add TextFrame to the Rectangle
//        ashp.getFillFormat().setFillType(FillType.NoFill);
//
//        //Setting text autofit type
//        String text = "Create an instance of Presentation class, Access the first slide, Setting text autofit type. Can't assign string which contains paragraph break character.";
//        AsposeSlideUtil.setText(ashp, text, true, TextAlignment.NotDefined);
//        
//        ISlide bulletSlide = pres.getSlides().addClone(slide);
//        AsposeSlideUtil.createBullet((Slide) bulletSlide);
//        
//        ISlide activitySlide = pres.getSlides().addClone(slide);
//        String description = AsposeSlideUtil.ReadFile(dataPath + "sampleActivity.html");
//        AsposeSlideUtil.setHtml(description, activitySlide.getShapes());
//        
//        
//        AsposeSlideUtil.handleDocumentProperties(pres);
        
        AsposeSlideUtil.setMargin(slide);
        //Save Presentation, ppt can not work
        //pres.save(dataPath + "formatText.ppt", SaveFormat.Ppt);
        pres.save(dataPath + "formatText.pptx", SaveFormat.Pptx);
        System.out.println("Done");
    }
}
