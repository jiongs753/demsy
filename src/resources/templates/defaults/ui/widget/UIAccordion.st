<script src="{$scriptPath}/jquery.ui.accordion.js" type="text/javascript"></script>
<script src="{$scriptPath}/demsy.tree.js" type="text/javascript"></script>

<link href="{$stylePath}/jquery.ui.accordion.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$stylePath}/jquery.tree.css" rel="stylesheet" type="text/css" media="screen" />

<style>
.bbit-tree-node-el {
	padding: 1px;
}
.main_bg{
	background-image: url({$contextPath}/themes2/images/index_main_bg.jpg);
	background-repeat: repeat;
	background-position: left top;
}
</style>
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
