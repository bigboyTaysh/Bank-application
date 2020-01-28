package pl.wolski.bank.features;


import com.itextpdf.text.pdf.BaseFont;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;
import pl.wolski.bank.models.Transaction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

@Log4j2
@Component
public class PdfGenerator {
    private final TemplateEngine templateEngine;

    public PdfGenerator(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public void createPdf(String templateName, Map<String, Object> map) throws Exception {
        Context ctx = new Context();
        Iterator itMap = map.entrySet().iterator();
        while (itMap.hasNext()) {
            Map.Entry pair = (Map.Entry) itMap.next();
            ctx.setVariable(pair.getKey().toString(), pair.getValue());
        }

        String id = ((Transaction)ctx.getVariable("transaction")).getId().toString();

        String processedHtml = templateEngine.process(templateName, ctx);

        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        String UPLOADED_FOLDER = s + "\\src\\main\\resources\\static\\confirmation\\";

        FileOutputStream os = null;
        try {
            os = new FileOutputStream(UPLOADED_FOLDER+id+".pdf");

            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(processedHtml);
            renderer.layout();
            renderer.createPDF(os,false);
            renderer.finishPDF();
            log.info("wygenerowano pdf!");
        } catch (IOException x){

        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) { /*ignore*/ }
            }
        }
    }

}
