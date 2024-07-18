import qupath.imagej.gui.ImageJMacroRunner
import qupath.lib.regions.*
import qupath.imagej.tools.IJTools
import qupath.imagej.gui.IJExtension
import ij.*
import qupath.lib.plugins.parameters.ParameterList
import qupath.lib.gui.tools.MeasurementExporter
import qupath.lib.objects.PathAnnotationObject

// Get the list of all images in the current project
def imagesToExport = project.getImageList()


// Separate each measurement value in the output file with a tab ("\t")
def separator = "\t"


// Choose the columns that will be included in the export
// Note: if 'columnsToInclude' is empty, all columns will be included
category = "mucinarea: red stuff area " + qupath.lib.common.GeneralTools.micrometerSymbol() + "^2"
print category
def columnsToInclude = new String[]{"Image","mucinarea: red stuff area " + qupath.lib.common.GeneralTools.micrometerSymbol() + "^2"}
def exportType = PathAnnotationObject.class

// Choose your *full* output path
def imageData = getCurrentImageData()
def imServer = imageData.getServer()
String path = imServer.getPath()

path = (path.substring(29))
print path
cutoff = path.lastIndexOf('/')
path = path.substring(0,cutoff+1)
outputPath = path + 'results.tsv'
print outputPath
name = 'results.tsv'
mkdirs(path)
path = buildFilePath(path, name)
def outputFile = new File(outputPath)
def exporter  = new MeasurementExporter()
                  .imageList(imagesToExport)            // Images from which measurements will be exported
                  .separator(separator)                 // Character that separates values
                  .includeOnlyColumns(columnsToInclude) // Columns are case-sensitive
                  .exportType(exportType)               // Type of objects to export
                  .exportMeasurements(outputFile)        // Start the export process
print('complete')