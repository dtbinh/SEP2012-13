public class PathFinding extends Thread {

int signal;// DFS的返回值
Pixel pixel;// DFS的Node	
int location[];// Robot的位置
int original_point[]={xpos,ypos};// Robot开始的地点
int currentPixel[]; // Robot现在的位置
int postPixel[]; // 记录Robot下一个位置

public void run(){
	signal=DFS(pixel);
	
	/*当DFS的后返回值是1的时候，说明遍历Map已经结束*/
	if(signal==1){
		
	/*当Robot_Flag等于0，说明Robot可以开始移动*/	
		if(Robot_Scanner.Robot_Flag()==0){
			
		PathFinder(currentPixel,original_point); //找到Robot移动的路径
		Robot_Scanner.start(); //Robot开始移动
		
	}
		}
	/*当DFS的返回值是0的时候，说明遍历Map没有结束，但是找到了一个死角（四面都是explored）*/
	else if(signal==0){
		
	 /*当Robot等于0，说明Robot可以开始移动*/	
		if(Robot_Scanner.Robot_Flag()==0){
			
		Robot_Scanner.start(); //Robot开始移动
		}
		
		/*找到最近的一个unexplore的pixel*/
		postPixel=findNearestUnexplorePixel(currentPixel,unexplorePixel);
		/*找到Robot移动的路径*/
		PathFinder(current,postPixel);
		/*当Robot_Flag等于0，Robot可以开始移动*/
		if(Robot_Scanner.Robot_Flag()==0){
		Robot_Scanner.start(); //Robot开始移动
	}
}
 /*当DFS返回值不是0或1，递归调用run方法*/	
	else{
		run(){}
	}
	}	

/*建立DFS遍历地图的每一个pixel，返回integer*/
public int DFS(Pixel p){
	int count=0;
	return count;
			}

/*找到距离现在pixel最近的unexplore的pixel*/
public Pixel findNearestUnexplorePixel(Pixel start, Pixel end){
	return Pixel;
	}

/*找到from到to的路径*/
public void PathFinder(Pixel from, Pixel to){}




}

