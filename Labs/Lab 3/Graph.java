import java.lang.IllegalArgumentException;
import java.lang.IndexOutOfBoundsException;

public class Graph{
	private final int V;
	private int E;
	private Bag<Integer>[] edges;

	public Graph(int v){
		if(v<0) throw new IllegalArgumentException("Number of vertices must be nonnegative");
		this.V = v;
		this.E = 0;
		edges = (Bag<Integer>[]) new Bag[V];
		for(int i=0 ;i<V;i++){
			edges[i] = new Bag<Integer>();
		}
	}


	public int V(){
		return V;
	}

	public int E(){
		return E;
	}
	public Bag<Integer>[] edges(){
		return edges;
	}
	public boolean isVertex(int v){
		if(v<0 || v>= V) throw new IndexOutOfBoundsException("vertex v is invalid");
		return true;

	}
	public void addEdge(int v, int x){
		isVertex(v);
		isVertex(x);
		// if(duplicate(v,x)){
		// 	return;
		// }
		E++;
		edges[x].add(v);
	}
	public boolean duplicate(int x, int y){
		Bag<Integer> a = edges[x];
		for(Integer b : a){
			if(y == b){
				return true;
			}
		}
		return false;
	}
	public String printEdges(){
    StringBuilder s = new StringBuilder();
    s.append(V + " vertices, " + E + " edges " + "\n");
    for (int v = 0; v < V; v++) {
        s.append(v + ": ");
        for (int w : edges[v]) {
            s.append(w + " ");
        }
        s.append("\n");
    }
    return s.toString();
	}
}