{foreach from=$children item="ele" key="idx"}
<div class="uimodellib" dataID="{tostring bean=$ele prop="dataID"}" type="{$ele.type}" title="{$ele.desc}" defaultWidth='{tostring bean=$ele prop="defaultWidth"}' defaultHeight='{tostring bean=$ele prop="defaultHeight"}' >
    <textarea class="expr" style="display:none;">{tostring bean=$ele prop="expr"}</textarea>
	{$ele.name}
</div>
{/foreach}