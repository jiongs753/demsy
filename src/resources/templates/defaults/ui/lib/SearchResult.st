
<dl>
	<dt>
	{import file="Title.st"}
	</dt>
	<dd class="d">
    {if $data0 == null && $data1 == null && $data2 == null && $data3 == null}
        <div style="color: red;height: 200px;">没有查询到满足条件的记录</div>
    {else}
    	{if $block.scrollDirection == null}
    		{import file="_search_result.st" data=$data0}
    		{import file="_search_result.st" data=$data1}
            {import file="_search_result.st" data=$data2}
            {import file="_search_result.st" data=$data3}
    	{else}
    		<div id="marquee_{$ui.uiid}" class="marquee" direction="{$block.scrollDirection}" style="width:{$block.position.width}px; height:{$block.position.height}px;overflow: hidden;">
    			{if $block.scrollDirection == "left"}
    			<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    				<tr>
    					<td class="marquee1">
                        {import file="_search_result.st" data=$data0}
                        {import file="_search_result.st" data=$data1}
                        {import file="_search_result.st" data=$data2}
                        {import file="_search_result.st" data=$data3}
                        </td>
    					<td class="marquee2"></td>
    				</tr>
    			</table>
    			{else}
    			<div class="marquee1">
                {import file="_search_result.st" data=$data0}
                {import file="_search_result.st" data=$data1}
                {import file="_search_result.st" data=$data2}
                {import file="_search_result.st" data=$data3}
                </div>
    			<div class="marquee2"></div>
    			{/if}
    		</div>
    	{/if}
        {import file="_search_pager.st"}
    {/if}
	</dd>
	<dd class="b"></dd>
</dl>