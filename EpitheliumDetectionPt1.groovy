import qupath.imagej.gui.ImageJMacroRunner
import qupath.lib.regions.*
import qupath.imagej.tools.IJTools
import qupath.imagej.gui.IJExtension
import ij.*
import qupath.lib.plugins.parameters.ParameterList
import qupath.lib.gui.tools.MeasurementExporter
import qupath.lib.objects.PathAnnotationObject

    setImageType('FLUORESCENCE')
    
    createAnnotationsFromPixelClassifier("lumenfinderv5", 30000.0, 0.0, "SPLIT")
 
    def annotations0 = getAnnotationObjects();
    print annotations0;
    for (annotation in annotations0){
        String type = annotation.getPathClass()
        if(type == 'Positive'){
            removeObject(annotation, true);
        }
   
    }
    selectAnnotations();
    annotations0 = getAnnotationObjects();
    for (annotation in annotations0){
        runPlugin('qupath.lib.plugins.objects.RefineAnnotationsPlugin', '{"minFragmentSizeMicrons": 5000.0,  "maxHoleSizeMicrons": 5000.0}'); 
    }
   // def request = RegionRequest.createInstance(server.getPath(), downsample, roi)
   /// def pathImage = IJTools.convertToImagePlus(server, request)
   // def imp = pathImage.getImage()
   // imp.show()
    
    //Convert QuPath ROI to ImageJ Roi & add to open image
  //  def roiIJ = IJTools.convertToIJRoi(roi, pathImage)
   // imp.setRoi(roiIJ)
    // Create a macro runner so we can check what the parameter list contains

    selectAnnotations();
    def imageData = getCurrentImageData()
    def annotations = getAnnotationObjects()
    
    // Loop through the annotations and run the macro
    runPlugin('qupath.lib.plugins.objects.DilateAnnotationPlugin', '{"radiusMicrons": 3.0,  "lineCap": "Round",  "removeInterior": false,  "constrainToParent": true}');     
    for (annotation in annotations) {
        removeObject(annotation, true);
    }
    
    selectAnnotations();    
    def annotations2 = getAnnotationObjects()
    runPlugin('qupath.lib.plugins.objects.DilateAnnotationPlugin', '{"radiusMicrons": 30.0,  "lineCap": "Round",  "removeInterior": true,  "constrainToParent": true}');     
    for (annotation in annotations2) {
        removeObject(annotation, true);
    }
    


   
    print 'Done!'

print('actually done')