<div id="{$ui.uiid}" style="display:none; position: relative; top: -1px;">{foreach from=$data.children item="item"}
	<h3>
		<a hideFocus href="javascript:void(0);">{$item.name}</a>
	</h3>
	<div>
		<div id="tree{$item.id}"></div>
		<script type='text/javascript'>
		$(document).ready(function(){$.doTimeout(1, function() {
	    
			$("#tree{$item.id}").treeview({
				showcheck: true,
				theme: "bbit-tree-arrows",
				cbiconpath: '{$imagePath}/tree',
				data: [{assign var="count" value=0}{foreach from=$item.children item="item2"}
	 			{if $count == 1},{/if}{import file="wdtree.json.st" data=$item2}
	 	    		{assign var="count" value=1}
	 	 		{/foreach}]
			});
		    
	 	});});

		</script>
	</div>{/foreach}
</div>

<script type='text/javascript'>
<!--
$(document).ready(function(){$.doTimeout(1, function() {
 
	$("#{$ui.uiid}").show().accordion({
		autoHeight: true
		,navigation: false
		,animated: false
		,fillSpace: true
		,collapsible: true
	});
    
});});

//-->
</script>
