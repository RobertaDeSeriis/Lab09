package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.DepthFirstIterator;


import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	
	private Graph <Country, DefaultEdge> grafo; 
	private BordersDAO dao; 
	private Map<Integer,Country> country; 
	
	public Model() {
		dao= new BordersDAO(); 	
		country= dao.loadAllCountries(); 
	}

	public void creaGrafo(int anno) {
		grafo = new SimpleGraph<>(DefaultEdge.class);
		
		List<Country> vertici= dao.loadVertex(anno);
		
		Graphs.addAllVertices(this.grafo, vertici);
		System.out.println("Vertici: " +grafo.vertexSet().size()); 
		
		//aggiungi archi
		for (Border b: this.dao.getBorder()) {
			Graphs.addEdgeWithVertices(this.grafo, b.getC1(),b.getC2()); 
		}
	}
	
	public int getComponenteConnessa() {
		ConnectivityInspector<Country, DefaultEdge> connesse= new ConnectivityInspector<Country, DefaultEdge>(this.grafo);
		return connesse.connectedSets().size(); 
		
		
	}
	public Set<Country> getRaggiungibili(Country c) {
		Set<Country> visitati= new HashSet<>(); 
		DepthFirstIterator<Country, DefaultEdge> it= new DepthFirstIterator<Country, DefaultEdge> (this.grafo, c); 
		
		while(it.hasNext()) {
			visitati.add(it.next());
		}
		
		//connectoringSector.
		return visitati;
	}
	
	public Map<Integer, Country> getCountry() {
		return country;
	}

	public Map<Country,Integer> getNumConfini(){
		return dao.statiConfinanti();
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size(); 
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size(); 
	}
	
	
	
	
}
