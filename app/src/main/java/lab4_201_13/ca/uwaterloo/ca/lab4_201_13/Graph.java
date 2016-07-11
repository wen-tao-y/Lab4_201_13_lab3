package lab4_201_13.ca.uwaterloo.ca.lab4_201_13;

import android.graphics.PointF;
import android.util.Log;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import mapper.MapView;
import mapper.NavigationalMap;
import mapper.VectorUtils;

/**
 * Created by ywt on 7/7/16.
 */
public class Graph {
    LinkedList<PointF> path = new LinkedList<>();

    public MapView map;
    LinkedList<Vertex> V = new LinkedList<>();
    LinkedList<Vertex> Vvertical;
    float[][] edge;
    PointF destination;
    int start=0;
    PointF size=new PointF();
    PointF initial=new PointF();
    LinkedList<Edge> E = new LinkedList<>();
    LinkedList<LinkedList<Integer>> adj=new LinkedList<>();
    public Graph(MapView map){
        this.map=map;
        List<List<PointF>> paths=map.map.getPaths();
        Log.d(paths.toString(), "");
        for(List<PointF> path:paths){
            Log.d("", path.toString());
            for(PointF p:path){
                if(size.x<p.x){
                    size.set(p.x,size.y);
                }
                if(size.y<p.y){
                    size.set(size.x,p.y);
                }
            }
        }
        Log.d("w and h:", String.format("%f,%f",size.x,size.y)+paths.toString());
        for(int i=0;i<(size.x);i+=1){
            for(int j=0;j<size.y;j+=1){
                V.add(new Vertex(new PointF(i,j)));
                Log.d("",V.getLast().p.toString());
            }
        }

        Vvertical=(LinkedList<Vertex>) V.clone();

        while(Vvertical.isEmpty()) {
            Collections.sort(Vvertical, new Comparator<Vertex>() {
                @Override
                public int compare(Vertex v1, Vertex v2) {
                    if (v1.p.x > v2.p.x) {
                        return 1;
                    } else if (v1.p.x < v2.p.x) {
                        return -1;
                    } else {
                        if (v1.p.y > v2.p.y) {
                            return 1;
                        } else if (v1.p.y < v2.p.y) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                }
            });
        }
        edge=new float[V.size()][V.size()];
        for(int i=0;i<V.size();i++){
            for(int j=0;j<V.size();j++){
                edge[i][j]=Float.POSITIVE_INFINITY;
            }
        }
        /*for(int i=0;i<V.size();i++){
            for(int j=0;j<V.size();j++){
                if(map.map.calculateIntersections(V.get(i).p,V.get(j).p).isEmpty()){
                    E.add(new Edge(V.get(i),V.get(j)));

                    edge[i][j]=PointF.length(V.get(j).p.x-V.get(i).p.x,V.get(j).p.y-V.get(i).p.y);

                }
                else{
                    edge[i][j]=Float.POSITIVE_INFINITY;
                }
            }
        }
        */
        for(Vertex v:V){
            for(Vertex u:V){
                if(VectorUtils.distance(u.p,v.p)==1&&map.map.calculateIntersections(u.p,v.p).isEmpty()){
                    edge[V.indexOf(u)][V.indexOf(v)]=VectorUtils.distance(u.p,v.p);
                    edge[V.indexOf(v)][V.indexOf(u)]=VectorUtils.distance(u.p,v.p);

                }
                
            }
        }

        /*
        for(int i=0;i<V.size()-1;i++){
            if(!map.map.calculateIntersections(V.get(i).p,V.get(i+1).p).isEmpty()){
                edge[i][i+1]=VectorUtils.distance(V.get(i).p,V.get(i+1).p);

                edge[i+1][i]=VectorUtils.distance(V.get(i).p,V.get(i+1).p);
                //Log.d("EDGE X:", String.format("[%d][%d]=%f",i,i+1,edge[i][i+1]));
            }
            else{
                //Log.d("EDGE X:", String.format("[%d][%d]=%f",i,i+1,edge[i][i+1]));

            }
        }
        for(int i=0;i<Vvertical.size()-1;i++){
            if(!map.map.calculateIntersections(Vvertical.get(i).p,Vvertical.get(i+1).p).isEmpty()){
                edge[V.indexOf(Vvertical.get(i))][V.indexOf(Vvertical.get(i+1))]=VectorUtils.distance(Vvertical.get(i).p,Vvertical.get(i+1).p);

                edge[V.indexOf(Vvertical.get(i+1))][V.indexOf(Vvertical.get(i))]=VectorUtils.distance(Vvertical.get(i).p,Vvertical.get(i+1).p);
                //Log.d("EDGE Y:", String.format("[%d][%d]=%f",V.indexOf(Vvertical.get(i)),V.indexOf(Vvertical.get(i+1)),edge[V.indexOf(Vvertical.get(i+1))][V.indexOf(Vvertical.get(i))]));
            }
            else{

                //Log.d("EDGE Y:", String.format("[%d][%d]=%f",V.indexOf(Vvertical.get(i)),V.indexOf(Vvertical.get(i+1)),edge[V.indexOf(Vvertical.get(i+1))][V.indexOf(Vvertical.get(i))]));
            }

        }
        */
        for(int i=0;i<V.size();i++){
            for(int j=i+1;j<V.size();j++){
                if(edge[i][j]!=Float.POSITIVE_INFINITY)
                Log.d("EDGE Y:", String.format("[%d][%d]=%f",i,j,edge[i][j]));
            }
        }

        /*for(int i=0;i<V.size()/size.y;i++){
            for(int j=0;j<V.size()/(int)size.x-(int)size.x;j+=(int)size.x){
                if(!map.map.calculateIntersections(V.get(i+j).p,V.get(i+j+(int)size.x+1).p).isEmpty()){
                    edge[i+j+(int)size.x][i+j]=VectorUtils.distance(V.get(i+j+(int)size.x).p,V.get(i+j).p);
                    edge[i+j][i+j+(int)size.x]=VectorUtils.distance(V.get(i+j+(int)size.x).p,V.get(i+j).p);
                    Log.d("EDGE Y:", String.format("[%d][%d]=%f",i+j,i+j+(int)size.x,edge[i+j+(int)size.x][i+j]));
                }
                else{
                    edge[i+j+(int)size.x][i+j]=Float.POSITIVE_INFINITY;
                    edge[i+j][i+j+(int)size.x]=Float.POSITIVE_INFINITY;
                    Log.d("EDGE Y:", String.format("[%d][%d]=%f",i+j,i+j+(int)size.x,edge[i+j+(int)size.x][i+j]));
                }
            }
        }

*/

        //form adj list
        for(int i=0;i<V.size();i++){
            adj.add(new LinkedList<Integer>());
            //Log.d("", adj.toString());
            for(int j=0;j<V.size();j++){
                if(edge[i][j]!=Float.POSITIVE_INFINITY){
                    adj.get(i).add(j);
                }
            }
        }



        //s=String.format("%d",E.size());
        //Log.d("", s);

        //find starting vertex




    }
    public  void setInitial(PointF p){
        initial=p;


        float d=Float.POSITIVE_INFINITY;
        for(Vertex v:V){
            float x= VectorUtils.distance(v.p,initial);
            if(x<d){
                start=V.indexOf(v);
                d=x;
            }
        }
        //Log.d(V.get(start).p.toString(),"\n"+p.toString());
        dijkstra(start);

    }
    private void initializeSingleSource(int s){
        for(Vertex v:V){
            v.d=Float.POSITIVE_INFINITY;
            v.pi=null;

        }
        V.get(s).d=0;
        //Log.d("", String.format("%d",s));
    }
    private void relax(int u, int v){
        if (V.get(v).d>V.get(u).d+edge[u][v]){
            V.get(v).d=V.get(u).d+edge[u][v];
            V.get(v).pi=V.get(u);
        }


    }
    public void dijkstra(int s){
        initializeSingleSource(s);

        LinkedList<Vertex> S=new LinkedList<>();
        LinkedList<Vertex> Q=(LinkedList<Vertex>) V.clone();

        while(!Q.isEmpty()){
            Collections.sort(Q, new Comparator<Vertex>() {
                @Override
                public int compare(Vertex v1, Vertex v2) {
                    if(v1.d>v2.d){
                        return 1;
                    }
                    else if(v1.d<v2.d){
                        return -1;
                    }
                    else {
                        return 0;
                    }
                }
            });
            //String out=String.format(Q.toString());
            //Log.d("Queue:", out);
            Vertex u=Q.pollFirst();

            S.add(u);
            for(int i:adj.get(V.indexOf(u))){
                relax(V.indexOf(u),i);
            }
            //out=String.format("Vertex %d, pi:%d, d: %f\n"+u.p.toString(), V.indexOf(u),V.indexOf(u.pi),u.d);
            //Log.d("Dijkstra", out);

        }

    }
    public  void setDestination(PointF p){
        destination=p;
    }
    LinkedList<PointF> getPath(){
        if(initial!=null){
            int end = 0;
            float d=Float.POSITIVE_INFINITY;
            for(Vertex v:V){
                float x=VectorUtils.distance(v.p,destination);
                if(x<d){
                    end=V.indexOf(v);
                    d=x;
                }
            }
            Vertex endVertex=V.get(end);
            Vertex iterator=endVertex;
            LinkedList<PointF> path=new LinkedList<>();
            path.add(destination);
            if(endVertex.pi==null){
                return null;
            }
            path.add(iterator.p);
            while(iterator.pi!=null){
                path.add(iterator.pi.p);
                iterator=iterator.pi;
            }
            path.add(initial);
            String out=path.toString();
            this.path=path;
            Log.d("path: ", out);
            return path;



        }
        else{
            return null;
        }
    }
}
class Edge{
    LinkedList<Vertex> e=new LinkedList<>();
    float weight;
    public  Edge(Vertex x, Vertex y){
        e.add(x);
        e.add(y);
        weight=PointF.length(x.p.x-y.p.x,x.p.y-y.p.y);

    }
}
class Vertex{
    PointF p;
    Vertex pi;
    float d;
    public Vertex(PointF p){
        this.p=p;
    }
}

