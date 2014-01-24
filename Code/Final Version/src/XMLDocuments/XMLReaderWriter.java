package XMLDocuments;

/**
 * XMLReader&Writer 
 * @author DaweiG
 */

import java.io.*;  
import MapStructure.Map;
import MapStructure.Pixel;
public class XMLReaderWriter implements XMLDocument {
	private String oneLine;

	/**
	 * Create XML file based on the map object
	 * @param myMap  pathName
	 * @return XML Filename
	 */
	
	public String createXml(Map myMap,String pathName) {
		File file = new File(pathName);
		if(myMap == null)
			throw new NullPointerException();
		try {
			 FileWriter fw=new FileWriter(file);  
			 BufferedWriter bw=new BufferedWriter(fw);
			 
			 //writing file head
			 bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			 bw.newLine();
			 bw.write("<arch-map units=\"pixels\">" + "\n");
			 String str = String.format("<attribute>"+"\n"+
										"\t<key>Creation Date</key>"+"\n"+
										"\t<value>"+myMap.getId()+"</value>"+"\n"+
										"</attribute>\n");
			 bw.write(str);
			 //writing Map information
			 str = String.format(	"<boundary>"+"\n"+
									"\t<point x=\"0\" y=\"0\"/>"+"\n"+
									"\t<point x=\"%d\" y=\"0\"/>"+"\n"+
									"\t<point x=\"%d\" y=\"%d\"/>"+"\n"+
									"\t<point x=\"0\" y=\"%d\"/>"+"\n"+
									"</boundary>", myMap.getWidth(),myMap.getWidth(),myMap.getLength(),myMap.getLength());
			 bw.write(str);
			 bw.newLine();
			 
			 //writing start point
			 str = String.format("<Start-pixel xPos=\"%d\" yPos=\"%d\">", myMap.getStartxPos(),myMap.getStartyPos());
			 bw.write(str);
			 bw.newLine();
			 
			 //writing current pixel
			 str = String.format("<robot-status>"+"\n"+
			 						"\t<attribute>"+"\n"+
			 							"\t\t<key>heading</key>"+"\n"+
			 							"\t\t<value>%d</value>"+"\n"+
			 						"\t</attribute>"+"\n"+
			 						"\t<point x=\"%d\" y=\"%d\"/>"+"\n"+
			 					"</robot-status>",((myMap.getRs().getRobotDirection()-1)*90), myMap.getCurrentPixel().getxPos(),myMap.getCurrentPixel().getyPos());
			 bw.write(str);
			 bw.newLine();
			 bw.write("<pixels>");
			 bw.newLine();
			 //writing pixels
			 for(Pixel p: myMap.getPixels()){
				 String pixStr = String.format("\t<Pixel xPos=\"%d\" yPos=\"%d\" Value=\"%d\"/>",p.getxPos(),p.getyPos(),p.getValue());;
				 bw.write(pixStr);
				 bw.newLine();
			 }
			 bw.write("</pixels>");
			 bw.newLine();
			 bw.write("</arch-map>");
			 bw.newLine();
			 bw.flush();
			 fw.close();
			 return pathName;
		}catch (Exception e) {  
  
            e.printStackTrace();  
        }
		return null; 

	}

	/**
	 * Load XML file into Map object.
	 * @param pathName
	 * @return Map loadedMap
	 */
	public Map loadXML(String pathName) throws XMLFormatException{
		Map loadedMap = null;
		try {
			FileReader reader = new FileReader(pathName);
			BufferedReader br = new BufferedReader(reader);
			String oneLine = null;
			String[] info = null;
			int lineNumber = 0;
			
			if(!br.readLine().equals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")) throw new XMLFormatException(null, pathName);
			lineNumber++;
			if(!br.readLine().equals("<arch-map units=\"pixels\">")) throw new XMLFormatException(null, pathName);
			lineNumber++;
			
			String mapId = "abc";
			
			if(!br.readLine().equals("<attribute>")) throw new XMLFormatException(null, pathName);
			else{
					br.readLine();
					oneLine = br.readLine();
					mapId = oneLine.substring(8, 18);
					lineNumber++;
			}
			br.readLine();
			
			
			//System.out.println(mapId);
			int mapLength = 0,mapWidth = 0;
			if(!br.readLine().equals("<boundary>")) throw new XMLFormatException(null, pathName);
			else{
					lineNumber++;
					br.readLine();
					lineNumber++;
					br.readLine();
					if((oneLine = br.readLine()).contains("<point")){
						if((info = oneLine.split("\""))!=null){
							mapLength = Integer.parseInt(info[1]);
							mapWidth = Integer.parseInt(info[3]);
					}
					}
					br.readLine();
					lineNumber++;
			}
			br.readLine();
			lineNumber++;

			oneLine = br.readLine();
			info = oneLine.split("\"");
			int startxPos = Integer.parseInt(info[1]);
			int startyPos = Integer.parseInt(info[3]);
			loadedMap = new Map(mapId, mapLength, mapWidth, startxPos, startyPos,2);
			int robotDirection = 0;
			int currxPos = 0;
			int curryPos = 0;
			
			if(!br.readLine().equals("<robot-status>")) throw new XMLFormatException(null, pathName);
			else{
					br.readLine();
					lineNumber++;
					br.readLine();
					lineNumber++;
					oneLine = br.readLine();
					robotDirection = Integer.parseInt(oneLine.substring(9, 10)) / 90 +1;
					loadedMap.getRs().setRobotDirection(robotDirection);
					br.readLine();
					lineNumber++;
					oneLine = br.readLine();
					info = oneLine.split("\"");
					currxPos = Integer.parseInt(info[1]);
					curryPos = Integer.parseInt(info[3]);
					loadedMap.getRs().setRobotLocation(loadedMap.findPixel(currxPos, curryPos));
					loadedMap.setCurrentPixel(loadedMap.findPixel(currxPos, curryPos));
			}
			br.readLine();
			lineNumber++;
			//Loading pixels 
			if(!br.readLine().equals("<pixels>")) throw new XMLFormatException(null, pathName);
			while((oneLine = br.readLine()) != null){
					info = oneLine.split("\"");
						if(info.length > 1 ){
							lineNumber++;
							//System.out.println(info.length);
							int pixelxPos = Integer.parseInt(info[1]);
							int pixelyPos = Integer.parseInt(info[3]);
							int pixelValue = Integer.parseInt(info[5]);
						loadedMap.findPixel(pixelxPos, pixelyPos).setValue(pixelValue);
					}else if(!oneLine.equals("</pixels>") && !oneLine.equals("</arch-map>"))
						throw new XMLFormatException(null, pathName);
							
			}
			for(Pixel p: loadedMap.getPixels()){
				p.setE(loadedMap.findPixel(p.getxPos()+1, p.getyPos()));
				p.setS(loadedMap.findPixel(p.getxPos(), p.getyPos()+1));
				p.setW(loadedMap.findPixel(p.getxPos()-1, p.getyPos()));
				p.setN(loadedMap.findPixel(p.getxPos(), p.getyPos()-1));
			}
			br.close();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new XMLFormatException(oneLine, pathName);
		}
        return loadedMap;

}
	}

