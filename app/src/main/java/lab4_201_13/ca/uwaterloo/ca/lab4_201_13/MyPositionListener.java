package lab4_201_13.ca.uwaterloo.ca.lab4_201_13;

import android.graphics.PointF;

import java.lang.reflect.Field;

import mapper.MapView;
import mapper.PositionListener;

/**
 * Created by ywt on 7/9/16.
 */
public class MyPositionListener implements PositionListener{
    lab4_201_13.ca.uwaterloo.ca.lab4_201_13.Graph g;
    public MyPositionListener(lab4_201_13.ca.uwaterloo.ca.lab4_201_13.Graph g){
        this.g=g;
    }
    @Override
    public void originChanged(MapView source, PointF loc){
        source.setUserPoint(loc);
        g.setInitial(g.map.getUserPoint());

        g.map.setUserPath(g.getPath());


    }
    @Override
    public void destinationChanged(MapView source,PointF loc){
        source.setDestinationPoint(loc);

        g.setDestination(loc);

        g.map.setUserPath(g.getPath());
    }
}
