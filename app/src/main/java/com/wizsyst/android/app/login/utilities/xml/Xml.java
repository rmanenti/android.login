package com.wizsyst.android.app.login.utilities.xml;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by rmanenti on 22/04/2016.
 */
public class Xml {

    public static String TAG = "com.android.rmanenti.utility.XML";

    /**
     * Create a Document object for the given XML String.
     * @param data string
     * */
    public static Document getDomElement( String data ){

        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {

            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream( new StringReader( data ) );

            doc = db.parse( is );

            if ( doc != null ) {
                doc.getDocumentElement().normalize();
            }
        }
        catch ( ParserConfigurationException e ) {

            Log.e( TAG, e.getMessage() );
            return null;
        }
        catch ( SAXException e ) {

            Log.e( TAG, e.getMessage() );
            return null;
        }
        catch ( IOException e ) {

            Log.e( TAG, e.getMessage() );
            return null;
        }

        return doc;
    }

    public static boolean hasNode( Node node, String name, boolean recursive ) {

        boolean found = false;

        if ( node != null && ( name != null && !name.isEmpty() ) ) {

            if ( node.getNodeName().equals( name ) ) {
                found = true;
            }
            else {

                if ( node.hasChildNodes() ) {
                    found =  Xml.hasNode( node.getFirstChild(), name, recursive );
                }

                if ( !found ) {

                    while (node.getNextSibling() != null) {
                        return Xml.hasNode(node.getNextSibling(), name, recursive);
                    }
                }
            }
        }

        return found;
    }

    public static Node getFirstNode( Document doc ) {

        Node node      = null;
        NodeList nodes = null;

        if ( doc != null && doc.hasChildNodes() ) {

            nodes = doc.getChildNodes();

            for ( int i = 0, l = nodes.getLength(); i < l; i++ ) {

                node = nodes.item( i );

                if ( node.getNodeType() == Node.TEXT_NODE ) {
                    return node;
                }
            }
        }

        return node;
    }

    /** Getting node value
     * @param elem element
     */
    public static String getElementValue( Node elem ) {

        Node child;

        if ( elem != null) {

            if ( elem.hasChildNodes() ) {

                for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ) {

                    if ( child.getNodeType() == Node.TEXT_NODE  ) {
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }

    /**
     * Getting node value
     * @param item node
     * @param str string
     * */
    public static String getValue( Element item, String str ) {

        NodeList n = item.getElementsByTagName( str );
        return Xml.getElementValue( n.item( 0 ) );
    }
}
