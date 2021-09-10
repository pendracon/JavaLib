package com.veetechis.lib.xml;

import java.io.InputStream;
import java.io.IOException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * Parses and processes an XML input stream for its "generic" data -- elements
 * and attributes only -- and stores the resulting data into an instance of
 * <code>XMLValueBean</code>.
 * 
 * An instance of this class reads a given <code>InputStream</code> for XML
 * document data and passes the data into the given bean instance through its
 * <code>setElementAttribute</code> and <code>setElementData</code> call-back
 * methods.  In the event of a document parsing error, and possibly a
 * warning, an exception is thrown.  In case of a warning condition, the default
 * is to throw an exception.  No other validation is performed by an instance of
 * this class, therefore the given bean instance should be able to fully
 * validate the document's data as required.
 *
 * @author      pendraconx@gmail.com
 * @see			XMLValueBean
 */
public class GenericSAXParser
{
	private XMLValueBean data;			// The composite data item to populate. 
	
	private boolean ignoreWarnings;
	private boolean validateDocument;
	private boolean validateData;
	private boolean namespaceAware;
	
	private static String LINE_SEP = System.getProperty( "line.separator" );
	

	public final static String XML_PROLOGUE = "<?xml version='1.0' encoding='ISO-8859-1' standalone='yes'?>";


	/**
	 * Creates a new default instance of <code>GenericSAXParser</code>.  Use of
	 * this constructor requires that the <code>XMLValueBean</code> instance to
	 * populate is provided through a call to <code>setXMLValueBean</code>
	 * before calling the appropriate <code>process</code> method.
	 */
	public GenericSAXParser()
	{
		super();
	}
	
	/**
	 * Creates a new instance of <code>GenericSAXParser</code> for the given
	 * <code>XMLValueBean</code> instance.  This constructor is meant as a
	 * convenience to bypass the necessity to call <code>setXMLValueBean</code>
	 * before the first use of this class.
	 *
	 * @param  bean				the composite data item to populate.
	 */
	public GenericSAXParser( XMLValueBean bean )
	{
		this();
		setXMLValueBean( bean );
	}
	
	/**
	 * Sets the given instance as the instance to populate on the next call to
	 * <code>process</code>.
	 *
	 * @param  bean				the composite data item to populate.
	 */
	public void setXMLValueBean( XMLValueBean bean )
	{
		data = bean;
	}
	
	/**
	 * If set to <code>true</code>, instructs this instance to ignore XML
	 * validation warnings during the next call to <code>process</code>.
	 *
	 * @param  ignore			the flag to ignore warnings, or not.
	 */
	public void setIgnoreWarnings( boolean ignore )
	{
		ignoreWarnings = ignore;
	}
	
	/**
	 * Returns <code>true</code> if the next call to <code>process</code> will
	 * ignore XML validation warnings.
	 *
	 * @return					the current state of the ignore warnings flag.
	 */
	public boolean isIgnoreWarnings()
	{
		return ignoreWarnings;
	}

	/**
	 * If set to <code>true</code>, instructs this instance to parse documents
	 * with namespace awarenes enabled.
	 *
	 * @param  ignore			the flag to enable namespace awareness, or not.
	 */
	public void setNamespaceAware( boolean enabled )
	{
		namespaceAware = enabled;
	}
	
	/**
	 * Returns <code>true</code> if the next call to <code>process</code> will
	 * will parse documents with namespace awareness enabled.
	 *
	 * @return					the current state of the namespace awareness
	 * 								flag.
	 */
	public boolean isNamespaceAware()
	{
		return namespaceAware;
	}

	/**
	 * If set to <code>true</code>, instructs this instance to validate the XML
	 * document while parsing it during the next call to <code>process</code>.
	 *
	 * @param  validate			the flag to enable document validation.
	 */
	public void enableDocumentValidating( boolean validate )
	{
		validateDocument = validate;
	}
	
	/**
	 * Returns <code>true</code> if the next call to <code>process</code> will
	 * validate the XML document while parsing it.
	 *
	 * @return					the state of the document validation flag.
	 */
	public boolean isDocumentValidating()
	{
		return validateDocument;
	}
	
	/**
	 * If set to <code>true</code>, instructs this instance to call the data
	 * bean's <code>validate</code> method after populating it during the next
	 * call to <code>process</code>.
	 *
	 * @param  validate			the flag to enable data validation.
	 */
	public void enableDataValidating( boolean validate )
	{
		validateData = validate;
	}
	
	/**
	 * Returns <code>true</code> if the next call to <code>process</code> will
	 * invoke the data bean's <code>validate</code> method after populating it.
	 *
	 * @return					the state of the data validation flag.
	 */
	public boolean isDataValidating()
	{
		return validateData;
	}
	
	/**
	 * Parses the given <code>java.io.InputStream</code> for XML document data
	 * and populates the currently set <code>XMLValueBean</code> instance with
	 * the data found.
	 * 
	 * If <code>isDocumentValidating()</code> evalutes to <code>true</code> then
	 * the document (stream) is validated while parsing it.
	 * 
	 * If <code>isDataValidating()</code> evaluates to <code>true</code> then
	 * the populated data bean's <code>validate</code> method is called after
	 * parsing completes.
	 * 
	 * Throws an exception if no data bean instance is set, if a non-recoverable
	 * parsing or I/O error is encountered, if document validation is enabled
	 * and a recoverable XML warning is encountered when
	 * <code>isIgnoreWarnings()</code> evaluates to <code>false</code>, or if a
	 * data validation error occurs.
	 *
	 * @param  istream			the input stream to processes.
	 * @throws					java.lang.NullPointerException
	 *							if the current bean instance is not set.
	 * @throws					XMLParseException
	 *							if an XML error or non-ignored warning occurs.
	 * @throws					XMLValidationException
	 *							if data validation fails.
	 */
	public void process( InputStream istream )
	throws XMLParseException, XMLValidationException, NullPointerException
	{
		StringBuffer msg = null;
		Exception exc = null;
		
		DefaultHandler handler = new GenericHandler();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware( isNamespaceAware() );
		factory.setValidating( isDocumentValidating() );

        try {
            SAXParser parser = factory.newSAXParser();
            parser.parse( istream, handler );
			if( isDataValidating() ) data.validate();
        }
        catch( SAXParseException e ) {
			msg = new StringBuffer().append( "SAX PARSING ERROR: " ).
				append( "Line = " ).append( e.getLineNumber() ).
				append( "; URI = " ).append( e.getSystemId() ).
				append( LINE_SEP ).
				append( "CAUSE: " ).append( e.getMessage() );
			exc = e;
        }
        catch( SAXException e ) {
			msg = new StringBuffer( "SAX ERROR: " ).append( e.getMessage() );
			exc = e;
        }
        catch( ParserConfigurationException e ) {
			msg = new StringBuffer( "PARSER CONFIGURATION ERROR: " ).
				append( e.getMessage() );
			exc = e;
        }
        catch( IOException e ) {
			msg = new StringBuffer( "I/O ERROR: " ).append( e.getMessage() );
    	    exc = e;
        }
		if( exc != null ) {
			throw new XMLParseException( msg.toString(), exc );
		}
	}


	/**
	 * SAX parsing handler for <code>GenericSAXParser</code>.  An instance of
	 * this class is used by <code>GenericSAXParser</code> to perform handling
	 * of generic SAX parsing events.  Only XML elements and attributes, parse
	 * errors, and validation warnings are handled by this class.
     *
     * @author		@AUTHOR@
	 * @version		@VERSION@
     */
    public class GenericHandler
	extends DefaultHandler
    {
		private String attributeName;
		private String attributeValue;
		private String elementName;
		private String elementValue;
		
		
		/**
		 * Called by the SAX parser to indicate an XML validation error in the
		 * document.  Re-throws the exception out to the processor.
		 */
        public void error( SAXParseException e )
        throws SAXParseException
        {
            throw e;
        }
        
		/**
		 * Called by the SAX parser to indicate an XML warning condition in the
		 * document.  If the processor is set to not ignore warning conditions,
		 * re-throws the exception out to the processor.
		 */
        public void warning( SAXParseException e )
		throws SAXParseException
        {
			if( ! isIgnoreWarnings() ) {
				throw e;
			}
        }
     
		/*
		 * Called by the SAX parser to indicate the beginning of the document
		 * has been encountered.
		 * 
		 * Currently not implemented.
        public void startDocument()
        throws SAXException
        {
			// TODO: nothing to do here yet.
        }
		 */

		/*
		 * Called by the SAX parser to indicate the end of the document has been
		 * reached.
		 * 
		 * Currently not implemented.
        public void endDocument()
        throws SAXException
        {
			// TODO: nothing to do here yet.
		}
		 */

		/**
		 * Called by the SAX parser to indicate that the opening tag of an
		 * element is encountered in the document.
		 * 
		 * Calls the <code>startElement</code> method on the currently set data
		 * bean instance.  If any attributes are present in the tag, also calls
		 * the <code>setElementAttribute</code> method on the bean for all of
		 * the element's attributes.
		 * 
		 * Throws an exception if the bean instance indicates an error.
		 */
        public void startElement(
			String nsURI, String sName, String qName, Attributes attrs )
        throws SAXException
        {
			elementName = findElementName( sName, qName );
			data.startElement( elementName );
			
            if( attrs != null ) {
                for( int i = 0; i < attrs.getLength(); i++ ) {
                    attributeName = attrs.getLocalName( i );
                    if( attributeName.equals( "" ) ) {
						attributeName = attrs.getQName( i );
					}
					attributeValue = attrs.getValue( i );
					data.setElementAttribute(
						elementName, attributeName,	attributeValue );
                }
            }
        }

		/**
		 * Called by the SAX parser to indicate the end of an element has been
		 * reached.  Calls the <code>endElement</code> method on the currently
		 * set data bean instance.
		 */
        public void endElement( String nsURI, String sName, String qName )
        throws SAXException
        {
			elementName = findElementName( sName, qName );
			data.endElement( elementName );
			elementName = null;
		}

		/**
		 * Called by the SAX parser to pass the character data of the just
		 * encountered element.  Calls the <code>setElementData</code> method on
		 * the currently set data bean instance to set the data.
		 * 
		 * Throws an exception if the bean instance indicates an error.
		 */
        public void characters( char buf[], int offset, int len )
	    throws SAXException
        {
			if( elementName != null ) {
	            elementValue = new String( buf, offset, len ).trim();
				data.setElementData( elementName, elementValue );
			}
        }

		
		/*
		 * Returns the correct element name based upon namespace awareness of
		 * the document.
		 */
		private String findElementName( String sName, String qName )
		{
			elementName = sName;
            if( elementName.equals( "" ) ) {
				elementName = qName;	// Not namespace aware
			}
			return elementName;
		}
		
	} // end of GenericHandler

} // end of GenericSAXParser

