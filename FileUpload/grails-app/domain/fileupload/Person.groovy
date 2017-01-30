package fileupload

class Person {
    String name
    String filename
    String fullPath
    //String smallImage
    Date uploadDate = new Date()
    static constraints = {
        filename(blank:false,nullable:false)
        fullPath(blank:false,nullable:false)
    }
}
