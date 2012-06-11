<ol id="imgslide_{$ui.uiid}" class="slide d" style="width: 100%; height: 100%;">
	{foreach from=$data.items item="item"}
	<li>{import file="a.st" href=$item.href target=$item.target title=$item.title img=$item.img}
		<p>{$item.name}</p>
	</li>
	{/foreach}
</ol>
