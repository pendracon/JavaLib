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
		writer.addQuotedField( "String2", "Value2" );
		writer.endObject();

		writer.startObject( "Object2" );
		writer.addStringField( "String3", "Value3" );
		writer.addStringField( "String4", "Value4" );
		writer.endObject();

		writer.startList( "List1" );
		for (int i = 1; i <= 2; i++) {
			writer.startObject();
			writer.startObject( "ListItem" + i + "Object1" );
			writer.addStringField( "String5", "Value5" );
			writer.startObject( "Object3" );
			writer.addUnquotedField( "Number1", "100" );
			writer.endObject();
			writer.endObject();
			writer.startObject( "ListItem" + i + "Object2" );
			writer.addStringField( "String6", "Value6" );
			writer.startObject( "Object4" );
			writer.addNumberField( "Number2", Integer.valueOf(200) );
			writer.addNumberField( "Number3", Float.valueOf(300.3f) );
			writer.addBooleanField( "Boolean1", Boolean.TRUE );
			writer.addBooleanField( "Boolean2", Boolean.FALSE );
			writer.endObject();
			writer.endObject();
			writer.endObject();
		}
		writer.endList();

		writer.addQuotedField( "String7", "Value7" );

		writer.addElement( "\"Element1\": \"Data1\"" );
		writer.addElement( "\"Element2\": \"Data2\"" );
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
		endObject();
	}

	public void startObject()
	{
		startObject( null );
	}
	
	public void startObject( String field )
	{
		if (markNext) {
			writeNextMark();
		}

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
		write( '}', true );
	}
	
	public void startList( String field )
	{
		if (markNext) {
			writeNextMark();
		}

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
		write( ']', true );
	}
	
	/**
	 * Adds the key/value pair to the document as a new string entry. If value
	 * is null then the method does nothing.
	 */
	public void addStringField( String key, String value )
	{
		addQuotedField( key, value );
	}

	/**
	 * Adds the key/value pair to the document as a new numeric entry. If value
	 * is null then the method does nothing.
	 */
	public void addNumberField( String key, Number value )
	{
		if (value != null) {
			addUnquotedField( key, value.toString() );
		}
	}

	/**
	 * Adds the key/value pair to the document as a new boolean entry. If value
	 * is null then the method does nothing.
	 */
	public void addBooleanField( String key, Boolean value )
	{
		if (value != null) {
			addUnquotedField( key, value.toString() );
		}
	}

	/**
	 * Adds the key/value pair to the document as a new entry with quoted
	 * value(e.g. '"key": "value"'). If value is null then the method does
	 * nothing.
	 */
	public void addQuotedField( String key, String value )
	{
		addField( key, value, true );
	}

	/**
	 * Adds the key/value pair to the document as a new entry with unquoted
	 * value parameter (e.g. '"key": value'). If value is null then the method
	 * does nothing.
	 */
	public void addUnquotedField( String key, String value )
	{
		addField( key, value, false );
	}

	/**
	 * Adds the element to the document as is unless element is null (e.g.
	 * 'element,'). The element should have all appropriate quoting embedded
	 * within it. If element is null then the method does nothing.
	 */
	public void addElement( String element )
	{
		addUnquotedField( null, element );
	}

	/**
	 * Adds the key/value pair to the document as a new field entry. If the
	 * parameter <code>quoted</code> is true then the field value is quoted
	 * (e.g. "key": "value"), otherwise the value is unquoted (e.g. "key":
	 * value). If value is null then the method does nothing.
	 */
	public void addField( String key, String value, boolean quoted )
	{
		if (value != null) {
			if (markNext) {
				writeNextMark();
			}

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
			
			markNext = true;
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
				markNext = false;
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
				markNext = true;
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


	private boolean markNext;
	private int indent = 0;

} // end of JSONWriter
