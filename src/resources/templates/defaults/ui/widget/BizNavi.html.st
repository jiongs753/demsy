{if $data != null && $data.size > 0}
<ul>{foreach from=$data.children item="item"}
<li{if $item.status != null} class="jstree-{$item.status}"{/if} id="{$item.id}" rel="{$item.type}"{if $item.params!=null} params="{$item.params}"{/if}><a href="javascript:void(0);">{$item.name}</a>{import file="BizNavi.html.st" data=$item}</li>{/foreach}
</ul>
{/if}
