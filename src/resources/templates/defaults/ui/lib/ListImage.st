<dl>
	<dt>
	{import file="Title.st"}
	</dt>
	<dd class="d">
	{if $block.scrollDirection == null}
		{import file="_data_img.st"}
	{else}
		<div id="marquee_{$ui.uiid}" class="marquee" direction="{$block.scrollDirection}" style="width:{$block.position.width}px; height:{$block.position.height}px;overflow: hidden;">
			{if $block.scrollDirection == "left"}
			<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td class="marquee1">{import file="_data_img.st"}</td>
					<td class="marquee2"></td>
				</tr>
			</table>
			{else}
			<div class="marquee1">{import file="_data_img.st"}</div>
			<div class="marquee2"></div>
			{/if}
		</div>
	{/if}
    {import file="_pager.st"}
	</dd>
	<dd class="b"></dd>
</dl>