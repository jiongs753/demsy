<dl>
    {foreach from=$group item="data"}
	<dt>{import file="Title.st" data=$data}</dt>
	<dd class="d">{import file="_data_txt.st" data=$data}</dd>
    {/foreach}
    {import file="_pager.st"}
	<dd class="b"></dd>
</dl>
{import file="children.st"}