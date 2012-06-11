[{assign var="count" value=0}{foreach from=$data.children item="item"}{if $count == 1},{/if}{
 	"id": "{$item.id}",
 	"text": {tojson bean=$item prop="name"},
 	"value": "{$item.id}",
 	"showcheck": true,
 	{prop bean=$item name="isSelf" assign="isSelf"}{if $isSelf == "true"}"cascadecheck": false,{/if}
 	"rel": "{$item.type}",
 	{if $item.params!=null}"params": "{$item.params}",{/if}
    "complete": true,
    "isexpand": {if $item.status == "open"}true{else}false{/if},
    "checkstate": 0,
    {if $item.size > 0}
 		"ChildNodes": {import file="BizNavi.json.st" data=$item},
 	{/if}
    "hasChildren": {if $item.type!="leaf"}true{else}false{/if}
}{assign var="count" value=1}{/foreach}]