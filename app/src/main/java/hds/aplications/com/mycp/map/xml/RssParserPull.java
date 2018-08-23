package hds.aplications.com.mycp.map.xml;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class RssParserPull
{
    private File xmlFile;

    public RssParserPull(File xmlFile)
    {
        this.xmlFile = xmlFile;
    }

    public List<POI> parse()
    {
        List<POI> pois = null;
        XmlPullParser parser = Xml.newPullParser();
        try
        {
            parser.setInput(this.getInputStream(), null);
            int evento = parser.getEventType();
            POI POIActual = null;

            while (evento != XmlPullParser.END_DOCUMENT)
            {
                String etiqueta = null;

                switch (evento)
                {
                    case XmlPullParser.START_DOCUMENT:
                        pois = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        etiqueta = parser.getName();
                        if (etiqueta.equals("node")){
                            double latitude = Double.parseDouble(parser.getAttributeValue(null, "lat"));
                            double longitude = Double.parseDouble(parser.getAttributeValue(null, "lon"));
                            POIActual = new POI(latitude, longitude);
                        }
                        else if (POIActual != null)
                        {
                            String attribute = parser.getAttributeValue(null, "k");

                            if (etiqueta.equals("tag") &&  attribute != null && attribute.equals("name"))
                            {
                                POIActual.setName(parser.getAttributeValue(null, "v"));
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        etiqueta = parser.getName();
                        if (etiqueta.equals("node") && POIActual != null)
                        {
                            pois.add(POIActual);
                        }
                        break;
                }

                evento = parser.next();
            }
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }

        return pois;
    }

    private InputStream getInputStream()
    {
        try
        {
            return new FileInputStream(xmlFile);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}