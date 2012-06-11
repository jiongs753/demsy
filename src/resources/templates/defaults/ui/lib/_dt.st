{if $block.titleEnabled}
	<dt>
		<table class="t" width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td class="tl">{import file="img.st" img=$data.img}&nbsp;</td>
				<td class="tt" valign="top">{import file="a.st" href=$data.href target=$data.target title=$data.title name=$data.name}</td>
				<td class="tr">&nbsp;</td>
			</tr>
		</table>
	</dt>
{/if}