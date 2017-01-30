package fileupload

import grails.converters.JSON


import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.io.File;
import java.io.IOException;
 
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;
//import javax.mail.Multipart

class PersonController {
   static allowedMethods = [createPerson: "POST"]
     def createPerson(String name, String file)
   {
       // println "file : ${params.file.getInputStream().toCsvReader().readAll()}"

        def f = request.getFile('file')//.inputStream.text
        println "file : ${f}"
        def personInstance = new Person()
        if (f == null) 
        {
           render "File cannot be empty", status: 400
           return
        } 
        else 
        {
            println "Inside else part file : ${f}"
            //def personInstance = new Person()
            personInstance.name = name
            personInstance.filename = f.originalFilename
            personInstance.fullPath = grailsApplication.config.uploadFolder + personInstance.filename
            f.transferTo(new File(personInstance.fullPath))
            personInstance.save(flush:true)
        }
             //def path = params.fullPath  //new File(PersonInstance.fullPath)
//           def fileInputStream = new FileInputStream(file2)
//            def outputStream = response.getOutputStream()
//            byte[] buffer = new byte[4096];
//            int len;
//            while ((len = fileInputStream.read(buffer)) > 0) {
//                outputStream.write(buffer, 0, len);
//            }
//            outputStream.flush()
//            outputStream.close()
//            fileInputStream.close()
         // def imageFile= new File("${personInstance.filename}")
         println "Hello entering to image manipulation"
        InputStream is  = new BufferedInputStream(new FileInputStream(grailsApplication.config.uploadFolder + personInstance.filename))
        BufferedImage originalImage = ImageIO.read(is)       
        BufferedImage small = Scalr.resize(originalImage,Scalr.Method.ULTRA_QUALITY,Scalr.Mode.AUTOMATIC,150,150,Scalr.OP_ANTIALIAS)
        println 'After bufferimage '
        ImageIO.write(small,"png",new File(grailsApplication.config.uploadFolder + personInstance.filename + "4.png"))
        render "${name} successfully inserted "        
   }
  
   def patientList(String format, int max, int offset)
   {
      format  = 'json'
      def c = Person.createCriteria()
      def person = c.get{
         if (params.name)
         {
            eq('name', params.name)
         }
          
      }   
      //def res = new PaginatedResults(listName:'patients', list:subjects, max:max, offset:offset)
      if (format == "json")
      {
         def result = person as JSON
         
         // JSONP
         if (params.callback) result = "${params.callback}( ${result} )"
         render(text: result, contentType:"application/json", encoding:"UTF-8")
      }
      else
      {
         renderError(message(code:'rest.error.ehr_uid_required'), '400', 400)
         return
      }      
    }  
    
   def download(String name) {
        def c = Person.createCriteria()
        def PersonInstance = c.get {
               eq('name',name)
         }   
        if ( PersonInstance == null) {
            flash.message = "Person not found."
           render "Person not found.", status: 400
        } else {
            response.setContentType("APPLICATION/OCTET-STREAM")
            response.setHeader("Content-Disposition", "Attachment;Filename=\"${PersonInstance.filename}\"")
            println "FileName : ${PersonInstance.filename}"
            def file = new File(PersonInstance.fullPath)
            def fileInputStream = new FileInputStream(file)
            def outputStream = response.getOutputStream()
            byte[] buffer = new byte[4096];
            int len;
            while ((len = fileInputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush()
            outputStream.close()
            fileInputStream.close()
        }
    }
    
}
