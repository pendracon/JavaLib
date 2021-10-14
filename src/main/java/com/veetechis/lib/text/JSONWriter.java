package com.veetechis.lib.text;

import java.io.StringWriter;


/**
 * A simple JSON object writer.
 * 
 * @author	pendraconx@gmail.com
 */
public class JSONWriter
extends StringWriter
{
	public final static String CONTENT_TYPE = "application/json";

	
	public static void main( String args[] )
	{
		JSONWriter writer = new JSONWriter();
		writer.startDocument();

		writer.startObject( "Object1" );
		writer.addQuotedField( "String1", "Value1" );
		writer.addQuotedField( "String2", "Value2", false );
		writer.endObject();

		writer.startObject( "Object2" );
		writer.addQuotedField( "String3", "Value3" );
		writer.addQuotedField( "String4", "Value4", false );
		writer.endObject();

		writer.startList( "List1" );
		writer.startObject();
		writer.addQuotedField( "String5", "Value5" );
		writer.startObject( "Object3" );
		writer.addUnquotedField( "Number1", "100", false );
		writer.endObject( false );
		writer.endObject();
		writer.startObject();
		writer.addQuotedField( "String6", "Value6" );
		writer.startObject( "Object4" );
		writer.addUnquotedField( "Number2", "200", false );
		writer.endObject( false );
		writer.endObject( false );
		writer.endList();

		writer.addQuotedField( "String7", "Value7" );

		writer.addElement( "\"Element1\": \"Data1\"" );
		writer.addElement( "\"Element2\": \"Data2\"", false );
		writer.endDocument();

		System.out.println( "Generated document:" );
		System.out.println( writer.toString() );
	}

	
	public void startDocument()
	{
		startObject();
	}

	public void endDocument()
	{
		endObject( false );
	}

	public void startObject()
	{
		startObject( null );
	}
	
	public void startObject( String field )
	{
		if (field != null) {
			writeIndentation();

			writeQuoteMark();
			write( field );
			writeQuoteMark();
			write( ": " );
		}

		write( '{', (field == null) );
	}
	
	public void endObject()
	{
		endObject( true );
	}
	
	public void endObject( boolean markNext )
	{
		write( '}', true );
		if (markNext) writeNextMark();
	}
	
	public void startList( String field )
	{
		if (field != null) {
			writeIndentation();

			writeQuoteMark();
			write( field );
			writeQuoteMark();
			
			write( ": " );
			write( '[', (field == null) );
		}
	}
	
	public void endList()
	{
		endList( true );
	}
	
	public void endList( boolean markNext )
	{
		write( ']', true );
		if (markNext) writeNextMark();
	}
	
	/**
	 * Adds the key/value pair to the document as a new entry with quoted
	 * parameters and ended with a field separator character (e.g. '"key":
	 * "value",').
	 */
	public void addQuotedField( String key, String value )
	{
		addQuotedField( key, value, true );
	}
	
	/**
	 * Adds the key/value pair to the document as a new entry with quoted
	 * parameters (e.g. "key": "value").  If 'markNext' is true then the
	 * entry is ended with a field separator character (",").  If value is
	 * null then the method does nothing.
	 */
	public void addQuotedField( String key, String value, boolean markNext )
	{
		addField( key, value, true, markNext );
	}

	/**
	 * Adds the key/value pair to the document as a new entry with quoted
	 * key parameter, but unquoted value parameter, and ended with a field
	 * separator character (e.g. '"key": value,').
	 */
	public void addUnquotedField( String key, String value )
	{
		addUnquotedField( key, value, true );
	}
	
	/**
	 * Adds the key/value pair to the document as a new entry with quoted
	 * key parameter, but unquoted value parameter (e.g. "key": value).  If
	 * 'markNext' is true then the entry is ended with a field separator
	 * character (",").  If value is null then the method does nothing.
	 */
	public void addUnquotedField( String key, String value, boolean markNext )
	{
		addField( key, value, false, markNext );
	}

	/**
	 * Adds the element to the document as is and ended with a field separator
	 * character (e.g. 'element,'). The element should have all appropriate
	 * quoting embedded within it.
	 */
	public void addElement( String element )
	{
		addElement( element, true );
	}
	
	/**
	 * Adds the element to the document as is and ended with a field separator
	 * character (e.g. 'element,'). If 'markNext' is true then the entry is
	 * ended with a field separator character (","). The element should have
	 * all appropriate quoting embedded within it. If element is null then
	 * the method does nothing.
	 */
	public void addElement( String element, boolean markNext )
	{
		addUnquotedField( null, element, markNext );
	}

	/**
	 * Adds the key/value pair to the document as a new field entry. If the
	 * parameter <code>quoted</code> is true then the field value is quoted
	 * (e.g. "key": "value"), otherwise the value is unquoted (e.g. "key":
	 * value).  If 'markNext' is true then the entry is ended with a field
	 * separator character (",").  If value is null then the method does
	 * nothing.
	 */
	public void addField( String key, String value, boolean quoted, boolean markNext )
	{
		if (value != null) {
			writeIndentation();

			if (key != null) {
				writeQuoteMark();
				write( key );
				writeQuoteMark();
				
				write( ": " );
			}
			
			if (quoted) writeQuoteMark();
			write( value );
			if (quoted) writeQuoteMark();
			
			if (markNext) {
				writeNextMark();
			}
		}
	}

	public byte[] getBytes()
	{
		return toString().getBytes();
	}
	
	public int length()
	{
		return toString().length();
	}
	
	public void writeNextMark()
	{
		write( ',' );
	}

	public void write( int c )
	{
		write( c, true );
	}

	public void write( int c, boolean writeIndent )
	{
		switch ((char)c) {
			case '[':
			case '{':
				if (writeIndent) writeIndentation();
				super.write(c);
				super.write('\n');
				incrementIndent();
				break;
			case ',':
				super.write(c);
				super.write('\n');
				break;
			case ']':
			case '}':
				super.write('\n');
				decrementIndent();
				if (writeIndent) writeIndentation();
				super.write(c);
				break;
			default:
				super.write(c);
		}
	}


	private void incrementIndent()
	{
		indent++;
	}
	
	private void decrementIndent()
	{
		indent--;
	}
	
	private void writeQuoteMark()
	{
		write( '"' );
	}
	
	private void writeIndentation()
	{
		for (int i = 0; i < indent; i++) {
			super.write( "  " );
		}
	}


	private int indent = 0;

} // end of JSONWriter
