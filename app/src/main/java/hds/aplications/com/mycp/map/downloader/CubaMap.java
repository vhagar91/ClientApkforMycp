package hds.aplications.com.mycp.map.downloader;

import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;

import hds.aplications.com.mycp.map.marker.CustomMarkerModel;



public class CubaMap extends AbstractMap {

    protected CubaMap() {
    }

    @Override
    protected String getDirectoryName() {
        return "cubafiles";
    }

    @Override
    protected long getMapSize() {
        return 48132766;

        /* 1 megabyte (MB) ----> 1048576 byte (B) */ //50331648
                                                     //48132766 calculo por la consola
        /*
        $ du -bsh /fichero_o_carpeta

        Du tiene más opciones, pero en este caso uso estas 3:
        -b [–bytes]: Mostrar en bytes.
        -s [–sumarize]: Mostrar solamente el tamaño total de cada argumento.
        -h [–human-readable]: Imprime los tamaños de forma leíble (e.g., 1K, 234M, 2G)
        */
    }

    @Override
    protected String getMapFileUrl() {
        return "http://cf.lvps92-51-151-239.dedicated.hosteurope.de/map";
    }

    @Override
    protected String getEdgesUrl() {
        return "http://cf.lvps92-51-151-239.dedicated.hosteurope.de/edges";
    }

    @Override
    protected String getGeometryUrl() {
        return "http://cf.lvps92-51-151-239.dedicated.hosteurope.de/geometry";
    }

    @Override
    protected String getLocationIndexUrl() {
        return "http://cf.lvps92-51-151-239.dedicated.hosteurope.de/locationIndex";
    }

    @Override
    protected String getNamesUrl() {
        return "http://cf.lvps92-51-151-239.dedicated.hosteurope.de/names";
    }

    @Override
    protected String getNodesUrl() {
        return "http://cf.lvps92-51-151-239.dedicated.hosteurope.de/nodes";
    }

    @Override
    protected String getPropertiesUrl() {
        return "http://cf.lvps92-51-151-239.dedicated.hosteurope.de/properties";
    }

    @Override
    public GeoPoint getCenterGeoPoint() {
        return new GeoPoint(23.140108, -82.37125);
    }

    @Override
    protected CustomMarkerModel[] getCustomMarkerModels() {
        return new CustomMarkerModel[]{
               //new CustomMarkerModel("Bacardi", R.mipmap.home, 23.141327,-82.355168),
                 /*new CustomMarkerModel("Virtudes", R.mipmap.pin_a_green, 23.139087, -82.366744),
                new CustomMarkerModel("Almejeiras", R.mipmap.pin_a_orange, 23.140108, -82.37125)*/
        };
    }

    @Override
    public int getDefaultZoom() {
        return 16;
    }

    @Override
    public BoundingBoxE6 getExtent() {
        double north = 23.725012;
        double east  =  -73.894043;

        double south = 19.580493;
        double west  =  -85.231934;

        return new BoundingBoxE6(north, east, south, west);
    }
}
