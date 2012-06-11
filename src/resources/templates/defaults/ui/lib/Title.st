{if $block.titleEnabled}
<div class="t">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td class="tl">{import file="img.st" img=$data.img}&nbsp;</td>
		<td class="tt" valign="top">{import file="a.st" href=$data.href target=$data.target title=$data.title name=$data.name}</td>
		<td class="tr" align="right">{if $block.showMoreBtn}<div class="more">
        <a{if $data.href != null} href="{$data.href}"{/if}{if $data.target!=null} target="{$data.target}"{/if}{if $data.title!=null} title="{$data.title}"{/if}>
        更多...</a></div>{/if}</td>
	</tr>
</table>
</div>
{/if}
