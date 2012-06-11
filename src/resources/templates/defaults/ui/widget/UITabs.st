{if $data.size > 0}

<div id="{$ui.uiid}" style="display: none; height:532px;">
	<ul>{foreach from=$data.children item="item"}
		<li><a href="{$item.params}">{$item.name}</a></li>{/foreach}
	</ul>
</div>
<script type="text/javascript">
<!--
$(document).ready(function(){$.doTimeout(100, function() {

    $("#{$ui.uiid}").tabs({
        //cache: true
        //,collapsible: true
    }).show();
	
});});

//-->
</script>
{/if}