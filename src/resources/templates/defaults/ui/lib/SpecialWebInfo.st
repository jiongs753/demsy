<dl>
	<dt class="t">
    <div>{import file="img.st" img=$data.img}</div>
    <div><span>{$data.obj.desc}</span></div>
    </dt>
	<dd class="d">
	{if $block.scrollDirection == null}
		{import file="_data_txt.st"}
	{else}
		<div id="marquee_{$ui.uiid}" class="marquee" direction="{$block.scrollDirection}" style="width:{$block.position.width}px; height:{$block.position.height}px;overflow: hidden;">
			{if $block.scrollDirection == "left"}
			<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td class="marquee1">{import file="_data_txt.st"}</td>
					<td class="marquee2"></td>
				</tr>
			</table>
			{else}
			<div class="marquee1">{import file="_data_txt.st"}</div>
			<div class="marquee2"></div>
			{/if}
		</div>
	{/if}
	</dd>
</dl>
