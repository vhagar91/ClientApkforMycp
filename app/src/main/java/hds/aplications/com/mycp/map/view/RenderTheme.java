package hds.aplications.com.mycp.map.view;

import org.mapsforge.map.rendertheme.XmlRenderTheme;
import org.mapsforge.map.rendertheme.XmlRenderThemeMenuCallback;

import java.io.InputStream;


public class RenderTheme  implements XmlRenderTheme {
    //static private final String path = "/org/mapsforge/android/maps/rendertheme/osmarender/osmarender.xml";
    static private final String path = "/osmarender/osmarender.xml";

    private final String absolutePath = "/osmarender/";
    private final String file = "osmarender.xml";

    @Override
    public XmlRenderThemeMenuCallback getMenuCallback() {
        return null;
    }

    @Override
    public String getRelativePathPrefix() {
        return this.absolutePath;
    }

    @Override
    public InputStream getRenderThemeAsStream() {
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(this.absolutePath + this.file);
        if ( resourceAsStream == null ) {
            resourceAsStream = getClass().getResourceAsStream(this.absolutePath + this.file);
        }
        return resourceAsStream;
    }
}