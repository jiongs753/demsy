{prop bean=$data name=$ui.propName pattern=$ui.pattern assign="value"}
{assign var="mode" value=$ui.mode}

{if $mode == "M" || $mode == "E" || $mode == "R" || $mode == "D"}
<div class="textarea">
<textarea id="fld{$ui.id}" class="textarea" name="{$ui.name}"{if $mode=="R"} readonly{/if}{if $mode=="D"} disabled{/if}>{prop bean=$data name=$ui.propName}</textarea>
</div>
{elseif $mode == "I"}
<input id="fld{$ui.id}" name="{$ui.name}" value="{$value}" type="hidden" />
<span>{tohtml bean=$value}</span>
{elseif $mode == "H"}
<input id="fld{$ui.id}" name="{$ui.name}" value="{$value}" type="hidden" />
{elseif $mode == "S"}
<span>{tohtml bean=$value}</span>
{elseif $mode == "P" && $value != null && $value != ""}
<span>{tohtml bean=$value}</span>
{/if}

