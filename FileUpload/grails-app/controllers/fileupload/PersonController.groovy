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

class ImageController {
   static allowedMethods = [uploadImage: "POST"]
@Transactional   
@SecuredStateless
def uploadImage(Person personInstance,String imageName) {
       
     
        if (params.imageName == null){
            println "No Image to upload"
        }
        else {
            if (!request.getHeader('Content-Type')?.startsWith('multipart/form-data')) {
                flash.message = "Image Not attached in request: Request is not of multipart/form-data. "
                render "Image Not attached in request: Request is not of multipart/form-data.", status: 400
                return
            }
            def f = request.getFile('payLoad')//.inputStream.text
            if (f == null) 
            {
                render "File cannot be empty", status: 400
                return
            } 
            else 
            {
                def origFileName = personInstance.imageName
                println "origFileName ${origFileName}"
                def fileName = java.util.UUID.randomUUID() as String
                def fullPath = config.image_upload_repo + fileName + ".png"
                f.transferTo(new File(fullPath))
                println "fullPath ${fullPath}"

                //delete if an image file exists             
                if (origFileName != null) { 
                    def file = new File(config.image_upload_repo + origFileName)   
                    println "deleting file ${file}"
                    file.setWritable(true);                    
                    file.delete()                      
                }
               
        InputStream is  = new BufferedInputStream(new FileInputStream(grailsApplication.config.uploadFolder + personInstance.filename))
        BufferedImage originalImage = ImageIO.read(is)       
        BufferedImage small = Scalr.resize(originalImage,Scalr.Method.ULTRA_QUALITY,Scalr.Mode.AUTOMATIC,150,150,Scalr.OP_ANTIALIAS)
        println 'After bufferimage '
        ImageIO.write(small,"png",new File(grailsApplication.config.uploadFolder + personInstance.filename + "4.png"))
        render "${name} successfully inserted "        
            } 
        }
        
        
    }  
    

    
}
