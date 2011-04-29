package com.toddbiz.neo4j.plugins;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;

import org.neo4j.server.plugins.ServerPlugin;
import org.neo4j.server.plugins.Description;
import org.neo4j.server.plugins.Name;
import org.neo4j.server.plugins.PluginTarget;
import org.neo4j.server.plugins.Source;
import org.neo4j.server.plugins.Parameter;


/**
 * Extensions to the Index service.
 *
 * @author Todd Chaffee
 * Some rights reserved.  Please contact the author for copyright information.
 * 
 */
public class NodeIndex extends ServerPlugin 
{
    // http://localhost:7474/db/data/index/node/names/{key}/{value}    
    
    @Name( "limit_by_count" )
    @Description( "Get the results of a node index search with a limit on " + 
            "number of results returned." )
    @PluginTarget( GraphDatabaseService.class )
    public Iterable<Node> getIndexLimitCount(
        @Source GraphDatabaseService graphDb,
        @Description("The name of the index")
        @Parameter( name = "index", optional=false ) String indexName,
        @Description("The query key. Example: \"name\"")
        @Parameter( name = "key", optional=false ) String key,
        @Description("The query. Example: \"a_value%20with%20spac*\"") 
        @Parameter( name = "query", optional=false ) String query,
        @Description( "The maximum number of results to return, default " + 
            "value (if omitted) is 4." ) 
        @Parameter( name = "count", optional = true ) Integer count 
    )
    {            
    

        Index<Node> index = graphDb.index().forNodes( indexName );
        
        ArrayList<Node> returnNodes = new ArrayList<Node>();

        int counted = 0;
        for ( Node node : index.query( key, query ) )
        {
            counted++;
            returnNodes.add( node );
            if ( counted >= count ) break;
        }
        return returnNodes;
    }
}