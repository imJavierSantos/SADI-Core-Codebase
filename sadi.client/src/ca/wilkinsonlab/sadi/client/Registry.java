package ca.wilkinsonlab.sadi.client;

import java.io.IOException;
import java.util.Collection;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * The main Registry interface.
 * @author Luke McCarthy
 */
public interface Registry
{
	/**
	 * Returns an OntModel containing all predicates that are mapped
	 * to services in this registry.
	 * @return an OntModel containing all predicates mapped in this registry
	 */
	public OntModel getPredicateOntology();
	
	/**
	 * Returns a Service object corresponding to the specified service URI.
	 * @param serviceURI a URI identifying the desired service
	 * @return a Service object corresponding to the specified service URI
	 * @throws IOException if there is a problem communicating with the registry
	 */
	public Service getService(String serviceURI) throws IOException;
	
	/**
	 * Returns a collection of services that can attach the specified predicate.
	 * @param predicate the unescaped URI of the predicate
	 * @return a collection of matching services
	 * @throws IOException if there is a problem communicating with the registry
	 */
	public Collection<? extends Service> findServicesByPredicate(String predicate) throws IOException;
	
	/**
	 * Returns a collection of services that can attach predicates to the
	 * specified subject
	 * @param subject the subject node
	 * @return a collection of matching services
	 * @throws IOException
	 */
	public Collection<? extends Service> findServices(Resource subject) throws IOException;
	
	/**
	 * Returns a collection of services that can attach the specified predicate
	 * to the specified subject.
	 * @param subject the subject node
	 * @param predicate the predicate URI
	 * @return the collection of matching services
	 * @throws IOException if there is a problem communicating with the registry
	 */
	public Collection<? extends Service> findServices(Resource subject, String predicate) throws IOException;
	
	/**
	 * Returns a collection of service/input pairs discovered by searching the
	 * specified input model for instances of the classes consumed by services
	 * in the registry.
	 * @param model the input model
	 * @return the collection of service/input pairs
	 * @throws IOException 
	 */
	public Collection<? extends ServiceInputPair> discoverServices(Model model) throws IOException;
	
	
	/**
	 * Returns a collection of predicates that are mapped to services that
	 * can take the specified subject as input.
	 * @param subject the subject URI
	 * @return a collection of matching predicates
	 * @throws IOException if there is a problem communicating with the registry
	 */
	public Collection<String> findPredicatesBySubject(Resource subject) throws IOException;
	
}