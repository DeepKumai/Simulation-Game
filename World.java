import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.StringTokenizer; 

abstract class Animals implements Comparable<Animals> {  //Declare this class as abstract class
	private double x;
	private double y;
	private int timestamp;
	private double health;
	private String order;
	private String Kill;
	Animals(double x,double y,double health,int timestamp){
		this.x=x;
		this.y=y;
		this.health=health;
		this.timestamp=timestamp;
		this.Kill=null;
	}
//	Method to be overridden in derived class
	public abstract void simulation(int n, ArrayList<grassland> g,ArrayList<carnivore> C, ArrayList<Herbivore> H);
	
	public double[] _new(grassland g, int r){
		double[] n=new double[2];
		double theta=Math.atan((this.getY()-g.Y)/(this.getX()-g.X));
		n[0]=this.getX()-(r*Math.cos(theta));
		n[1]=this.getY()-(r*Math.sin(theta));
		return n;
	}
	public double[] _newc(carnivore c, int r){
		double[] n=new double[2];
		double theta=Math.atan((this.getY()-c.getY())/(this.getX()-c.getX()));
		n[0]=this.getX()+(r*Math.cos(theta));
		n[1]=this.getY()+(r*Math.sin(theta));
		return n;
	}
	public String getKill() {
		return Kill;
	}
	public void setKill(String kill) {
		Kill = kill;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public int getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}
	public double getHealth() {
		return health;
	}
	public void setHealth(double health) {
		this.health = health;
	}
	public int compareTo(Animals a2) {
		// TODO Auto-generated method stub
		if(this.timestamp>a2.timestamp){
			return 1;
		}else if(this.timestamp<a2.timestamp){
			return -1;
		}else{
			if(this.health<a2.health){
				return 1;
			}else if(this.health>a2.health){
				return -1;
			}else{
				if(a2.getClass().getName().equals("Herbivore")){
					return 1;
				}else if(a2.getClass().getName().equals("carnivore")){
					return -1;
				}else{
					double m1=Math.sqrt((this.getX()*this.getX())+(this.getY()*this.getY()));
					double m2=Math.sqrt((a2.getX()*a2.getX())+(a2.getY()*a2.getY()));
					if(m1>m2){
						return 1;
					}else if(m1<m2){
						return -1;
					}
				}
			}
		}
		return 0;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public grassland nearest_land(grassland g1, grassland g2) {
		double n1=Math.pow(g1.X-this.getX(), 2)+Math.pow(g1.Y-this.getY(), 2);
		double n2=Math.pow(g2.X-this.getX(), 2)+Math.pow(g2.Y-this.getY(), 2);
		if(n1<=n2){
			return g1;
		}else{
			return g2;
		}
	}
	public carnivore nearest_carn(carnivore c1, carnivore c2) {
		double n1=Math.pow(c1.getX()-this.getX(), 2)+Math.pow(c1.getY()-this.getY(), 2);
		double n2=Math.pow(c2.getX()-this.getX(), 2)+Math.pow(c2.getY()-this.getY(), 2);
		if(n1<=n2){
			return c1;
		}else{
			return c2;
		}
	}
	public Herbivore nearest_herb(Herbivore c1, Herbivore c2) {
		double n1=Math.pow(c1.getX()-this.getX(), 2)+Math.pow(c1.getY()-this.getY(), 2);
		double n2=Math.pow(c2.getX()-this.getX(), 2)+Math.pow(c2.getY()-this.getY(), 2);
		if(n1<=n2){
			return c1;
		}else{
			return c2;
		}
	}
	
	
}
class Herbivore extends Animals{

	private double capacity;
	protected int turn;
	Herbivore(double x, double y,double health, int timestamp,double capacity,String order) {
		super(x, y, health, timestamp);
		this.capacity=capacity;
		this.setOrder(order);
		turn=0;
	}
	public double getCapacity() {
		return capacity;
	}

	@Override
	public void simulation(int n, ArrayList<grassland> g,ArrayList<carnivore> C, ArrayList<Herbivore> H) {
		// TODO Auto-generated method stub
		Random rand = new Random();
//		int  n = rand.nextInt(100) + 1;
		if(n==0){
			if(inside_grassland(g.get(0))){
				int  n1 = rand.nextInt(100) + 1;
				if(n1<=50){
					double[] N=super._new(g.get(1),5);
					this.setX(N[0]);
					this.setY(N[1]);
					this.setHealth(this.getHealth()-25);
				}
			}else if(inside_grassland(g.get(1))){
				int  n1 = rand.nextInt(100) + 1;
				if(n1<=50){
					double[] N=super._new(g.get(0),5);
					this.setX(N[0]);
					this.setY(N[1]);
					this.setHealth(this.getHealth()-25);
				}
			}
		}else{
			if(!inside_grassland(g.get(0)) && !inside_grassland(g.get(1))){
				this.turn++;
				int  n2 = rand.nextInt(100) + 1;
				if(n2>5){
					int  n1 = rand.nextInt(100) + 1;
					if(n1<=65){
						grassland n_g=super.nearest_land(g.get(0),g.get(1));
						double[] N=super._new(n_g,5);
						this.setX(N[0]);
						this.setY(N[1]);
						if(this.turn>7){
							this.setHealth(this.getHealth()-5);
						}
						
					}else{
						if(!C.isEmpty()){
							carnivore c;
							if(C.size()==2){
								c=super.nearest_carn(C.get(0),C.get(1));
							}else{
								c=C.get(0);
							}
							double[] N=super._newc(c,5);
							this.setX(N[0]);
							this.setY(N[1]);
	
						}if(this.turn>7){
							this.setHealth(this.getHealth()-5);
						}
					}	
				}else{
					grassland ng;
					if(inside_grassland(g.get(0))){
						ng=g.get(0);
					}else{
						ng=g.get(1);
					}
					if(ng.grass>=this.getCapacity()){
						int  n1 = rand.nextInt(100) + 1;
						if(n1<=90){
							this.setHealth(this.getHealth()+(0.5*this.getHealth()));
						}else{
							int  n3 = rand.nextInt(100) + 1;
							if(n3<=50){
								if(!C.isEmpty()){
									carnivore c;
									if(C.size()==2){
										c=super.nearest_carn(C.get(0),C.get(1));
									}else{
										c=C.get(0);
									}
									double[] N=super._newc(c,2);
									this.setX(N[0]);
									this.setY(N[1]);
							}else{
								grassland n_g=super.nearest_land(g.get(0),g.get(1));
								double[] N=super._new(n_g,3);
								this.setX(N[0]);
								this.setY(N[1]);
								}this.setHealth(this.getHealth()-25);
							}
						}
					}else{
						int  n1 = rand.nextInt(100) + 1;
						if(n1<=20){
							this.setHealth(this.getHealth()+(0.2*this.getHealth()));
						}else{
							int  n3 = rand.nextInt(100) + 1;
							if(n3<=70){
								if(!C.isEmpty()){
									carnivore c;
									if(C.size()==2){
										c=super.nearest_carn(C.get(0),C.get(1));
									}else{
										c=C.get(0);
									}
									double[] N=super._newc(c,4);
									this.setX(N[0]);
									this.setY(N[1]);
							}else{
								grassland n_g=super.nearest_land(g.get(0),g.get(1));
								double[] N=super._new(n_g,2);
								this.setX(N[0]);
								this.setY(N[1]);
								}this.setHealth(this.getHealth()-25);
							}
						}
					}
				}
			}
		}
	}
	private boolean inside_grassland(grassland g) {
		// TODO Auto-generated method stub
		double g1=Math.pow(g.X-this.getX(), 2)+Math.pow(g.Y-this.getY(), 2);
		if(g1<=Math.pow(g.radius,2)){
			return true;
		}
		return false;
	}
	
}
class carnivore extends Animals{
	protected int turn;
	carnivore(double x, double y,double health, int timestamp,String order) {
		super(x, y, health, timestamp);
		this.setOrder(order);
		this.turn=0;
	}
	public double[] _newc(Herbivore c, int r){
		double[] n=new double[2];
		double theta=Math.atan((this.getY()-c.getY())/(this.getX()-c.getX()));
		n[0]=this.getX()-(r*Math.cos(theta));
		n[1]=this.getY()-(r*Math.sin(theta));
		return n;
	}
	@Override
	public void simulation(int n, ArrayList<grassland> g,ArrayList<carnivore> C, ArrayList<Herbivore> H) {
		// TODO Auto-generated method stub
		Random rand=new Random();
		if(n==0){
			if(!inside_grassland(g.get(0)) && !inside_grassland(g.get(1))){
				this.setHealth(this.getHealth()-60);
			}else{
				this.setHealth(this.getHealth()-30);
			}
		}else{
			if(H.size()==2){
				double l1=Math.pow(H.get(0).getX()-this.getX(), 2)+Math.pow(H.get(0).getY()-this.getY(), 2);
				double l2=Math.pow(H.get(1).getX()-this.getX(), 2)+Math.pow(H.get(1).getY()-this.getY(), 2);
				if(l1>25 && l2>25){
					turn++;
				}
				else if(l1<=1 && l2<=1){
					if(l1<=l2){
						this.setKill(H.get(0).getOrder());
						this.setHealth(this.getHealth()+((2/3)*H.get(0).getHealth()));
						H.remove(0);
					}else{
						this.setKill(H.get(1).getOrder());
						this.setHealth(this.getHealth()+((2/3)*H.get(1).getHealth()));
						H.remove(1);
					}
				}
			}else if(H.size()==1){
				double l1=Math.pow(H.get(0).getX()-this.getX(), 2)+Math.pow(H.get(0).getY()-this.getY(), 2);
				if(l1>25){
					turn++;
				}
				else if(l1<=1){
					this.setKill(H.get(0).getOrder());
					this.setHealth(this.getHealth()+((2/3)*H.get(0).getHealth()));
					H.remove(0);
				}
			}else if(!inside_grassland(g.get(0)) && !inside_grassland(g.get(1))){
				int n1=rand.nextInt(100)+1;
				if(n1<=92){
					if(!H.isEmpty()){
						Herbivore h;
						if(H.size()==2){
							h=super.nearest_herb(H.get(0),H.get(1));
						}else{
							h=H.get(0);
						}
						double[] N=_newc(h,4);
						this.setX(N[0]);
						this.setY(N[1]);
						double l1=Math.pow(H.get(0).getX()-this.getX(), 2)+Math.pow(H.get(0).getY()-this.getY(), 2);
						if(H.size()==2){
							double l2=Math.pow(H.get(1).getX()-this.getX(), 2)+Math.pow(H.get(1).getY()-this.getY(), 2);
							if(l1>25 && l2>25){
								if(turn>7){
									this.setHealth(this.getHealth()-6);
								}
							}
						}
					}
				}else{
					this.setHealth(this.getHealth()-60);
				}
			}else if(inside_grassland(g.get(0)) || inside_grassland(g.get(1))){
				int n1=rand.nextInt(100)+1;
				if(n1<=25){
					this.setHealth(this.getHealth()-30);
				}else{
					if(!H.isEmpty()){
						Herbivore h;
						if(H.size()==2){
							h=super.nearest_herb(H.get(0),H.get(1));
						}else{
							h=H.get(0);
						}
						double[] N=_newc(h,4);
						this.setX(N[0]);
						this.setY(N[1]);

					}
				}
				double l1=Math.pow(H.get(0).getX()-this.getX(), 2)+Math.pow(H.get(0).getY()-this.getY(), 2);
				if(H.size()==2){
					double l2=Math.pow(H.get(1).getX()-this.getX(), 2)+Math.pow(H.get(1).getY()-this.getY(), 2);
					if(l1>25 && l2>25){
						if(turn>7){
							this.setHealth(this.getHealth()-6);
						}
					}
				}
			}
		}
	}
	
	private boolean inside_grassland(grassland g) {
		// TODO Auto-generated method stub
		double g1=Math.pow(g.X-this.getX(), 2)+Math.pow(g.Y-this.getY(), 2);
		if(g1<=Math.pow(g.radius,2)){
			return true;
		}
		return false;
	}
	
}
class grassland{
	int X;
	int Y;
	int radius;
	int grass;
	grassland(int X,int Y,int radius,int grass){
		this.X=X;
		this.Y=Y;
		this.radius=radius;
		this.grass=grass;
	}
}
public class World {
	

	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		BufferedReader b=new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter Total Final Time for Simulation:");
		int time =Integer.parseInt(b.readLine());
		ArrayList<grassland> G=new ArrayList<grassland>();
		ArrayList<carnivore> C=new ArrayList<carnivore>();
		ArrayList<Herbivore> H=new ArrayList<Herbivore>();
		int max_time=0;
		PriorityQueue<Animals> pq = new PriorityQueue<Animals>();
		System.out.println("Enter x, y centre, radius and Grass Available for First Grassland:");
		StringTokenizer s1=new StringTokenizer(b.readLine());
		G.add(new grassland(Integer.parseInt(s1.nextToken()),Integer.parseInt(s1.nextToken()),Integer.parseInt(s1.nextToken()),Integer.parseInt(s1.nextToken())));
		System.out.println("Enter x, y centre, radius and Grass Available for Second Grassland:");
		StringTokenizer s2=new StringTokenizer(b.readLine());
		G.add(new grassland(Integer.parseInt(s2.nextToken()),Integer.parseInt(s2.nextToken()),Integer.parseInt(s2.nextToken()),Integer.parseInt(s2.nextToken())));

//		Herbivore object declaration
		
		System.out.println("Enter Health and Grass Capacity for Herbivores:");
		StringTokenizer s3=new StringTokenizer(b.readLine());
		double herb_health=Double.parseDouble(s3.nextToken());	//Herbivore's Health
		double grass_cap=Double.parseDouble(s3.nextToken());	//Herbivore's grass capacity
		System.out.println("Enter x, y position and timestamp for First Herbivore:");
		StringTokenizer s4=new StringTokenizer(b.readLine());
		Herbivore herbivore1=(new Herbivore(Double.parseDouble(s4.nextToken()),Double.parseDouble(s4.nextToken()),herb_health,Integer.parseInt(s4.nextToken()),grass_cap,"First"));
		pq.add(herbivore1);H.add(herbivore1);
		if(herbivore1.getTimestamp()>max_time){
			max_time=herbivore1.getTimestamp();
		}
		System.out.println("Enter x, y position and timestamp for Second Herbivore:");
		StringTokenizer s5=new StringTokenizer(b.readLine());
		Herbivore herbivore2=(new Herbivore(Double.parseDouble(s5.nextToken()),Double.parseDouble(s5.nextToken()),herb_health,Integer.parseInt(s5.nextToken()),grass_cap,"Second"));
		pq.add(herbivore2);H.add(herbivore2);
		if(herbivore2.getTimestamp()>max_time){
			max_time=herbivore2.getTimestamp();
		}
		
//		Carnivore object declaration
		
		System.out.println("Enter Health for Carnivores:");
		double carn_health=Double.parseDouble(b.readLine());
		System.out.println("Enter x, y position and timestamp for First Carnivore:");
		StringTokenizer s6=new StringTokenizer(b.readLine());	//Carnivore's Health
		carnivore carnivore1=(new carnivore(Double.parseDouble(s6.nextToken()),Double.parseDouble(s6.nextToken()),carn_health,Integer.parseInt(s6.nextToken()),"First"));	
		pq.add(carnivore1);C.add(carnivore1);
		if(carnivore1.getTimestamp()>max_time){
			max_time=carnivore1.getTimestamp();
		}
		System.out.println("Enter x, y position and timestamp for Second Carnivore:");
		StringTokenizer s7=new StringTokenizer(b.readLine());
		carnivore carnivore2=(new carnivore(Double.parseDouble(s7.nextToken()),Double.parseDouble(s7.nextToken()),carn_health,Integer.parseInt(s7.nextToken()),"Second"));
		pq.add(carnivore2);C.add(carnivore2);
		if(carnivore2.getTimestamp()>max_time){
			max_time=carnivore2.getTimestamp();
		}
		System.out.println("The Simulation Begins -");
//		for(int i=0;i<4;i++){
//			System.out.println(pq.poll().getTimestamp());
//		}
		Random rand1=new Random();
		int noh=2;	//Number of herbivore
		int noc=2;	//Number of carnivore
		int j=0;
		while(!pq.isEmpty() && (j++)<time){
			Animals a=pq.poll();
			System.out.println("It is "+a.getOrder()+" "+a.getClass().getName());
			if(a.getClass().getName().equals("Herbivore")){
				a.simulation(noc,G,C,H);
				a.setTimestamp(rand1.nextInt(time-(max_time+1)) + max_time+1);
				if(a.getTimestamp()>max_time && a.getTimestamp()<time-1){
					max_time=a.getTimestamp();
				}
				if(a.getHealth()<=0 || a.getTimestamp()>=(time-1)){
					System.out.println("It is dead");
					continue;
				}
				
				pq.add(a);
				System.out.println("It's  health after taking turn is "+a.getHealth());
			}else{
				a.simulation(noh,G,C,H);
				if(a.getKill()!=null){
					ArrayList<Animals> A=new ArrayList<Animals>();
					while(!pq.isEmpty()){
						A.add(pq.poll());
					}
					int i=0;
					while(i<A.size()){
						if((A.get(i).getKill().equals(a.getOrder())) && (A.get(i).getClass().equals("Herbivore"))){
							continue;
						}pq.add(A.get(i));
						i++;
					}
				}
				a.setTimestamp(rand1.nextInt(time-(max_time+1)) + max_time+1);
				if(a.getTimestamp()>max_time && a.getTimestamp()<time-1){
					max_time=a.getTimestamp();
				}
				if(a.getHealth()<=0 || a.getTimestamp()>=(time-1)){
					System.out.println("It is dead");
					continue;
				}
				pq.add(a);
				System.out.println("It's  health after taking turn is "+a.getHealth());
			}
		}
		
	}
}

