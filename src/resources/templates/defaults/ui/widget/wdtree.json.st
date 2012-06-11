{
 	"id": "{$data.id}",
 	"text": "{$data.name}",
 	"value": "{$data.id}",
 	"showcheck": false,
    "complete": true,
    "isexpand": true,
    "checkstate": 0,
    {if $data.params != null && $data.params != ""}
    "href": "{$data.params}",
    {if $target != null}
    target: "{$target}",
    {/if}{/if}
    {if $data.size > 0}
 		"ChildNodes": [{assign var="count" value=0}{foreach from=$data.children item="item"}
 			{if $count == 1},{/if}{import file="wdtree.json.st" data=$item}
    		{assign var="count" value=1}
 		{/foreach}],
    	"hasChildren": true
    {else}
    "hasChildren": false
 	{/if}
}