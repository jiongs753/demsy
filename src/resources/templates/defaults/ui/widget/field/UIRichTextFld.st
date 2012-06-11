{prop bean=$data name=$ui.propName pattern=$ui.pattern assign="value"}
{assign var="mode" value=$ui.mode}

{if $mode == "M" || $mode == "E"}
<div class="textarea"><textarea id="fld{$ui.id}" class="textarea richtext" name="{$ui.name}">{prop bean=$data name=$ui.propName}</textarea></div>
{elseif $mode == "I"}
<input id="fld{$ui.id}" name="{$ui.name}" value="{$value}" type="hidden" />
<span>{tostring bean=$value}</span>
{elseif $mode == "H"}
<input id="fld{$ui.id}" name="{$ui.name}" value="{$value}" type="hidden" />
{elseif $mode == "S" || $mode == "R" || $mode == "D"}
<span>{tostring bean=$value}</span>
{elseif $mode == "P" && $value != null && $value != ""}
<span>{tostring bean=$value}</span>
{/if}

