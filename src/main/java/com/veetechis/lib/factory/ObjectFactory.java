package com.veetechis.lib.factory;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.veetechis.lib.util.ResourceException;
import com.veetechis.lib.util.Resources;


/**
 * Implements a generic factory for object construction.  This factory creates
 * objects based on the following methods:
 * <ul>
 * 	<li>By <i>class name</i>: creates an object from a given classname and
 * 		parameters.  This method is equivalent to calling
 * 		<code>Class.forName(<i>className</i>)</code> and using reflection to
 * 		initialize the class with parameters.</li>
 *	<li>By class name bound to a lookup key in a given resource bundle.  This
 *		method looks for a class name by key in a resource bundle specified by
 *		calling <code>.ObjectFactory.getInstance(<i>bundleName</i>)</code>.  If
 *		a matching class is found in the bundle, object creation proceeds as
 *		above.</li>
 *	<li>By class name bound to a lookup key in the default resource bundle
 *		<code>ObjectFactory.CLASSES_BUNDLE</code>.  If the resource bundle is
 *		found in the factory's runtime classpath, class lookup and object
 *		creation proceeds as above.
 *	</li>
 * </ul>
 * 
 * @author		pendraconx@gmail.com
 */
public class ObjectFactory
{
	/**
	 * Creates an <code>ObjectFactory</code>.
	 */
	protected ObjectFactory()
	{
		constructors = new HashMap();
	}


	/**
	 * Returns the default factory instance suitable for class name-based object
	 * creation or class name lookup in the default resource bundle
	 * <code>CLASSES_BUNDLE</code>.  The resource bundle must be on factory's
	 * runtime classpath.
	 * 
	 * Throws an exception if the instance can not be returned because the
	 * default resource bundle can not be found.
	 * 
	 * @return					the default object factory.
	 * @throws					FactoryException
	 * 							if the default instance can not be returned.
	 */
	public static synchronized ObjectFactory getInstance()
	throws FactoryException
	{
		if( defaultBundle == null ) {
			try {
				defaultBundle = Resources.loadResourceBundle( CLASSES_BUNDLE );
			}
			catch( ResourceException exc ) {
				throw new FactoryException( exc );
			}
		}

		return getInstance( defaultBundle );
	}

	/**
	 * Returns the factory instance for the given resource bundle.  If
	 * <code>null</code> is passed in the parameter, returns an instance
	 * suitable for classname-based object creation only.
	 * 
	 * @param  bundle			the resource bundle to use for classname lookup.
	 * @return					the object factory associated with the resource
	 * 								bundle.
	 */
	public static synchronized ObjectFactory getInstance( ResourceBundle bundle )
	{
		ObjectFactory factory = getFactory( bundle );

		if( factory == null ) {
			factory = new ObjectFactory();
			factory.setResourceBundle( bundle );
			putFactory( bundle, factory );
		}

		return factory; 
	}

	/**
	 * Looks for and returns a constructor for the specified class whose
	 * parameter types match the object types of the given <code>parms</code>
	 * array.  If the array is <code>null</code> a default constructor is
	 * returned if defined by the class.
	 * 
	 * Throws an exception if a matching constructor is not found for the class.
	 * 
	 * @param  cls				the class to search for a matching constructor.
	 * @param  parms			the parms whose constructor types to match.
	 * @return					the matching constructor.
	 * @throws					FactoryException
	 * 							if a matching constructor is not found.
	 */
	public static Constructor findMatchingConstructor( Class cls, Object[] parms )
	throws FactoryException
	{
		Constructor ctor = null;
		
		try {
			ctor = cls.getConstructor( parseTypes( parms ) );
		}
		catch( NoSuchMethodException exc ) {
			throw new FactoryException( exc );
		}
		
		return ctor;
	}

	/**
	 * Returns an array of class types for the given parameters array.
	 * 
	 * @param  parms			the array of objects to type cast.
	 * @return					the array of class types.
	 */
	public static Class[] parseTypes( Object[] parms )
	{
		int numParms = ( parms == null ? 0 : parms.length);
		Class[] types = new Class[numParms];
		
		for( int i = 0; i < numParms; i++ ) {
			types[i] = parms[i].getClass();
		}
		
		return types;
	}


	/**
	 * Creates an instance of the class identified by the given classname key in
	 * the associated resource bundle.  The class is instantiated using the
	 * constructor whose parameter types match the types of the objects in the
	 * given <code>parms</code> array.  If <code>null</code> is given for the
	 * parameters array, a default class instance is returned.
	 *
	 * Throws an exception if a class name is not found in the underlying
	 * resource bundle for the given key, the class is not found on the runtime
	 * classpath, or if it can not be instantiated because no matching
	 * constructor is found for the class.
	 * 
	 * @param  key				resource bundle classname lookup key.
	 * @param  parms			the parameters with which to instantiate the
	 * 								class.
	 * @return					the class instance.
	 * @throws					FactoryException
	 * 							if an object can not be created.
	 */
	public synchronized Object createObject( String key, Object[] parms )
	throws FactoryException
	{
		Class cls = null;

		try {
			cls = Class.forName( getResourceBundle().getString( key ) );
		}
		catch( MissingResourceException | ClassNotFoundException exc ) {
			throw new FactoryException( exc );
		}
		
		return createObject( cls, parms );
	}

	/**
	 * Creates an object with the given class.  The class is instantiated using
	 * the constructor whose parameter types match the types of the objects in
	 * the given <code>parms</code> array.  If <code>null</code> is given for
	 * the parameters array, a default class instance is returned.
	 * 
	 * Throws an exception if the class can not be instantiated because no
	 * matching constructor is found for the class.
	 *
	 * @param  cls				the class to instantiate.
	 * @param  parms			the parameters with which to instantiate the
	 * 								class.
	 * @return					the class instance.
	 * @throws					FactoryException
	 * 							if an object can not be created.
	 */
	public synchronized Object createObject( Class cls, Object[] parms )
	throws FactoryException
	{
		if( parms == null ) parms = new Object[] {};
		String constructorKey = createConstructorKey( cls, parms );
		
		if( log.isTraceEnabled() ) {
			log.trace( "Create object for class: " + constructorKey );
		}

		Object obj = null;
		try	{
			obj = findConstructor( constructorKey, cls, parms ).newInstance( parms );
		}
		catch( Exception exc ) {
			throw new FactoryException( exc );
		}
		
		return obj;
	}

	
	/**
	 * Returns a hash key for the specified class and the types of the objects
	 * in the given parameter array.
	 * 
	 * @param  cls				the class for which to construct a hash key.
	 * @param  parms			the parms of the class to use.
	 * @return					the hash key. 
	 */
	protected static String createConstructorKey( Class cls, Object[] parms )
	{
		StringBuffer key = new StringBuffer( cls.getName() ).append( "(" );
		for( int i = 0; i < parms.length; i++ ) {
			if( i > 0 ) key.append( "," );
			key.append( parms[i].getClass().getName() );
		}
		key.append( ")" );
		
		return key.toString();
	}

	/**
	 * Returns the factory instance bound to the specified resource bundle in the
	 * internal factory cache.  Return <code>null</code> if the bundle does not
	 * have a factory instance bound to it in the cache.
	 * 
	 * @param  bundle			the bundle for the factory to return.
	 * @return					the factory instance, or null.
	 */
	protected static ObjectFactory getFactory( ResourceBundle bundle )
	{
		return (ObjectFactory) FACTORIES.get( bundle );
	}

	/**
	 * Saves the given factory instance in the internal cache bound to the
	 * specified resource bundle.
	 * 
	 * @param  bundle			the resource bundle of the factory to cache.
	 * @param  factory			the factory instance to cache.
	 */
	protected static void putFactory( ResourceBundle bundle, ObjectFactory factory )
	{
		FACTORIES.put( bundle, factory );
	}


	/**
	 * Sets the resource bundle associated with the factory instance.
	 * 
	 * @param  bundle			the resource bundle to set.
	 */
	protected void setResourceBundle( ResourceBundle bundle )
	{
		this.bundle = bundle;
	}

	/**
	 * Returns the resource bundle associated with the factory instance.
	 * 
	 * @return					the resource bundle.
	 */
	protected ResourceBundle getResourceBundle()
	{
		return bundle;
	}

	/**
	 * Returns an object constructor for the specified constructor key and class
	 * and which has parameter types matching the object types in the given
	 * <code>parms</code> array.
	 * 
	 * Throws an exception if the class does not have a matching constructor.
	 * 
	 * @param  key				the constructor lookup and binding key.
	 * @param  cls				the class to find a matching constructor.
	 * @param  parms			the parms array whose types to match.
	 * @return					the matching constructor.
	 * @throws					FactoryException
	 * 							if the class does not have a matching
	 * 								constructor.
	 */
	protected Constructor findConstructor( String key, Class cls, Object[] parms )
	throws FactoryException
	{
		Constructor ctor = (Constructor) constructors.get( key );
		
		if( ctor == null ) {
			ctor = findMatchingConstructor( cls, parms );
			constructors.put( key, ctor );
		}
		
		return ctor;
	}
	

	/**
	 * Default name of the classname lookup resource bundle.
	 */
	protected static String CLASSES_BUNDLE = "classes";


	private ResourceBundle bundle;
	private HashMap constructors;

	private static ResourceBundle defaultBundle;
	private static Log log = LogFactory.getLog( ObjectFactory.class );

	private final static HashMap FACTORIES = new HashMap();
   
} // end of ObjectFactory
