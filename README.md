# Neo4j Index Limited By Count Server Plugin #

* Works just like the REST API query index search for nodes, except you can provide a limit.
* [Neo4j REST API docs](http://components.neo4j.org/neo4j-server/snapshot/rest.html#Index_search_-_Using_a_query_language)
* Based on Neo4j 1.3 Server
* Uses the org.neo4j.server.plugins.ServerPlugin feature.
* Only implemented for Node Indexes right now.

## Use Case ##

* Let's say you have couple million nodes of people with an indexed 'name' property.  Do a search on *a* will try to return a good part of those nodes over your network connection.
* Instead you can use this plugin and do the same search, but with only the first few nodes returned.
* Example:
* `curl -X POST -H Accept:appplication/json -H Content-Type:application/json http://alhost:7474/db/data/ext/NodeIndex/graphdb/limit_by_count -d '{"index":"names", "key":"name", "query":"*a*", "count":4}'`

## Todo ##

* Don't know Maven very well.  Could use some help verifying the POM file.
* Write unit tests

## Getting started ##

* Make sure you have the latest version (1.3) of the Neo4j Server (tested on Community Edition)
* Compile the plugin.  Using maven from the root directory of this project:
* `maven install`
* Copy the jar file (neo4j-index-limited-by-count-plugin-0.1-SNAPSHOT.jar) from the target directory into the plugins directory of your neo4j installation. 
* Restart the neo4j server:
* sudo service neo4j restart
* Check if the extension is installed:
* `curl -H Accept:application/json http://localhost:7474/db/data/`
* You should see the extension listed in the output:

`{
  "relationship_index" : "http://localhost:7474/db/data/index/relationship",
  "node" : "http://localhost:7474/db/data/node",
  "relationship_types" : "http://localhost:7474/db/data/relationship/types",
  "extensions_info" : "http://localhost:7474/db/data/ext",
  "node_index" : "http://localhost:7474/db/data/index/node",
  "reference_node" : "http://localhost:7474/db/data/node/0",
  "extensions" : {
    "NodeIndex" : {
      "limit_by_count" : "http://localhost:7474/db/data/ext/NodeIndex/graphdb/limit_by_count"
    }
  }
}`

* Find the required parameters:
* `curl -H Accept:appplication/json http://localhost:7474/db/data/ext/NodeIndex/graphd/limit_by_count`
* You'll get something like the following:
`{
  "extends" : "graphdb",
  "description" : "Get the results of an index search with a limit on number of results returned.",
  "name" : "limit_by_count",
  "parameters" : [ {
    "description" : "The name of the index",
    "optional" : false,
    "name" : "index",
    "type" : "string"
  }, {
    "description" : "The query key. Example: \"name\"",
    "optional" : false,
    "name" : "key",
    "type" : "string"
  }, {
    "description" : "The query. Example: \"a_value%20with%20spac*\"",
    "optional" : false,
    "name" : "query",
    "type" : "string"
  }, {
    "description" : "The maximum number of results to return, default value (if omitted) is 4.",
    "optional" : true,
    "name" : "count",
    "type" : "integer"
  } ]`
* Based on the above information about parameter types, we can build the following string to retrieve the first 4 nodes from the 'names' index that have an 'a' somewhere in the value associated with the 'name' key.
* `curl -X POST -H Accept:appplication/json -H Content-Type:application/json http://localhost:7474/db/data/ext/NodeIndex/graphdb/limit_by_count -d '{"index":"names", "key":"name", "query":"*a*", "count":4}'`
* Should return 4 nodes very quickly which is nice considering there could be millions of matching nodes.
 
## Requirements ##

* Neo4j 1.3 Server
* curl
