package XML_STRUCTURE;

/**
 * XMLReader&Writer 

 * @author DaweiG
 */
import java.io.*;  
public class XMLReaderWriter implements XMLDocument {
	/**
	 * @param Map map
	 * @return XML Filename
	 */
	public String createXml(Map map,String fileName) {
		 fileName = fileName + ".xml";
		File file = new File(fileName);
		try {
			 FileWriter fw=new FileWriter(file);  
			 BufferedWriter bw=new BufferedWriter(fw);
			 
			 //writing file head
			 bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			 bw.newLine();
			 
			 //writing Map information
			 String str = String.format("<Map id=\"%d\" length=\"%d\" width=\"%d\">", map.getId(),map.getLength(),map.getWidth());
			 bw.write(str);
			 bw.newLine();
			 
			 //writing start point
			 str = String.format("<Start xPos=\"%d\" yPos=\"%d\">", map.getStartxPos(),map.getStartyPos());
			 bw.write(str);
			 bw.newLine();
			 
			 //writing current pixel
			 str = String.format("<Current xPos=\"%d\" yPos=\"%d\">", map.getCurrentPixel().getxPos(),map.getCurrentPixel().getyPos());
			 bw.write(str);
			 bw.newLine();
			 
			 //writing pixels
			 for(Pixel p: map.getPixels()){
				 String pixStr = String.format("\t<Pixel xPos=\"%d\" yPos=\"%d\" Value=\"%d\"/>",p.getxPos(),p.getyPos(),p.getValue());;
				 bw.write(pixStr);
				 bw.newLine();
			 }
			 bw.write("</Map>");
			 bw.newLine();
			 bw.flush();
			 fw.close();
			 return fileName;
		}catch (Exception e) {  
  
            e.printStackTrace();  
        }
		return null; 

	}

	/**
	 * @param String fileName
	 * @return Map loadedMap
	 */
	public Map parserXml(String fileName) {
		Map loadedMap = null;
		
		try {
			FileReader reader = new FileReader(fileName);
			BufferedReader br = new BufferedReader(reader);
			br.readLine();
			String oneLine = null;
			oneLine = br.readLine();
			
			//Loading Map information
			String[] info = oneLine.split("\"");
			int mapId = Integer.parseInt(info[1]);
			int mapLength = Integer.parseInt(info[3]);
			int mapWidth = Integer.parseInt(info[5]);
			oneLine = br.readLine();
			info = oneLine.split("\"");
			int startxPos = Integer.parseInt(info[1]);
			int startyPos = Integer.parseInt(info[3]);
			loadedMap = new Map(mapId, mapLength, mapWidth, startxPos, startyPos);
			
			//Loading current pixel
			info = oneLine.split("\"");
			int currxPos = Integer.parseInt(info[1]);
			int curryPos = Integer.parseInt(info[3]);

			
			//Loading pixels 
			while((oneLine = br.readLine()) != null) {
				if(oneLine.contains("</Map>")){
					System.out.println(oneLine);
					info = oneLine.split("\"");
					int pixelxPos = Integer.parseInt(info[1]);
					int pixelyPos = Integer.parseInt(info[3]);
					int pixelValue = Integer.parseInt(info[5]);
					loadedMap.findPixel(pixelxPos, pixelyPos).setValue(pixelValue);
				}else
					break;
			}
			
			loadedMap.setCurrentPixel(loadedMap.findPixel(currxPos, curryPos));
			//Linking pixels
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
		}
		
        return loadedMap;
		

	}

}
