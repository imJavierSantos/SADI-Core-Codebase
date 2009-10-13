package ca.wilkinsonlab.sadi.service.example;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
	AsyncLinearRegressionServiceServletTest.class,
	LinearRegressionServiceServletTest.class,
	UniProt2GoServiceServletTest.class,
	UniProt2PdbServiceServletTest.class,
	UniProt2PubmedServiceServletTest.class,
	ErmineJServiceServletTest.class 
})

public class AllTests
{
}