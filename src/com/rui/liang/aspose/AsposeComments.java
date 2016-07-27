package com.rui.liang.aspose;

import java.util.Date;

import com.aspose.words.Comment;
import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.Paragraph;
import com.aspose.words.Run;
import com.aspose.words.SaveFormat;

/**
 * TODO: Provide a class description.
 *
 * @author liangrui
 */
public class AsposeComments {
    public static void main(String[] args) throws Exception {
        String dataPath = "C:/Users/IBM_ADMIN/Downloads/";

        Document doc = new Document();
        DocumentBuilder builder = new DocumentBuilder(doc);
        builder.writeln("Some text is added.");
        Date date = new Date(1463561367118L); // new Date(1463590167118L);
        Comment comment = new Comment(doc, "Aspose", "As", date);
        builder.getCurrentParagraph().appendChild(comment);
        comment.getParagraphs().add(new Paragraph(doc));
        comment.getFirstParagraph().getRuns().add(new Run(doc, "Comment text."));
        
        builder.writeln("Public URL.");
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("INCLUDEPICTURE ");
        strBuilder.append("https://upload.wikimedia.org/wikipedia/commons/c/ce/Konqueror4_Logo.png");
        strBuilder.append(" \\* MERGEFORMAT");
        //"INCLUDEPICTURE \"https://upload.wikimedia.org/wikipedia/commons/c/ce/Konqueror4_Logo.png\" \\* MERGEFORMAT"
        builder.insertField(strBuilder.toString());
        
        builder.writeln("Local URL.");
        strBuilder.setLength(0);
        strBuilder.append("INCLUDEPICTURE ");
        strBuilder.append("https://localhost:8443/scr/download/wukong.jpg?processId=ab0010&fileItemId=ac0007");
        strBuilder.append(" \\* MERGEFORMAT");
        
        builder.writeln("Ralative URL.");
        strBuilder.setLength(0);
        strBuilder.append("INCLUDEPICTURE ");
        strBuilder.append("/scr/download/wukong.jpg?processId=ab0010&fileItemId=ac0007");
        strBuilder.append(" \\* MERGEFORMAT");
        
        doc.save(dataPath + "Aspose_Comments.docx", SaveFormat.DOCX);
        System.out.println("Done.");
    }
}
