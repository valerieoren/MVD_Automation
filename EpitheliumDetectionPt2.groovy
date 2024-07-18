import qupath.imagej.gui.ImageJMacroRunner
import qupath.lib.regions.*
import qupath.imagej.tools.IJTools
import qupath.imagej.gui.IJExtension
import ij.*
import qupath.lib.plugins.parameters.ParameterList
import qupath.lib.gui.tools.MeasurementExporter
import qupath.lib.objects.PathAnnotationObject
    selectAnnotations();
    def annotations = getAnnotationObjects();
    for (annotation in annotations) {
        runPlugin('qupath.lib.plugins.objects.RefineAnnotationsPlugin', '{"minFragmentSizeMicrons": 10000.0,  "maxHoleSizeMicrons": 5000.0}'); 
        runPlugin('qupath.lib.plugins.objects.SplitAnnotationsPlugin', '{}');
    }
    def server = getCurrentServer()
    def roi = getSelectedROI()
    double downsample = 1.0
   // def request = RegionRequest.createInstance(server.getPath(), downsample, roi)
   /// def pathImage = IJTools.convertToImagePlus(server, request)
   // def imp = pathImage.getImage()
   // imp.show()
    
    //Convert QuPath ROI to ImageJ Roi & add to open image
  //  def roiIJ = IJTools.convertToIJRoi(roi, pathImage)
   // imp.setRoi(roiIJ)
    // Create a macro runner so we can check what the parameter list contains
    def params = new ImageJMacroRunner(getQuPath()).getParameterList()
    
    // Change the value of a parameter, using the JSON to identify the key
    params.getParameters().get('downsampleFactor').setValue(1.0 as double)
    print ParameterList.getParameterListJSON(params, ' ')
    
    // Get the macro text and other required variables
    //def macro = new File("myMacroPath/MacroName.ijm").text
    //Line above from https://github.com/qupath/qupath/issues/176

    String path = server.getPath()
    print path;
    path = path[path.lastIndexOf('/')+1..-1];
    image = "'" + path + "'";
    print image;
    def macro = //'run("Interpolate", "interval=5 smooth");' +
                'dir = getDirectory("image");'+
               // 'print(dir);'+
                'run("Create Mask");'+
                'setOption("BlackBackground", false);'+
                'run("Skeletonize");'+
                'run("Analyze Skeleton (2D/3D)", "prune=[shortest branch] calculate");;'+
                'selectWindow("Longest shortest paths");' +
                'Table.applyMacro("image =' + image +';"+ "a=Table.size;"+"b = 0;"+"for (i = 0; i < a; i++){ if (getResult('+"'Longest Shortest Path'"+', i)>getResult('+"'Maximum Branch Length'"+',i)){c = getResult('+"'Longest Shortest Path'"+', i);}else{c = getResult('+"'Maximum Branch Length'"+', i);}if (c>b){ b=c;}}" +"Table.create('+"'Results'"+');" + "values1 = newArray(1);" + "Array.fill(values1,b);"+ "values2 = newArray(image);" + "Table.showArrays('+"'Membrane'"+', values2, values1);");'+
                'IJ.renameResults('+"'Results'"+');'+
                'path1 = dir + "membrane.xls";'+
                'print(path1);'+
                'path2 = path1.substring(29);'+
                'run("Read and Write Excel", "no_count_column stack_results file=["+path2+"]");'
                
    def imageData = getCurrentImageData()
    def annotations2 = getAnnotationObjects()
    selectAnnotations()
    runPlugin('qupath.lib.plugins.objects.DilateAnnotationPlugin', '{"radiusMicrons": 2.0,  "lineCap": "Round",  "removeInterior": false,  "constrainToParent": false}'); 
    // Loop through the annotations and run the macro
    for (annotation in annotations2) {
        removeObject(annotation,true)
        // addPixelClassifierMeasurements("randomtree", "randomtree")
    }
    def annotations3 = getAnnotationObjects()
    print annotations3
    selectAnnotations()
    for (annotation in annotations3) {
        ImageJMacroRunner.runMacro(params, imageData, null, annotation, macro)
        addPixelClassifierMeasurements("8-2-mucin", "mucinarea")
    }
       
   